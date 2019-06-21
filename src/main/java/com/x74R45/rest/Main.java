package com.x74R45.rest;

import static org.apache.commons.codec.binary.Hex.encodeHex;

import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.x74R45.protocol.Item;

public class Main {

	private static final String LOGIN = "root";
	private static final String PASSWORD = "password";
	private static String token;
	private static int id;
	
	public static void main(String[] args) throws Exception {
		token = login();
		id = putItem();
		getItem();
		postItem();
		getItem();
		deleteItem();
		getItem();
	}
	
	private static String login() throws Exception {
		String login = LOGIN, pass = encodeMD5(PASSWORD);
		URL url = new URL("http://localhost:7474/login?login=" + login
				+ "&password=" + pass);
		
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		
		int responseCode = con.getResponseCode();
		System.out.println("Sending GET /login");
		System.out.println("Response Code: " + responseCode);
		String token = new String(con.getInputStream().readAllBytes(), "UTF-8");
		
		System.out.println("Token: " + token);
		return token;
	}
	
	private static int putItem() throws Exception {
		String url = "http://localhost:7474/api/good";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("PUT");
		con.setRequestProperty("Token", token);
		
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		String item = new Item("rice", 20, 500).toJSON().toString();
		wr.writeBytes(item);
		wr.flush();
		wr.close();
		
		int responseCode = con.getResponseCode();
		System.out.println("\nSending PUT /api/good");
		System.out.println("Put parameters: " + item);
		System.out.println("Response Code: " + responseCode);
		
		int id = ByteBuffer.wrap(con.getInputStream().readAllBytes()).getInt();
		System.out.println("Response: " + id);
		return id;
	}
	
	private static Item getItem() throws Exception {
		String url = "http://localhost:7474/api/good/" + id;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("Token", token);
		
		int responseCode = con.getResponseCode();
		System.out.println("\nSending GET /api/good/" + id);
		System.out.println("Response Code: " + responseCode);
		
		if (responseCode == 200) {
			String json = new String(con.getInputStream().readAllBytes(), "UTF-8");
			System.out.println("Response: " + json);
			
			JSONParser parser = new JSONParser();
			JSONObject jobj = (JSONObject) parser.parse(json);
			Item item = new Item((String)jobj.get("name"),
								 (int)(long)jobj.get("amount"),
								 (int)(long)jobj.get("price"),
								 (int)(long)jobj.get("id"));
			System.out.println("Parsed good: " + item);
			return item;
		}
		return null;
	}
	
	public static void postItem() throws Exception {
		String url = "http://localhost:7474/api/good";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Token", token);
		
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		String item = new Item("rice2", 200, 750, id).toJSON().toString();
		wr.writeBytes(item);
		wr.flush();
		wr.close();
		
		int responseCode = con.getResponseCode();
		System.out.println("\nSending POST /api/good");
		System.out.println("Post parameters: " + item);
		System.out.println("Response Code: " + responseCode);
	}
	
	public static void deleteItem() throws Exception {
		String url = "http://localhost:7474/api/good/" + id;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("DELETE");
		con.setRequestProperty("Token", token);
		
		int responseCode = con.getResponseCode();
		System.out.println("\nSending DELETE /api/good/ + id");
		System.out.println("Response Code: " + responseCode);
	}
	
	private static String encodeMD5(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		char[] encoded = encodeHex(new String(md.digest("password".getBytes()), "UTF-8").getBytes());
		return new String(encoded);
	}
}