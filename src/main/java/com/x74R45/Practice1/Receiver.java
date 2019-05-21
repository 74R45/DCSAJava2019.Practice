package com.x74R45.Practice1;

import java.nio.ByteBuffer;

public class Receiver {
	
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
}