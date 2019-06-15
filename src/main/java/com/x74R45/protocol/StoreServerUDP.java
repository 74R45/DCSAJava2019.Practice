package com.x74R45.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class StoreServerUDP {
    protected BufferedReader in = null;
	
	public static void main(String[] args) {
		try {
			KeyStorage.clearKeys();
			DatagramSocket socket = new DatagramSocket(4445);

            boolean shouldReceiveMessages = true;
            while(shouldReceiveMessages) {
            	
            	// receive request
            	byte[] buf = new byte[256];
            	
            	DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                
                ArrayList<Vector<Object>> keys = KeyStorage.getKeys();
                Key key;
                
                if((key = KeyStorage.getKey(packet.getPort())) != null 
                		|| Validator.validate(packet.getData())) {
                	
        			try {
						Message mes = Decryptor.decrypt(packet.getData(), key);
						Message replyMes = Processor.process(mes);
						
						// figure out response
						byte[] reply = Encryptor.encrypt(replyMes, key);
            
						// send the response to the client at "address" and "port"
						InetAddress address = packet.getAddress();
						int port = packet.getPort();
						packet = new DatagramPacket(reply, reply.length, address, port);
						socket.send(packet);
					} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
							| InvalidAlgorithmParameterException | NoSuchAlgorithmException
							| NoSuchPaddingException e) {
						e.printStackTrace();
					}
                	
                } else {
                	System.out.println("data corrupted");
                	
                	// figure out response
                	try {
						String mes = "data corrupted";
						buf = Encryptor.encrypt(new Message((byte)0, 1010, 0, mes.getBytes()), key);
						
						// send the response to the client at "address" and "port"
						InetAddress address = packet.getAddress();
						int port = packet.getPort();
						packet = new DatagramPacket(buf, buf.length, address, port);
						socket.send(packet);
					} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
							| InvalidAlgorithmParameterException | NoSuchAlgorithmException
							| NoSuchPaddingException e) {
						e.printStackTrace();
					}
                }
            }
            
            socket.close();
            
            System.out.println("Server closed");
            
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
