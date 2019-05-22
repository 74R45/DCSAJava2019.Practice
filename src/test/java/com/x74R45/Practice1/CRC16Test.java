package com.x74R45.Practice1;

import static org.junit.Assert.*;

import org.junit.Test;

public class CRC16Test {

	private String INPUT = "12345lalala";
	
	@Test
	public void testFull() {
		short expected = 0x1e69;
		assertEquals(expected, CRC16.crc16(INPUT.getBytes()));
	}
	
	@Test
	public void testInner() {
		short expected = 0x1e69;
		byte[] INPUTbytes = INPUT.getBytes();
		byte[] input = new byte[INPUTbytes.length+5];
		for (int i = 0; i < INPUTbytes.length; i++)
			input[i+2] = INPUTbytes[i];
		assertEquals(expected, CRC16.crc16(input, 2, 2+INPUTbytes.length));
	}
}