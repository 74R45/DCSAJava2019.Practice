package com.x74R45.Practice1;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;

public class Sender {
	private static long bPktId = 0;
	
	public static byte[] send(byte[] msg, int type, int userId, Key key, IvParameterSpec ivspec) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		
		// Message
		byte[] bMsg = new byte[msg.length+8];
		byte[] cType = ByteBuffer.allocate(4).putInt(type).array();
		for (int i = 0; i < 4; i++)
			bMsg[i] = cType[i];
		
		byte[] bUserId = ByteBuffer.allocate(4).putInt(userId).array();
		for (int i = 0; i < 4; i++)
			bMsg[i+4] = bUserId[i];
		
		for (int i = 0; i < msg.length; i++)
			bMsg[i+8] = msg[i];
		
		byte[] cipherMsg = Encryptor.encrypt(bMsg, key, ivspec);
		
		
		byte[] res = new byte[18+cipherMsg.length];
		// bMagic
		res[0] = 0x13;
		
		// bSrc
		res[1] = 0x00;
		
		// bPktId
		byte[] id = ByteBuffer.allocate(8).putLong(bPktId).array();
		for (int i = 0; i < 8; i++)
			res[i+2] = id[i];
		bPktId++;
		
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
		
		return res;
	}
}