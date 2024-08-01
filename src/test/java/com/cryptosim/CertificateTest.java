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

public class CertificateTest {

    @BeforeAll
    public static void setup() {

        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    public void testGenerateCA() {
        try {
            // Generisanje CA sertifikata
            Certificate.generateCA();

            // Provjeravamo da li su fajlovi kreirani
            File certFile = new File("CAcert.cert");
            File keyFile = new File("CAkey.key");

            assertTrue(certFile.exists());
            assertTrue(keyFile.exists());

            // Učitavanje i provjera sertifikata
            FileInputStream fis = new FileInputStream(certFile);
            CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
            X509Certificate caCert = (X509Certificate) cf.generateCertificate(fis);
            fis.close();

            // Provjera validnosti sertifikata
            caCert.verify(caCert.getPublicKey());

        } catch (Exception e) {
            e.printStackTrace();
            fail("Test nije uspeo zbog izuzetka: " + e.getMessage());
        }
    }
}
