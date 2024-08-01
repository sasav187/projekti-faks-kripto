package com.cryptosim;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import static org.junit.Assert.*;

public class AuthentificationTest {

    private static final String TEST_CERT_PATH = "user_files/testuser/testuser.cert";
    private static final String TEST_KEY_PATH = "user_files/testuser/testuser.key";
    private static final String TEST_USER_DIR = "user_files/testuser/";
    private static final String TEST_CREDENTIALS_PATH = TEST_USER_DIR + "testuser.cred";

    private Authentification auth;

    @Before
    public void setUp() throws Exception {
        auth = new Authentification();
        // Ensure the test directory and files are set up
        Files.createDirectories(Paths.get(TEST_USER_DIR));
        // Write a test credential file
        Files.write(Paths.get(TEST_CREDENTIALS_PATH), "testuser:testpassword".getBytes());
    }

    @Test
    public void testLoadCertificate() throws Exception {
        X509Certificate cert = auth.loadCertificate(TEST_CERT_PATH);
        assertNotNull(cert);
        assertEquals("CN=testuser, O=My Organization, L=My City, ST=My State, C=My Country", cert.getSubjectX500Principal().getName());
    }

    @Test
    public void testExtractUsernameFromCert() throws Exception {
        X509Certificate cert = auth.loadCertificate(TEST_CERT_PATH);
        String username = auth.extractUsernameFromCert(cert);
        assertEquals("testuser", username);
    }

    @Test
    public void testVerifyPassword() throws Exception {
        boolean isAuthenticated = auth.verifyPassword("testuser", "testpassword");
        assertTrue(isAuthenticated);

        isAuthenticated = auth.verifyPassword("testuser", "wrongpassword");
        assertFalse(isAuthenticated);
    }

    @Test
    public void testAuthenticate() throws Exception {
        boolean isAuthenticated = auth.authenticate(TEST_CERT_PATH, "testuser", "testpassword");
        assertTrue(isAuthenticated);

        isAuthenticated = auth.authenticate(TEST_CERT_PATH, "testuser", "wrongpassword");
        assertFalse(isAuthenticated);

        isAuthenticated = auth.authenticate(TEST_CERT_PATH, "wronguser", "testpassword");
        assertFalse(isAuthenticated);
    }

    @Test
    public void testLoadPrivateKey() throws Exception {
        PrivateKey privateKey = auth.loadPrivateKey(TEST_KEY_PATH);
        assertNotNull(privateKey);
    }
}


