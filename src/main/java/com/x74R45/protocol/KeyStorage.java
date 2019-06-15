package com.x74R45.protocol;

import static org.apache.commons.codec.binary.Hex.decodeHex;
import static org.apache.commons.codec.binary.Hex.encodeHex;
import org.apache.commons.codec.DecoderException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.Key;
import java.util.ArrayList;
import java.util.Vector;

import javax.crypto.spec.SecretKeySpec;

public class KeyStorage {
	
	private static final String PATH = "keys.txt";
	
	public static Key getKey(int port) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(PATH));
		String line;
		while ((line = reader.readLine()) != null) {
			try {
				byte[] encoded = decodeHex(line);
				Key key = new SecretKeySpec(encoded, "AES");
				
				line = reader.readLine();
				if (Integer.parseInt(line) == port) {
					reader.close();
					return key;
				}
			} catch (DecoderException e) {
				e.printStackTrace();
			}
		}
		reader.close();
		return null;
	}
	
	public static ArrayList<Vector<Object>> getKeys() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(PATH));
		String line;
		ArrayList<Vector<Object>> res = new ArrayList<>();
		while ((line = reader.readLine()) != null) {
			try {
				byte[] encoded = decodeHex(line);
				Key key = new SecretKeySpec(encoded, "AES");
				
				line = reader.readLine();
				
				Vector<Object> v = new Vector<Object>();
				v.add(key); v.add(Integer.parseInt(line));
				res.add(v);
			} catch (DecoderException e) {
				e.printStackTrace();
			}
		}
		reader.close();
		return res;
	}
	
	public static void addKey(Key key, int port) {
		char[] hex = encodeHex(key.getEncoded());
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(PATH, true));
			writer.write(hex);
			writer.newLine();
			writer.write(Integer.toString(port));
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void clearKeys() {
		try {
			FileWriter fw = new FileWriter(PATH, false);
			fw.write("");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}