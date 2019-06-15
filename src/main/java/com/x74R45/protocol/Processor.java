package com.x74R45.protocol;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class Processor {

	public static Message process(Message mes) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		
		// Processing the message...
		MessageChecker.addReceived(mes);
		switch (mes.getType()) {
		case 0:
			try {
				int res = Storage.countItem(new String(mes.getMessage(), "UTF-8"));
				return new Message((byte) 0, 2020, 0, ByteBuffer.allocate(4).putInt(res).array());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			break;
		case 1:
			try {
				byte[] amountArr = new byte[4];
				for (int i = 0; i < 4; i++)
					amountArr[i] = mes.getMessage()[i];
				int amount = ByteBuffer.wrap(amountArr).getInt();
				
				byte[] nameArr = new byte[mes.getMessage().length-4];
				for (int i = 0; i < nameArr.length; i++)
					nameArr[i] = mes.getMessage()[i+4];		
				String name = new String(nameArr, "UTF-8");
				
				Storage.addItems(name, amount);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			break;
		case 2:
			try {
				byte[] amountArr = new byte[4];
				for (int i = 0; i < 4; i++)
					amountArr[i] = mes.getMessage()[i];
				int amount = ByteBuffer.wrap(amountArr).getInt();
				
				byte[] nameArr = new byte[mes.getMessage().length-4];
				for (int i = 0; i < nameArr.length; i++)
					nameArr[i] = mes.getMessage()[i+4];		
				String name = new String(nameArr, "UTF-8");
				
				Storage.subtractItems(name, amount);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			break;
		case 3:
			try {
				Storage.addGroup(new String(mes.getMessage(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			break;
		case 4:
			try {
				byte[] lenArr = new byte[4];
				for (int i = 0; i < 4; i++)
					lenArr[i] = mes.getMessage()[i];
				int len = ByteBuffer.wrap(lenArr).getInt();
				
				byte[] itemNameArr = new byte[len];
				for (int i = 0; i < itemNameArr.length; i++)
					itemNameArr[i] = mes.getMessage()[i+4];		
				String itemName = new String(itemNameArr, "UTF-8");
				
				byte[] groupNameArr = new byte[mes.getMessage().length-len-4];
				for (int i = 0; i < groupNameArr.length; i++)
					groupNameArr[i] = mes.getMessage()[i+len+4];
				String groupName = new String(groupNameArr, "UTF-8");
				
				Storage.addItemToGroup(itemName, groupName);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			break;
		case 5:
			try {
				byte[] priceArr = new byte[4];
				for (int i = 0; i < 4; i++)
					priceArr[i] = mes.getMessage()[i];
				int price = ByteBuffer.wrap(priceArr).getInt();
				
				byte[] nameArr = new byte[mes.getMessage().length-4];
				for (int i = 0; i < nameArr.length; i++)
					nameArr[i] = mes.getMessage()[i+4];		
				String name = new String(nameArr, "UTF-8");
				
				Storage.setPrice(name, price);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			break;
		}
		
		// Message of type 2020 means that it is a reply from server with message "Ok"
		return new Message((byte) 0, 2020, 0, "Ok".getBytes());
	}
}