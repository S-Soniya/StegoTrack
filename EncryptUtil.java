import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

public class EncryptUtil {

    // Generate AES key from password
    private static SecretKeySpec getSecretKey(String myKey) throws Exception {
        byte[] key = myKey.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        key = sha.digest(key);
        byte[] keyBytes = new byte[16]; // use only first 128 bits (16 bytes)
        System.arraycopy(key, 0, keyBytes, 0, 16);
        return new SecretKeySpec(keyBytes, "AES");
    }

    public static String encrypt(String message, String key) throws Exception {
        SecretKeySpec secretKey = getSecretKey(key);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedMessage, String key) throws Exception {
        SecretKeySpec secretKey = getSecretKey(key);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedMessage);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, "UTF-8");
    }
}
