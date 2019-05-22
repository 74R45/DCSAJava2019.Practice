package com.x74R45.Practice1;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class App {
	public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("Please provide an input!");
            System.exit(0);
        }
        
        byte[] message = args[0].getBytes();
        
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        
        KeyGenerator keyGen;
		try {
			keyGen = KeyGenerator.getInstance("AES");
			SecretKey key = keyGen.generateKey();
			byte[] pkg = Sender.send(message, 1, 1, key, ivspec);
			System.out.println("Package: " + Arrays.toString(pkg));
			System.out.println("Validated? " + Receiver.validate(pkg));
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
    }
}