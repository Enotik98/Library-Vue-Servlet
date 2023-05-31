package com.libraryspring.utils;

import java.security.MessageDigest;
import java.util.Base64;

public class PasswordUtils {
    public static String hashPassword(String password){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedByte = digest.digest(password.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hashedByte);
        }catch (Exception e){
            System.out.println("hash :" + e);
            return null;
        }
    }
    public static boolean verifyPassword(String password, String hash){
        System.out.println("password " + hashPassword(password));
        System.out.println("hash " + hash);
        return hashPassword(password).equals(hash);
    }
}
