package com.x74R45.Practice1;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

public class Encryptor {
	
	private static Cipher cipher;
	
	public static byte[] encrypt(byte[] bytes, Key key, IvParameterSpec ivspec) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		
		cipher.init(Cipher.ENCRYPT_MODE, key, ivspec);
		return cipher.doFinal(bytes);
	}
	
	public static byte[] decrypt(byte[] bytes, Key key, IvParameterSpec ivspec) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		
		cipher.init(Cipher.DECRYPT_MODE, key, ivspec);
		return cipher.doFinal(bytes);
	}
}