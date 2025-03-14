package com.kamalteja.brevify.shortenerService.util;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class EncryptionUtil {
    private static final String BASE62_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public String hashUrl(String longUrl) {
        // Todo: Change logic to use User Id instead of current timestamp
        long timestamp = System.currentTimeMillis();
        String input = timestamp + longUrl;

        byte[] hash = sha256(input);
        long number = bytesToLong(hash);
        return encodeBase62(number);
    }

    private byte[] sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    private long bytesToLong(byte[] hash) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value = (value << 8) | (hash[i] & 0xFF);
        }
        return value < 0 ? -value : value;
    }

    private String encodeBase62(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(BASE62_ALPHABET.charAt((int) (num % 62)));
            num /= 62;
        }
        return sb.reverse().toString();
    }
}
