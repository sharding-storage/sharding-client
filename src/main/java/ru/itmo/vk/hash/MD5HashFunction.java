package ru.itmo.vk.hash;

import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5HashFunction implements HashFunction {
    @Override
    public int hash(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(key.getBytes());
            return new BigInteger(1, digest).intValue();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }
}
