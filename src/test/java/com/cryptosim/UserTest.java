package com.cryptosim;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class UserTest {

    @BeforeAll
    public static void setup() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    public void testUser() {
        try {

            String path = "user_files/";
            String username = "testuser";
            String password = "testpassword";

            // Registracija korisnika
            User.registerUser(username, password);

            // Provjera da li su sertifikat i ključ kreirani
            File certFile = new File(path + username + "/" + username + ".cert");
            File keyFile = new File(path  + username + "/" + username + ".key");
            File credFile = new File(path  + username + "/" + username +  ".cred");

            assertTrue(certFile.exists());
            assertTrue(keyFile.exists());
            assertTrue(credFile.exists());

            // Učitavanje i provjera sertifikata
            FileInputStream fis = new FileInputStream(certFile);
            CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
            X509Certificate userCert = (X509Certificate) cf.generateCertificate(fis);
            fis.close();

            // Provjera validnosti sertifikata
            FileInputStream caFis = new FileInputStream("CAcert.cert");
            X509Certificate caCert = (X509Certificate) cf.generateCertificate(caFis);
            caFis.close();

            userCert.verify(caCert.getPublicKey());

        } catch (Exception e) {
            e.printStackTrace();
            fail("Test nije uspeo zbog izuzetka: " + e.getMessage());
        }
    }
}
