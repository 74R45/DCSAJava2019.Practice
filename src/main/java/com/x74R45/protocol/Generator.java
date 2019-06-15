package com.x74R45.protocol;

import java.nio.ByteBuffer;
import java.util.Random;

public class Generator {

	public static Message generateRandom() {
		Random rand = new Random();
		int len = rand.nextInt(100);
		byte[] msg = new byte[len];
		rand.nextBytes(msg);
		return new Message((byte) rand.nextInt(256), rand.nextInt(994)+6, rand.nextInt(1000), msg);
	}
	
	public static Message commandCount(String itemName) {
		return new Message((byte) 0, 0, 0, itemName.getBytes());
	}
	
	public static Message commandAdd(String itemName, int amount) {
		byte[] message = new byte[itemName.length()+4];
		byte[] amountArr = ByteBuffer.allocate(4).putInt(amount).array();
		for (int i = 0; i < 4; i++)
			message[i] = amountArr[i];
		byte[] itemNameArr = itemName.getBytes();
		for (int i = 4; i < message.length; i++)
			message[i] = itemNameArr[i-4];
		
		return new Message((byte) 0, 1, 0, message);
	}
	
	public static Message commandSubtract(String itemName, int amount) {
		byte[] message = new byte[itemName.length()+4];
		byte[] amountArr = ByteBuffer.allocate(4).putInt(amount).array();
		for (int i = 0; i < 4; i++)
			message[i] = amountArr[i];
		byte[] itemNameArr = itemName.getBytes();
		for (int i = 4; i < message.length; i++)
			message[i] = itemNameArr[i-4];
		
		return new Message((byte) 0, 2, 0, message);
	}
	
	public static Message commandAddGroup(String groupName) {
		return new Message((byte) 0, 3, 0, groupName.getBytes());
	}
	
	public static Message commandAddItemToGroup(String itemName, String groupName) {
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
		
		return new Message((byte) 0, 4, 0, message);
	}
	
	public static Message commandSetPrice(String itemName, int price) {
		byte[] message = new byte[itemName.length()+4];
		byte[] amountArr = ByteBuffer.allocate(4).putInt(price).array();
		for (int i = 0; i < 4; i++)
			message[i] = amountArr[i];
		byte[] itemNameArr = itemName.getBytes();
		for (int i = 4; i < message.length; i++)
			message[i] = itemNameArr[i-4];
		
		return new Message((byte) 0, 5, 0, message);
	}
}