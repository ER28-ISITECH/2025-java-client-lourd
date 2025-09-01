package fr.isitech.service;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

public class StringEncryptor {
    private final SecretKeySpec secretKey;
    private final IvParameterSpec ivSpec;

    public StringEncryptor(String encryptionKey) throws Exception {
        byte[] keyBytes = encryptionKey.getBytes(StandardCharsets.UTF_8);
        if (!(keyBytes.length == 16 || keyBytes.length == 24 || keyBytes.length == 32)) {
            throw new IllegalArgumentException("Encryption key must be 16, 24, or 32 bytes long.");
        }
        this.secretKey = new SecretKeySpec(keyBytes, "AES");
        byte[] iv = new byte[16];
        this.ivSpec = new IvParameterSpec(iv);
    }

    public String encrypt(String input) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encryptedBytes = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String input) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decodedBytes = Base64.getDecoder().decode(input);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static String encryptWithPassword(String input, String password) throws Exception {
        SecretKeySpec key = generateKeyFromPassword(password);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[16]));
        byte[] encryptedBytes = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decryptWithPassword(String input, String password) throws Exception {
        SecretKeySpec key = generateKeyFromPassword(password);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[16]));
        byte[] decodedBytes = Base64.getDecoder().decode(input);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    private static SecretKeySpec generateKeyFromPassword(String password) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), new byte[16], 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    public static String generateRandomKey(int length) {
        StringBuilder key = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            key.append(chars.charAt(index));
        }
        return key.toString();
    }
}
