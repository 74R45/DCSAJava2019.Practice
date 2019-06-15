package com.x74R45.protocol;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class StoreServerTCP implements Runnable {

	public static final int PORT = 7474;
	
	public static void main(String args[]) {
		new StoreServerTCP();
	}
	
	public StoreServerTCP() {
		new Thread(this).start();
	}
	
	public void run() {
		try {
			KeyStorage.clearKeys();
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
			ServerSocket ss = ssc.socket();
			InetSocketAddress isa = new InetSocketAddress(PORT);
			ss.bind(isa);
			
			Selector selector = Selector.open();
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			
			while (true) {
				if (selector.select() == 0)
					continue;
				
				Set keys = selector.selectedKeys();
				Iterator it = keys.iterator();
				
				while (it.hasNext()) {
					SelectionKey key = (SelectionKey) it.next();
					if ((key.readyOps() & SelectionKey.OP_ACCEPT) ==
							SelectionKey.OP_ACCEPT) {
						Socket s = ss.accept();
						SocketChannel sc = s.getChannel();
						sc.configureBlocking(false);
						sc.register(selector, SelectionKey.OP_READ);
					} else if ((key.readyOps() & SelectionKey.OP_READ) ==
				            SelectionKey.OP_READ) {
						SocketChannel sc = null;
						
						try {
							sc = (SocketChannel) key.channel();
							if (!processInput(sc)) {
								key.cancel();
								sc.socket().close();
							}
						} catch (IOException e) {
							key.cancel();
							sc.close();
							e.printStackTrace();
						}
					}
		        }
				
				keys.clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		KeyStorage.clearKeys();
	}
	
	private boolean processInput(SocketChannel sc) throws IOException {
		ByteBuffer bytes = ByteBuffer.allocate(16384);
		sc.read(bytes);
		bytes.flip();
		
		if (bytes.limit() == 0)
			return false;
		
		byte[] packet = new byte[bytes.limit()];
		bytes.get(packet, 0, packet.length);
		
		try {
			if (!Validator.validate(packet))
				return false;
			
			Key key;
			
			if ((key = KeyStorage.getKey(sc.socket().getPort())) == null) {
				System.out.println("Could not find a key for a packet.");
				return false;
			}
			
			Message mes = Decryptor.decrypt(packet, key);
			Message replyMes = Processor.process(mes);
			
			byte[] reply = Encryptor.encrypt(replyMes, key);
			
			sc.write(ByteBuffer.wrap(reply));
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
				| InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		return true;
	}
}