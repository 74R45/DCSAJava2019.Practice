package com.x74R45.protocol;

import static org.junit.Assert.*;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;

import org.junit.Test;

public class CommandTest {

	private class Truck implements Runnable {

		private int numberOfThreads;
		private String item;
		private int amount;
		
		public Truck(String item, int amount, int numberOfThreads) {
			this.item = item;
			this.amount = amount;
			this.numberOfThreads = numberOfThreads;
		}
		
		@Override
		public void run() {
			try {
				byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
				IvParameterSpec ivspec = new IvParameterSpec(iv);
				KeyGenerator keyGen = KeyGenerator.getInstance("AES");
				if (numberOfThreads > 0) {
					Thread t = new Thread(new Truck(item, amount, numberOfThreads-1));
					t.start();
					Generator.commandAdd(item, amount, keyGen.generateKey(), ivspec);
					t.join();
				}
			} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
					| InvalidAlgorithmParameterException | InterruptedException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void test() {
		byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		IvParameterSpec ivspec = new IvParameterSpec(iv);
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			Generator.commandAddItemToGroup("buckwheat", "food", keyGen.generateKey(), ivspec);
			Generator.commandAdd("buckwheat", 40, keyGen.generateKey(), ivspec);
			Generator.commandSetPrice("buckwheat", 1000, keyGen.generateKey(), ivspec);
			Generator.commandCount("buckwheat", keyGen.generateKey(), ivspec);
			Generator.commandAddGroup("stuff", keyGen.generateKey(), ivspec);
			Generator.commandSubtract("buckwheat", 4, keyGen.generateKey(), ivspec);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
				| InvalidAlgorithmParameterException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		assertEquals(36, Storage.countItem("buckwheat"));
		assertEquals(1000, Storage.getPrice("buckwheat"));
		assertTrue(Storage.getGroup("food").getItems().get(0).equals("buckwheat"));
		assertTrue(Storage.getGroup("food").getItems().size() == 1);
		assertTrue(Storage.getGroup("stuff").getItems().size() == 0);
		Storage.clear();
	}
	
	@Test
	public void threadsTest() {
		Storage.addItemToGroup("buckwheat", "food");
		Thread t = new Thread(new Truck("buckwheat", 10, 50));
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(500, Storage.countItem("buckwheat"));
		Storage.clear();
	}
}