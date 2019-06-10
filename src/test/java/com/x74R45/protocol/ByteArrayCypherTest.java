package com.x74R45.protocol;

import static org.junit.Assert.*;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.junit.Test;

import com.x74R45.protocol.ByteArrayCipher;

public class ByteArrayCypherTest {
	
	private String INPUT = "12345";

	@Test
	public void test() {
		try {
			byte[] iv = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			SecretKey key = keyGen.generateKey();
			assertTrue(Arrays.equals(INPUT.getBytes(), ByteArrayCipher.decipher(ByteArrayCipher.encipher(INPUT.getBytes(), key, ivspec), key, ivspec)));
		} catch (InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException
				| InvalidAlgorithmParameterException | NoSuchPaddingException e) {
			e.printStackTrace();
			fail("An exception has been thrown.");
		}
	}
}