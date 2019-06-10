package com.x74R45.protocol;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;

public class Receiver {
	
	private static ArrayList<Vector<Object>> keys = new ArrayList<>();
	
	public static boolean validate(byte[] bytes) {
		if (bytes[0] != 0x13) return false;
		
		byte[] wLenArr = new byte[4];
		for (int i = 0; i < 4; i++)
			wLenArr[i] = bytes[i+10];
		int wLen = ByteBuffer.wrap(wLenArr).getInt();
		
		byte[] wCrc16Arr = new byte[2];
		for (int i = 0; i < 2; i++)
			wCrc16Arr[i] = bytes[i+14];
		short wCrc16 = ByteBuffer.wrap(wCrc16Arr).getShort();
		if (CRC16.crc16(bytes, 0, 14) != wCrc16) return false;
		
		for (int i = 0; i < 2; i++)
			wCrc16Arr[i] = bytes[i+16+wLen];
		wCrc16 = ByteBuffer.wrap(wCrc16Arr).getShort();
		if (CRC16.crc16(bytes, 16, 16+wLen) != wCrc16) return false;
		
		return true;
	}
	
	public static void receive(byte[] msg) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		if (!validate(msg))
			return;
		
		byte[] pktIdArr = new byte[8];
		for (int i = 0; i < 8; i++)
			pktIdArr[i] = msg[i+2];
		long pktId = ByteBuffer.wrap(pktIdArr).getLong();
		
		synchronized (keys) {
			int i;
			for (i = 0; i < keys.size(); i++) {
				if ((long)keys.get(i).get(2) == pktId) {
					Decryptor.decrypt(msg, (Key)keys.get(i).get(0), (IvParameterSpec)keys.get(i).get(1));
					break;
				}
			}
			keys.remove(i);
		}
	}
	
	public static void addKey(Key key, IvParameterSpec ivspec, long id) {
		Vector<Object> v = new Vector<>();
		v.add(key); v.add(ivspec); v.add(id);
		synchronized (keys) {
			if (!keys.contains(v))
				keys.add(v);
		}
	}
}