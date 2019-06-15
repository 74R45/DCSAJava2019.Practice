package com.x74R45.protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class StoreClientUDP implements Runnable {
	public Message message;
	DatagramSocket socket;
	InetAddress address;
	DatagramPacket packet;
	
	public static final int MAX_RETRIES = 4;
	
	public static void main(String args[]) throws Exception {
		new StoreClientUDP(1, Generator.commandAddItemToGroup("rice", "food"));
		Thread.sleep(1000);
		for (int i = 0; i < 10; i++)
			new StoreClientUDP(1, Generator.commandAdd("rice", 15));        	
		Thread.sleep(1000);
		new StoreClientUDP(1, Generator.commandCount("rice"));
	}
	
	public StoreClientUDP(int numThreads, Message message) {
		this.message = message;
		for (int i = 0; i < numThreads; i++)
			new Thread(this).start();
	}
	
	public void run() {
		
		try {
		
			for(int i = 0; i < MAX_RETRIES; i++) {

				socket = new DatagramSocket();
		    	// send request
		    	try {
					SecretKey key = KeyGenerator.getInstance("AES").generateKey();
					byte[] buf = Encryptor.encrypt(message, key);
					KeyStorage.addKey(key, socket.getLocalPort());
					
					address = InetAddress.getByName(null);
					packet = new DatagramPacket(buf, buf.length, address, 4445);
					socket.send(packet);
					System.out.println("Sent: " + message);
					
					// get response
					packet = new DatagramPacket(buf, buf.length);
					socket.receive(packet);

					if(Validator.validate(packet.getData())) {
						Message mes = Decryptor.decrypt(packet.getData(), key);
						if(mes.getType() == 1010) {
							continue;
						} else {
							System.out.println("Received: " + mes);
							if (message.getType() == 0) {
								System.out.println("Response: " + ByteBuffer.wrap(mes.getMessage()).getInt());
							}
							socket.close();
							return;
						}
					}
				} catch (InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException
						| BadPaddingException | InvalidAlgorithmParameterException | NoSuchPaddingException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Request failed after " + MAX_RETRIES + " retries");
       		socket.close();
       		
		} catch(IOException e) {};
	}
}