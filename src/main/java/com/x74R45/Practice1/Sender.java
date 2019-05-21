package com.x74R45.Practice1;

import java.nio.ByteBuffer;

public class Sender {
	private static long bPktId = 0;
	
	public static byte[] send(byte[] msg, int type, int userId)
	{
		byte[] res = new byte[26+msg.length];
		res[0] = 0x13;
		
		res[1] = 0x00;
		
		byte[] id = ByteBuffer.allocate(8).putLong(bPktId).array();
		for (int i = 0; i < 8; i++)
			res[i+2] = id[i];
		bPktId++;
		
		byte[] wLen = ByteBuffer.allocate(4).putInt(msg.length+8).array();
		for (int i = 0; i < 4; i++)
			res[i+10] = wLen[i];
		
		byte[] wCrc16 = ByteBuffer.allocate(2).putShort(CRC16.crc16(res, 0, 14)).array();
		for (int i = 0; i < 2; i++)
			res[i+14] = wCrc16[i];
		
		byte[] bMsg = new byte[msg.length+8];
		byte[] cType = ByteBuffer.allocate(4).putInt(type).array();
		for (int i = 0; i < 4; i++)
			bMsg[i] = cType[i];
		
		byte[] bUserId = ByteBuffer.allocate(4).putInt(userId).array();
		for (int i = 0; i < 4; i++)
			bMsg[i+4] = bUserId[i];
		
		for (int i = 0; i < msg.length; i++)
			bMsg[i+8] = msg[i];
		
		for (int i = 0; i < bMsg.length; i++)
			res[i+16] = bMsg[i];
		
		wCrc16 = ByteBuffer.allocate(2).putShort(CRC16.crc16(res, 16, 16+bMsg.length)).array();
		for (int i = 0; i < 2; i++)
			res[i+16+bMsg.length] = wCrc16[i];
		
		return res;
	}
}