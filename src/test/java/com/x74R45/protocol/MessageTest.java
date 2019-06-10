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

public class MessageTest {
	
	private class Tester implements Runnable {
		private static final int MAX_THREADS = 100;
		private KeyGenerator keyGen;
		private IvParameterSpec ivspec;
		private int numberOfThreads;
		
		public Tester(KeyGenerator keyGen, IvParameterSpec ivspec, int numberOfThreads) {
			this.keyGen = keyGen;
			this.ivspec = ivspec;
			this.numberOfThreads = numberOfThreads;
		}
		
		public void run() {
			try {
				if (numberOfThreads > MAX_THREADS) 
					numberOfThreads = MAX_THREADS;
				if (numberOfThreads > 0) {
					Thread t = new Thread(new Tester(keyGen, ivspec, numberOfThreads-1));
					t.start();
					Generator.generateRandom(keyGen.generateKey(), ivspec);
					t.join();
				}
				else {
					Generator.generateRandom(keyGen.generateKey(), ivspec);
				}
			} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
					| InvalidAlgorithmParameterException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void test() {
		try {
			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			
			Thread t = new Thread(new Tester(keyGen, ivspec, 100));
			t.start();
			t.join();
			assertTrue(MessageChecker.check());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			fail("KeyGenerator threw NoSuchAlgorithmException.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}