package com.cryptosim;

import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import javax.security.auth.x500.X500Principal;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;
public class Certificate {

    // Dodavanje Bouncy Castle za podršku
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    // Metoda za generisanje CA sertifikata i ključa
    public static void generateCA() throws Exception {

        //
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair(); // Generisanje para ključeva

        long now = System.currentTimeMillis();
        Date startDate = new Date(now);
        Date endDate = new Date(now + 365L * 24 * 60 * 60 * 1000); // Datum isteka serifikata nakon jedne godine

        X500Principal issuer = new X500Principal("CN=My CA, O=My Organization, L=My City, ST=My State, C=My Country");

        X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                issuer,
                BigInteger.valueOf(now), // Serijski broj
                startDate,
                endDate,
                issuer, // Samopotpisujući sertifikat
                keyPair.getPublic()
        );

        // Kreiranje potpisivača sadržaja koristeći SHA256 sa RSA algoritmom i privatni ključ
        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA").setProvider("BC").build(keyPair.getPrivate());

        X509Certificate caCert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBuilder.build(signer));

        caCert.verify(keyPair.getPublic());

        try (FileOutputStream fos = new FileOutputStream("CAcert.cert")) {
            fos.write(caCert.getEncoded());
        }

        try (FileOutputStream fos = new FileOutputStream("CAkey.key")) {
            fos.write(keyPair.getPrivate().getEncoded());
        }

        System.out.println("CA sertifikat i ključ su uspješno kreirani.");
    }
}