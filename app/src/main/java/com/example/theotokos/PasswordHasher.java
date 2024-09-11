package com.example.theotokos;
import android.os.Build;
import androidx.annotation.RequiresApi;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;

public class PasswordHasher {

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 128;

    public static String hashPassword(String password) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hashedPassword = f.generateSecret(spec).getEncoded();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hashedPassword);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return password;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean validatePassword(String storedHash, String plaintextPassword) {
        try {
            String[] parts = storedHash.split(":");
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] storedHashBytes = Base64.getDecoder().decode(parts[1]);

            KeySpec spec = new PBEKeySpec(plaintextPassword.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hashedPassword = f.generateSecret(spec).getEncoded();

            return java.util.Arrays.equals(storedHashBytes, hashedPassword);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}