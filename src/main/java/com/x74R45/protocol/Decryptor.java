package com.x74R45.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

public class Decryptor {
	
	public static Message decrypt(byte[] message, Key key) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
		byte src = message[1];
		byte[] pktIdArr = new byte[8];
		for (int i = 0; i < 8; i++) {
			pktIdArr[i] = message[i+2];
		}
		long pktId = ByteBuffer.wrap(pktIdArr).getLong();
		
		byte[] lenArr = new byte[4];
		for (int i = 0; i < 4; i++) {
			lenArr[i] = message[i+10];
		}
		int len = ByteBuffer.wrap(lenArr).getInt();
		
		byte[] encrypted = new byte[len];
		for (int i = 0; i < len; i++) {
			encrypted[i] = message[i+16];
		}
		
		byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		IvParameterSpec ivspec = new IvParameterSpec(iv);
		
		byte[] decrypted = ByteArrayCipher.decipher(encrypted, key, ivspec);
		
		byte[] typeArr = new byte[4];
		for (int i = 0; i < 4; i++) {
			typeArr[i] = decrypted[i];
		}
		int type = ByteBuffer.wrap(typeArr).getInt();
		
		byte[] userIdArr = new byte[4];
		for (int i = 0; i < 4; i++) {
			userIdArr[i] = decrypted[i+4];
		}
		int userId = ByteBuffer.wrap(userIdArr).getInt();
		
		byte[] msg = new byte[decrypted.length-8];
		for (int i = 0; i < msg.length; i++) {
			msg[i] = decrypted[i+8];
		}
		return new Message(src, type, userId, msg, pktId);
	}
}