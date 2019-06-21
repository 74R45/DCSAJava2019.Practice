package com.x74R45.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.sql.SQLException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.x74R45.protocol.Item;
import com.x74R45.protocol.Storage;

public class GoodHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String token = exchange.getRequestHeaders().getFirst("Token");
		if (token == null || !TokenStorage.validateToken(token)) {
			exchange.sendResponseHeaders(403, 0);
			OutputStream os = exchange.getResponseBody();
			os.close();
			return;
		}
		
		String method = exchange.getRequestMethod();
		switch (method) {
		case "GET":
			getItem(exchange, getId(exchange));
			break;
		case "POST":
			postItem(exchange);
			break;
		case "PUT":
			putItem(exchange);
			break;
		case "DELETE":
			deleteItem(exchange, getId(exchange));
			break;
		}
	}
	
	private void getItem(HttpExchange exchange, int id) throws IOException {
		try {
			Item item = Storage.getItem(id);
			JSONObject jobj = item.toJSON();
			
			String response = jobj.toString();
			exchange.sendResponseHeaders(200, response.length());
			
			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		} catch (SQLException e) {
			e.printStackTrace();
			exchange.sendResponseHeaders(404, "Error".length());
	        OutputStream os = exchange.getResponseBody();
	        os.write("Error".getBytes());
	        os.close();
		}
		
	}
	
	private void postItem(HttpExchange exchange) throws IOException {
		String json = new String(exchange.getRequestBody().readAllBytes());
		JSONParser parser = new JSONParser();
		
		try {
			JSONObject jobj = (JSONObject) parser.parse(json);
			Item item = new Item((String)jobj.get("name"),
								 (int)(long)jobj.get("amount"),
								 (int)(long)jobj.get("price"),
								 (int)(long)jobj.get("id"));
			if (!item.isValid()) {
				exchange.sendResponseHeaders(409, "Bad Request".length());
		        OutputStream os = exchange.getResponseBody();
		        os.write("Bad Request".getBytes());
		        os.close();
		        return;
			}
			
			try {
				Storage.changeItem(item);
				exchange.sendResponseHeaders(204, 0);
				OutputStream os = exchange.getResponseBody();
				os.close();
			} catch (SQLException e) {
				e.printStackTrace();
		        exchange.sendResponseHeaders(404, "Error".length());
		        OutputStream os = exchange.getResponseBody();
		        os.write("Error".getBytes());
		        os.close();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void putItem(HttpExchange exchange) throws IOException {
		String json = new String(exchange.getRequestBody().readAllBytes());
		JSONParser parser = new JSONParser();
		
		try {
			JSONObject jobj = (JSONObject) parser.parse(json);
			Item item = new Item((String)jobj.get("name"),
								 (int)(long)jobj.get("amount"),
								 (int)(long)jobj.get("price"));
			if (!item.isValid()) {
				exchange.sendResponseHeaders(409, "Bad Request".length());
		        OutputStream os = exchange.getResponseBody();
		        os.write("Bad Request".getBytes());
		        os.close();
		        return;
			}
			
			try {
				int id = Storage.createItem(item);
				byte[] idArr = ByteBuffer.allocate(4).putInt(id).array();
				exchange.sendResponseHeaders(201, idArr.length);
				OutputStream os = exchange.getResponseBody();
		        os.write(idArr);
		        os.close();
			} catch (SQLException e) {
				e.printStackTrace();
		        exchange.sendResponseHeaders(400, "Error".length());
		        OutputStream os = exchange.getResponseBody();
		        os.write("Error".getBytes());
		        os.close();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void deleteItem(HttpExchange exchange, int id) throws IOException {
		try {
			Storage.deleteItem(id);
			
			exchange.sendResponseHeaders(204, 0);
			OutputStream os = exchange.getResponseBody();
			os.close();
		} catch (SQLException e) {
		      e.printStackTrace();
		      exchange.sendResponseHeaders(404, "Error".length());
		      OutputStream os = exchange.getResponseBody();
		      os.write("Error".getBytes());
		      os.close();
		    }
	}
	
	private int getId(HttpExchange exchange) {		
		String path = exchange.getRequestURI().getPath();
		return Integer.parseInt(path.split("/")[3]);
	}
}