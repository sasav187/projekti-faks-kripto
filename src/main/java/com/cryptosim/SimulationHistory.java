package com.cryptosim;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.*;
import java.util.*;

public class SimulationHistory {

    private static final String USER_FILES_DIR = "user_files/";
    private static final String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding"; // Transformacija za AES algoritam (CBC sa paddingom)
    private static final int AES_KEY_SIZE = 256;

    public static void saveSimulation(String username, String text, String algorithm, String key, String cipher) throws Exception {
        createOrVerifyUserDirectory(username);
        SecretKey secretKey = getUserAESKey(username);

        String simulationData = text + " | " + algorithm + " | " + key + " | " + cipher;
        byte[] encryptedData = encryptData(simulationData, secretKey);

        // Base64 enkodovanje podataka
        String base64EncryptedData = Base64.getEncoder().encodeToString(encryptedData);

        String filename = username + "_history.enc";
        Path filePath = Paths.get(USER_FILES_DIR + username, filename);

        try (FileOutputStream fos = new FileOutputStream(filePath.toFile(), true)) {
            fos.write(base64EncryptedData.getBytes(StandardCharsets.UTF_8));
            fos.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
        }
    }

    public static void readSimulations(String username) throws Exception {
        createOrVerifyUserDirectory(username);

        String filename = username + "_history.enc";
        Path filePath = Paths.get(USER_FILES_DIR + username, filename);

        if (!Files.exists(filePath)) {
            System.out.println("Korisnik " + username + " nema istoriju simulacija.");
            return;
        }

        // Generisanje tajnog ključa za korisnika
        SecretKey secretKey = getUserAESKey(username);

        List<String> encryptedLines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

        System.out.println("Istorija simulacija za korisnika " + username + ":");
        for (String encryptedLine : encryptedLines) {
            if (!encryptedLine.trim().isEmpty()) {
                byte[] encryptedData = Base64.getDecoder().decode(encryptedLine);
                String decryptedText = decryptData(encryptedData, secretKey);
                System.out.println(decryptedText);
            }
        }
    }

    static byte[] encryptData(String data, SecretKey secretKey) throws Exception {

        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        return concatenateByteArrays(iv, encryptedData);
    }

    static String decryptData(byte[] encryptedData, SecretKey secretKey) throws Exception {

        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        byte[] iv = Arrays.copyOfRange(encryptedData, 0, 16);
        byte[] encryptedText = Arrays.copyOfRange(encryptedData, 16, encryptedData.length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] decryptedData = cipher.doFinal(encryptedText);

        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    static SecretKey getUserAESKey(String username) throws Exception {

        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = sha.digest(username.getBytes(StandardCharsets.UTF_8));
        key = Arrays.copyOf(key, 16); // Koristimo prvih 16 bajtova za AES

        return new SecretKeySpec(key, "AES");
    }

    private static void createOrVerifyUserDirectory(String username) throws IOException {

        Path userDir = Paths.get(USER_FILES_DIR + username);

        if (!Files.exists(userDir)) {
            Files.createDirectories(userDir);
        }
    }

    private static byte[] concatenateByteArrays(byte[] a, byte[] b) {

        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);

        return result;
    }
}
