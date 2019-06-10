package com.x74R45.protocol;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;

public class Generator {

	public static void generateRandom(Key key, IvParameterSpec ivspec) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		Random rand = new Random();
		int len = rand.nextInt(100);
		byte[] msg = new byte[len];
		rand.nextBytes(msg);
		Encryptor.encrypt(new Message((byte) rand.nextInt(256), rand.nextInt(994)+6, rand.nextInt(1000), msg), key, ivspec);
	}
	
	public static void commandCount(String itemName, Key key, IvParameterSpec ivspec) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		Encryptor.encrypt(new Message((byte) 0, 0, 0, itemName.getBytes()), key, ivspec);
	}
	
	public static void commandAdd(String itemName, int amount, Key key, IvParameterSpec ivspec) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		byte[] message = new byte[itemName.length()+4];
		byte[] amountArr = ByteBuffer.allocate(4).putInt(amount).array();
		for (int i = 0; i < 4; i++)
			message[i] = amountArr[i];
		byte[] itemNameArr = itemName.getBytes();
		for (int i = 4; i < message.length; i++)
			message[i] = itemNameArr[i-4];
		
		Encryptor.encrypt(new Message((byte) 0, 1, 0, message), key, ivspec);
	}
	
	public static void commandSubtract(String itemName, int amount, Key key, IvParameterSpec ivspec) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		byte[] message = new byte[itemName.length()+4];
		byte[] amountArr = ByteBuffer.allocate(4).putInt(amount).array();
		for (int i = 0; i < 4; i++)
			message[i] = amountArr[i];
		byte[] itemNameArr = itemName.getBytes();
		for (int i = 4; i < message.length; i++)
			message[i] = itemNameArr[i-4];
		
		Encryptor.encrypt(new Message((byte) 0, 2, 0, message), key, ivspec);
	}
	
	public static void commandAddGroup(String groupName, Key key, IvParameterSpec ivspec) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		Encryptor.encrypt(new Message((byte) 0, 3, 0, groupName.getBytes()), key, ivspec);
	}
	
	public static void commandAddItemToGroup(String itemName, String groupName, Key key, IvParameterSpec ivspec) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		byte[] message = new byte[itemName.length()+groupName.length()+4];
		byte[] lenArr = ByteBuffer.allocate(4).putInt(itemName.length()).array();
		for (int i = 0; i < 4; i++)
			message[i] = lenArr[i];
		byte[] itemNameArr = itemName.getBytes();
		for (int i = 4; i < itemNameArr.length+4; i++)
			message[i] = itemNameArr[i-4];
		byte[] groupNameArr = groupName.getBytes();
		for (int i = itemNameArr.length+4; i < message.length; i++)
			message[i] = groupNameArr[i-itemNameArr.length-4];
		
		Encryptor.encrypt(new Message((byte) 0, 4, 0, message), key, ivspec);
	}
	
	public static void commandSetPrice(String itemName, int price, Key key, IvParameterSpec ivspec) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		byte[] message = new byte[itemName.length()+4];
		byte[] amountArr = ByteBuffer.allocate(4).putInt(price).array();
		for (int i = 0; i < 4; i++)
			message[i] = amountArr[i];
		byte[] itemNameArr = itemName.getBytes();
		for (int i = 4; i < message.length; i++)
			message[i] = itemNameArr[i-4];
		
		Encryptor.encrypt(new Message((byte) 0, 5, 0, message), key, ivspec);
	}
}