package fr.isitech.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Objects;

public class PasswordHasher {

    public static String hashPassword(String password) {
        Objects.requireNonNull(password, "Password cannot be null");
        byte[] salt = generateSalt();
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, Constants.ITERATIONS, Constants.KEY_LENGTH);
        byte[] hash = generateHash(spec);
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifyPassword(String password, String storedHash) {
        Objects.requireNonNull(password, "Password cannot be null");
        Objects.requireNonNull(storedHash, "Stored hash cannot be null");
        String[] parts = storedHash.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid stored hash format");
        }
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] hash = Base64.getDecoder().decode(parts[1]);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, Constants.ITERATIONS, Constants.KEY_LENGTH);
        byte[] newHash = generateHash(spec);
        return slowEquals(hash, newHash);
    }

    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[Constants.SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    private static byte[] generateHash(KeySpec spec) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(Constants.ALGORITHM);
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
}
