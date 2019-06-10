package com.x74R45.protocol;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class Sender {
	
	public static void send(byte[] msg) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		System.out.println("Message sent: " + Arrays.toString(msg));
		Receiver.receive(msg);
	}
}