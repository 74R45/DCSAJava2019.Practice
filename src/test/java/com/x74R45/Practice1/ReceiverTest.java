package com.x74R45.Practice1;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.junit.BeforeClass;
import org.junit.Test;

public class ReceiverTest {

	private static IvParameterSpec ivspec;
	private static SecretKey key;
	
	@BeforeClass
	public static void setup() {
		byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        ivspec = new IvParameterSpec(iv);
        KeyGenerator keyGen = null;
		try {
			keyGen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			fail("KeyGenerator threw NoSuchAlgorithmException.");
		}
		key = keyGen.generateKey();
	}
	
	@Test
	public void testFirstByte() {
		try {
			byte[] input = Sender.send("hi".getBytes(), 0, 0, key, ivspec);
			input[0] = 0x14;
			assertFalse(Receiver.validate(input));
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
	
	@Test
	public void testFirstCRC16False() {
		try {
			byte[] input = Sender.send("hi".getBytes(), 0, 0, key, ivspec);
			input[14]++;
			assertFalse(Receiver.validate(input));
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
	
	@Test
	public void testSecondCRC16False() {
		try {
			byte[] input = Sender.send("hi".getBytes(), 0, 0, key, ivspec);
			byte[] wLenArr = new byte[4];
			for (int i = 0; i < 4; i++)
				wLenArr[i] = input[i+10];
			int wLen = ByteBuffer.wrap(wLenArr).getInt();
			input[16+wLen]++;
			assertFalse(Receiver.validate(input));
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
	
	@Test
	public void testTrue() {
		try {
			byte[] input = Sender.send("hi".getBytes(), 0, 0, key, ivspec);
			assertTrue(Receiver.validate(input));
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