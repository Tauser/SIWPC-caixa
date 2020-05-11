package br.gov.caixa.faleconosco.portal.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class CriptpUtil {

	static String key = "FALECONOSCO1KEYS"; // 128 bit key
	
	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		
		String criptografar = criptografar("minhas senha");
		
		System.out.println(criptografar);
		
		System.out.println(decriptografar(criptografar));
	}
	
	
	public static String criptografar(String text) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		// Create key and cipher
         Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
         Cipher cipher = Cipher.getInstance("AES");
         // encrypt the text
         cipher.init(Cipher.ENCRYPT_MODE, aesKey);
         byte[] encrypted = Base64.encodeBase64URLSafe((cipher.doFinal(text.getBytes())));
		
         String textEncripted = new String(encrypted);
		return textEncripted;
	}

	
	public static String decriptografar(String textEncripted)
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		
		Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, aesKey);
         String decrypted = new String(cipher.doFinal(Base64.decodeBase64((textEncripted.getBytes()))));
         return decrypted;
	}
	
	
}
