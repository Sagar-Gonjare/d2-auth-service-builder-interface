package org.dnyanyog.encryption;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;

@Component
public class EncryptionService {
  private static final String SECRET_KEY = "5F2708070EF2F88AB8123AB1036880E4";

  private static final String ALGORITHM = "AES";
  private static SecretKey secretKey;
  private static Cipher cipher;

  static {
    secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
    try {
      cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    }
  }

  public static String encrypt(String data) throws Exception {
    byte[] encryptedByte = cipher.doFinal(data.getBytes());

    return Base64.getEncoder().encodeToString(encryptedByte);
  }

  public static String decrypt(String data) throws Exception {
    byte[] decryptedBytes = cipher.doFinal(data.getBytes());

    return new String(decryptedBytes, StandardCharsets.UTF_8);
  }

  public static SecretKey geneateAESKey() throws NoSuchAlgorithmException {
    try {
      KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
      keyGenerator.init(256);
      return keyGenerator.generateKey();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Error generating AES Key" + e);
    }
  }

  //	public static SecretKey geneateAESKey() throws NoSuchAlgorithmException {
  //		KeyGenerator keyGenerator= KeyGenerator.getInstance("AES");
  //		keyGenerator.init(256);
  //		return keyGenerator.generateKey();
  //	}

  //	public static String encrypt (String plainText, SecretKey SKey) throws Exception,
  // NoSuchPaddingException {
  //		Cipher cipher = Cipher.getInstance("AES");
  //		cipher.init(Cipher.ENCRYPT_MODE, SKey);
  //		byte[] plainTextByte = plainText.getBytes(StandardCharsets.UTF_8);
  //		byte[] encryptedByte = cipher.doFinal(plainTextByte);
  //
  //		String encryptedDataInString = Base64.getEncoder().encodeToString(encryptedByte);
  //
  //		return encryptedDataInString;
  //
  //	}

  //	public static String decrypt (String encryptData, SecretKey key) throws Exception {
  //		Cipher cipher = Cipher.getInstance("AES");
  //		cipher.init(Cipher.DECRYPT_MODE, key);
  //
  //		byte[] encryptByArrayData= Base64.getDecoder().decode(encryptData);
  //		byte[] decryptedBytes = cipher.doFinal(encryptByArrayData);
  //
  //		return new String (decryptedBytes , StandardCharsets.UTF_8);
  //
  //	}

  //	public static void main(String[] args) throws Exception {
  //		SecretKey key = geneateAESKey();
  //
  //		String plainText="Test@123";
  //
  //		String encryptedText = encrypt(plainText,key);
  //		System.out.println("Encrypted "+encryptedText);
  //
  //		String decryptedText= decrypt (encryptedText, key);
  //		System.out.println("Decrpted "+ decryptedText);
  //
  //		System.out.println("Key "+ key );
  //	}

}
