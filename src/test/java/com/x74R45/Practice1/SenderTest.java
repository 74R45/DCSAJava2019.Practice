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

public class SenderTest {

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
			byte[] output = Sender.send("hi".getBytes(), 0, 0, key, ivspec);
			assertEquals(0x13, output[0]);
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
	public void testIds() {
		try {
			byte[] output1 = Sender.send("hi".getBytes(), 0, 0, key, ivspec);
			byte[] output2 = Sender.send("hi".getBytes(), 0, 0, key, ivspec);
			byte[] idArr = new byte[8];
			for (int i = 0; i < 8; i++)
				idArr[i] = output1[i+2];
			long id1 = ByteBuffer.wrap(idArr).getLong();
			for (int i = 0; i < 8; i++)
				idArr[i] = output2[i+2];
			long id2 = ByteBuffer.wrap(idArr).getLong();
			assertEquals(1, id2-id1);
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
	public void testLen() {
		try {
			byte[] output = Sender.send("hi".getBytes(), 0, 0, key, ivspec);
			byte[] lenArr = new byte[4];
			for (int i = 0; i < 4; i++)
				lenArr[i] = output[i+10];
			int len = ByteBuffer.wrap(lenArr).getInt();
			assertEquals(len, output.length-18);
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
	public void testFirstCRC16() {
		try {
			byte[] output = Sender.send("hi".getBytes(), 0, 0, key, ivspec);
			byte[] crc16Arr = new byte[2];
			for (int i = 0; i < 2; i++)
				crc16Arr[i] = output[i+14];
			short crc16 = ByteBuffer.wrap(crc16Arr).getShort();
			short expected = CRC16.crc16(output, 0, 14);
			assertEquals(expected, crc16);
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
	public void testSecondCRC16() {
		try {
			byte[] output = Sender.send("hi".getBytes(), 0, 0, key, ivspec);
			byte[] wLenArr = new byte[4];
			for (int i = 0; i < 4; i++)
				wLenArr[i] = output[i+10];
			int wLen = ByteBuffer.wrap(wLenArr).getInt();
			
			byte[] crc16Arr = new byte[2];
			for (int i = 0; i < 2; i++)
				crc16Arr[i] = output[i+16+wLen];
			short crc16 = ByteBuffer.wrap(crc16Arr).getShort();
			short expected = CRC16.crc16(output, 16, 16+wLen);
			assertEquals(expected, crc16);
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