package com.x74R45.protocol;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

public class ByteArrayCipher {
	
	public static byte[] encipher(byte[] bytes, Key key, IvParameterSpec ivspec) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, ivspec);
		return cipher.doFinal(bytes);
	}
	
	public static byte[] decipher(byte[] bytes, Key key, IvParameterSpec ivspec) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, ivspec);
		return cipher.doFinal(bytes);
	}
}