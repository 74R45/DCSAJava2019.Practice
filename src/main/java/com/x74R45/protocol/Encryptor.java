package com.x74R45.protocol;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

public class Encryptor {

	public static void encrypt(Message message, Key key, IvParameterSpec ivspec) {
		// Message
		byte[] bMsg = new byte[message.getMessage().length+8];
		byte[] cType = ByteBuffer.allocate(4).putInt(message.getType()).array();
		for (int i = 0; i < 4; i++)
			bMsg[i] = cType[i];
		
		byte[] bUserId = ByteBuffer.allocate(4).putInt(message.getUserId()).array();
		for (int i = 0; i < 4; i++)
			bMsg[i+4] = bUserId[i];
		
		for (int i = 0; i < message.getMessage().length; i++)
			bMsg[i+8] = message.getMessage()[i];
		
		try {
			byte[] cipherMsg = ByteArrayCipher.encipher(bMsg, key, ivspec);
			
			byte[] res = new byte[18+cipherMsg.length];
			// bMagic
			res[0] = 0x13;
			
			// bSrc
			res[1] = message.getSrc();
			
			// bPktId
			byte[] id = ByteBuffer.allocate(8).putLong(message.getPktId()).array();
			for (int i = 0; i < 8; i++)
				res[i+2] = id[i];
			
			// wLen
			byte[] wLen = ByteBuffer.allocate(4).putInt(cipherMsg.length).array();
			for (int i = 0; i < 4; i++)
				res[i+10] = wLen[i];
			
			// wCrc16 (bytes 0-13)
			byte[] wCrc16 = ByteBuffer.allocate(2).putShort(CRC16.crc16(res, 0, 14)).array();
			for (int i = 0; i < 2; i++)
				res[i+14] = wCrc16[i];
			
			// Adding encrypted message
			for (int i = 0; i < cipherMsg.length; i++)
				res[i+16] = cipherMsg[i];
			
			// wCrc16
			wCrc16 = ByteBuffer.allocate(2).putShort(CRC16.crc16(res, 16, 16+cipherMsg.length)).array();
			for (int i = 0; i < 2; i++)
				res[i+16+cipherMsg.length] = wCrc16[i];
			
			MessageChecker.addSent(message);
			Receiver.addKey(key, ivspec, message.getPktId());
			Sender.send(res);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
				| InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}
}