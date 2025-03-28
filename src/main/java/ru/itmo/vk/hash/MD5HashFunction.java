package ru.itmo.vk.hash;

import dev.mccue.guava.hash.Hashing;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class MD5HashFunction implements HashFunction {
    @Override
    public int hash(String key) {
        //return Hashing.murmur3_32_fixed().hashUnencodedChars(key).asInt();
//        return Hashing.sha256()
//            .hashString(key, StandardCharsets.UTF_8)
//            .asInt();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(key.getBytes());
            return new BigInteger(1, digest).intValue();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }
}
