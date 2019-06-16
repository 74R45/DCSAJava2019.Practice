package com.x74R45.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class StoreClientTCP implements Runnable {

	private Message mes;
	
	public static void main(String args[]) throws Exception {
        new StoreClientTCP(10, Generator.commandAdd("rice", 15));
        Thread.sleep(1500);
        new StoreClientTCP(1, Generator.commandCount("rice"));
    }
	
	public StoreClientTCP(int numThreads, Message mes) {
		this.mes = mes;
        for (int i = 0; i < numThreads; i++) {
            new Thread(this).start();
        }
    }
	
	public void run() {
		try {
			InetAddress addr = InetAddress.getByName(null);
			Socket s = new Socket();
			s.connect(new InetSocketAddress(addr, StoreServerTCP.PORT));
			
			InputStream in = s.getInputStream();
			OutputStream out = s.getOutputStream();
			
			SecretKey key = KeyGenerator.getInstance("AES").generateKey();
			KeyStorage.addKey(key, s.getLocalPort());
			byte[] packet = Encryptor.encrypt(mes, key);
			
			System.out.println("Sending message: " + mes);
			while (true) {
				try {
					out.write(packet);
					
					byte[] buf = new byte[16384];
					int len = in.read(buf);
					byte[] reply = new byte[len];
					for (int i = 0; i < len; i++)
						reply[i] = buf[i];
					
					if (Validator.validate(reply)) {
						Message replyMes = Decryptor.decrypt(reply, key);
						System.out.println("Received: " + replyMes);
						if (mes.getType() == 0) {
							System.out.println("Response: " + ByteBuffer.wrap(replyMes.getMessage()).getInt());
						}
					}
					break;
				} catch (IOException e) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
			
			s.close();
		} catch (IOException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}
}
