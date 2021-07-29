package com.porwau.reportgenerator.common.utilities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Utkarsh Porwal<br>
 *Contains utilities for Encrypting and decrypting data using a symmetric key.
 */
public class EncryptDecrypt {
    private static final byte[] SALT = new String("12345678").getBytes();
    private static final int ITERATION_COUNT = 40000;
    private static final int KEY_LENGTH = 128;
    
	  /**
	 * @param Symmetric key
	 * @return SecretKeySpec
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static SecretKeySpec createSecretKey(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
	        PBEKeySpec keySpec = new PBEKeySpec(password, SALT, ITERATION_COUNT, KEY_LENGTH);
	        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
	        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
	    }

	    /**
	     * @param dataToEncrypt
	     * @param key
	     * @return Encrypted string
	     * @throws GeneralSecurityException
	     * @throws UnsupportedEncodingException
	     */
	    public static String encrypt(String dataToEncrypt, SecretKeySpec key) throws GeneralSecurityException, UnsupportedEncodingException {
	        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        pbeCipher.init(Cipher.ENCRYPT_MODE, key);
	        AlgorithmParameters parameters = pbeCipher.getParameters();
	        IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
	        byte[] cryptoText = pbeCipher.doFinal(dataToEncrypt.getBytes("UTF-8"));
	        byte[] iv = ivParameterSpec.getIV();
	        return base64Encode(iv) + ":" + base64Encode(cryptoText);
	    }

	    private static String base64Encode(byte[] bytes) {
	        return Base64.getEncoder().encodeToString(bytes);
	    }

	    /**
	     * @param dataToDecrypt
	     * @param key same as used for encryption
	     * @return decrypted string
	     * @throws GeneralSecurityException
	     * @throws IOException
	     */
	    public static String decrypt(String dataToDecrypt, SecretKeySpec key) throws GeneralSecurityException, IOException {
	        String iv = dataToDecrypt.split(":")[0];
	        String property = dataToDecrypt.split(":")[1];
	        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
	        return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
	    }

	    private static byte[] base64Decode(String property) throws IOException {
	        return Base64.getDecoder().decode(property);
	    }
}
