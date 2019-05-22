package com.x74R45.Practice1;

import static org.junit.Assert.*;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.junit.Test;

public class EncryptorTest {
	
	private String INPUT = "12345";

	@Test
	public void test() {
		try {
			byte[] iv = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
		    IvParameterSpec ivspec = new IvParameterSpec(iv);
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			SecretKey key = keyGen.generateKey();
			assertTrue(Arrays.equals(INPUT.getBytes(), Encryptor.decrypt(Encryptor.encrypt(INPUT.getBytes(), key, ivspec), key, ivspec)));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			fail("KeyGenerator threw NoSuchAlgorithmException.");
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			fail("Encryptor threw InvalidKeyException.");
		} catch (BadPaddingException e) {
			e.printStackTrace();
			fail("Encryptor threw BadPaddingException.");
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			fail("Encryptor threw IllegalBlockSizeException.");
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
			fail("Encryptor threw InvalidAlgorithmParameterException.");
		}
	}
}