package com.cryptosim;

import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import javax.security.auth.x500.X500Principal;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

public class User {

    // Dodavanje Bouncy Castle providera za podršku
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    // Putanje do CA sertifikata i privatnog ključa
    private static final String USER_FILES_DIR = "user_files/";
    private static final String CA_CERT_PATH = "CAcert.cert";
    private static final String CA_KEY_PATH = "CAkey.key";

    // Registracija korisnika
    public static void registerUser(String username, String password) throws Exception {

        // Generisanje ključeva za korisnika
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        // Učitavanje CA sertifikata
        FileInputStream fis = new FileInputStream(CA_CERT_PATH);
        CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
        X509Certificate caCert = (X509Certificate) cf.generateCertificate(fis);
        fis.close();

        // Učitavanje privatnog ključa
        PrivateKey caPrivateKey = loadPrivateKey(CA_KEY_PATH);

        // Postavljanje perioda validnosti
        long now = System.currentTimeMillis();
        Date startDate = new Date(now);
        Date endDate = new Date(now + 365L * 24 * 60 * 60 * 1000);

        // Definisanje subjekta sertifikata
        X500Principal subject = new X500Principal("CN=" + username + ", O=My Organization, L=My City, ST=My State, C=My Country");

        // Kreiranje sertifikata za korisnika
        X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                caCert.getSubjectX500Principal(),
                BigInteger.valueOf(now),
                startDate,
                endDate,
                subject,
                keyPair.getPublic()
        );

        // Potpisivanje sertifikata sa CA privatnim ključem
        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA").setProvider("BC").build(caPrivateKey);
        X509Certificate userCert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBuilder.build(signer));

        // Verifikacija sa CA javnim ključem
        userCert.verify(caCert.getPublicKey());

        // Kreiranje korisničkog direktorijuma ako ne postoji
        Path userDir = Paths.get(USER_FILES_DIR + username);
        if (!Files.exists(userDir)) {
            Files.createDirectories(userDir);
        }

        // Definisanje putanja za čuvanje sertifikata i privatnog ključa
        String certPath = userDir.toString() + "/" + username + ".cert";
        String keyPath = userDir.toString()  + "/" + username + ".key";

        // Čuvanje korisničkog sertifikata i privatnog ključa
        try (FileOutputStream fos = new FileOutputStream(certPath)) {
            fos.write(userCert.getEncoded());
        }
        try (FileOutputStream fos = new FileOutputStream(keyPath)) {
            fos.write(keyPair.getPrivate().getEncoded());
        }

        System.out.println("Korisnički sertifikat i ključ su uspješno kreirani.");
        System.out.println("Putanja do sertifikata: " + certPath);
        System.out.println("Putanja do privatnog ključa: " + keyPath);

        saveUserCredentials(username, password);
    }

    // Metoda za učitavanje privatnog ključa sa zadate putanje
    private static PrivateKey loadPrivateKey(String keyPath) throws Exception {

        FileInputStream fis = new FileInputStream(keyPath);
        byte[] keyBytes = fis.readAllBytes();
        fis.close();

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA", "BC");
        return kf.generatePrivate(spec);
    }

    // Metoda za čuvanje korisničkih podataka u fajl
    private static void saveUserCredentials(String username, String password) {

        try (FileOutputStream fos = new FileOutputStream(USER_FILES_DIR + username + "/" + username + ".cred")) {
            fos.write((username + ":" + password).getBytes());
        } catch (Exception e) {
            System.out.println("Greška pri čuvanju korisničkih podataka u fajl.");
        }
    }
}