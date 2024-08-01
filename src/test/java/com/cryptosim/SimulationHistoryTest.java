package com.cryptosim;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.crypto.SecretKey;
import java.io.*;
import java.nio.file.*;

public class SimulationHistoryTest {

    private static final String TEST_USER = "testuser";
    private static final String TEST_TEXT = "OVO JE TESTNI TEKST";
    private static final String TEST_ALGORITHM = "AES";
    private static final String TEST_KEY = "testkey";
    private static final String TEST_CIPHER = "testcipher";

    @BeforeAll
    public static void setup() throws Exception {
        // Kreira testni direktorijum
        Files.createDirectories(Paths.get("user_files/" + TEST_USER));
    }

    @AfterAll
    public static void teardown() throws Exception {
        // Briše testni direktorijum i fajlove
        Path testDir = Paths.get("user_files/" + TEST_USER);
        if (Files.exists(testDir)) {
            Files.walk(testDir)
                    .map(Path::toFile)
                    .forEach(File::delete);
            Files.deleteIfExists(testDir);
        }
    }

    @Test
    public void testSaveSimulation() throws Exception {
        SimulationHistory.saveSimulation(TEST_USER, TEST_TEXT, TEST_ALGORITHM, TEST_KEY, TEST_CIPHER);

        Path filePath = Paths.get("user_files/" + TEST_USER, TEST_USER + "_history.enc");
        assertTrue(Files.exists(filePath));
    }

    @Test
    public void testReadSimulations() throws Exception {
        SimulationHistory.saveSimulation(TEST_USER, TEST_TEXT, TEST_ALGORITHM, TEST_KEY, TEST_CIPHER);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        SimulationHistory.readSimulations(TEST_USER);

        System.setOut(originalOut);
        String output = outputStream.toString();

        assertTrue(output.contains(TEST_TEXT));
        assertTrue(output.contains(TEST_ALGORITHM));
        assertTrue(output.contains(TEST_KEY));
        assertTrue(output.contains(TEST_CIPHER));
    }

    @Test
    public void testNoSimulationHistory() throws Exception {
        // Briše fajl sa istorijom ako postoji
        Path filePath = Paths.get("user_files/" + TEST_USER, TEST_USER + "_history.enc");
        Files.deleteIfExists(filePath);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        SimulationHistory.readSimulations(TEST_USER);

        System.setOut(originalOut);
        String output = outputStream.toString();

        assertTrue(output.contains("nema istoriju simulacija"));
    }

    @Test
    public void testEncryptDecrypt() throws Exception {
        SecretKey secretKey = SimulationHistory.getUserAESKey(TEST_USER);
        String originalText = "This is a secret message";

        byte[] encryptedData = SimulationHistory.encryptData(originalText, secretKey);
        String decryptedText = SimulationHistory.decryptData(encryptedData, secretKey);

        assertEquals(originalText, decryptedText);
    }
}