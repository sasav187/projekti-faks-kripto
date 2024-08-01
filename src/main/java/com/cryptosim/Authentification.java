package com.cryptosim;

import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class Authentification {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String USER_DIR_PATH = "user_files/";
    private static final String CREDENTIALS_SUFFIX = ".cred";

    public boolean authenticate(String certPath, String username, String password) throws Exception {

        X509Certificate cert = loadCertificate(certPath);
        String certUsername = extractUsernameFromCert(cert);

        if (!certUsername.equals(username)) {
            return false;
        }

        return verifyPassword(username, password);
    }

    X509Certificate loadCertificate(String certPath) throws Exception {
        try (FileInputStream fis = new FileInputStream(certPath)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
            return (X509Certificate) cf.generateCertificate(fis);
        }
    }

    String extractUsernameFromCert(X509Certificate cert) throws Exception {

        JcaX509CertificateHolder certHolder = new JcaX509CertificateHolder(cert);
        String dn = certHolder.getSubject().toString();
        String[] dnParts = dn.split(",");

        for (String part : dnParts) {
            if (part.trim().startsWith("CN=")) {
                return part.trim().substring(3);
            }
        }

        return null;
    }

    boolean verifyPassword(String username, String password) throws Exception {

        String credPath = USER_DIR_PATH + username + "/" + username + CREDENTIALS_SUFFIX;
        if (!Files.exists(Paths.get(credPath))) {
            return false;
        }

        String storedCred = new String(Files.readAllBytes(Paths.get(credPath)));
        String[] parts = storedCred.split(":");
        if (parts.length != 2) {
            return false;
        }

        String storedUsername = parts[0];
        String storedPassword = parts[1];

        return storedUsername.equals(username) && storedPassword.equals(password);
    }

    public PrivateKey loadPrivateKey(String keyPath) throws Exception {

        byte[] keyBytes = Files.readAllBytes(Paths.get(keyPath));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA", "BC");

        return kf.generatePrivate(spec);
    }
}
