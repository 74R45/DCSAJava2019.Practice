package com.x74R45.rest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TokenStorage {

	public static final String TOKEN_PATH = "tokens.txt";
	
	public static void saveToken(String token) {
		if (validateToken(token))
			return;
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(TOKEN_PATH, true));
			writer.write(token);
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean validateToken(String token) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(TOKEN_PATH));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.equals(token)) {
					reader.close();
					return true;
				}
			}
			reader.close();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void clearTokens() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(TOKEN_PATH, false));
			writer.write("");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}