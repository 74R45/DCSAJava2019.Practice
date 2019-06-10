package com.x74R45.protocol;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class App {

	public static void main(String[] args) {
		byte[] iv = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
		IvParameterSpec ivspec = new IvParameterSpec(iv);
		KeyGenerator keyGen;
		try {
			keyGen = KeyGenerator.getInstance("AES");
			SecretKey key = keyGen.generateKey();
			Generator.generateRandom(key, ivspec);
			Generator.commandAddItemToGroup("buckwheat", "food", key, ivspec);
			Generator.commandAdd("buckwheat", 40, key, ivspec);
			Generator.commandSetPrice("buckwheat", 1000, key, ivspec);
			Generator.commandCount("buckwheat", key, ivspec);
			Generator.commandAddGroup("stuff", key, ivspec);
			Generator.commandSubtract("buckwheat", 4, key, ivspec);
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Error: KeyGenerator couldn't find an algorithm named \"AES\".");
		} catch (InvalidKeyException e) {
			System.err.println("Error: Sender failed to use the key.");
		} catch (IllegalBlockSizeException e) {
			System.err.println("Error: Sender threw an IllegalBlockSizeException.");
		} catch (BadPaddingException e) {
			System.err.println("Error: Sender threw a BadPaddingException.");
		} catch (InvalidAlgorithmParameterException e) {
			System.err.println("Error: Sender threw an InvalidAlgorithmParameterException.");
		}
		System.out.println("MessageChecker: " + MessageChecker.check());
    }
}