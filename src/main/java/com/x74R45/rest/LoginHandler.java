package com.x74R45.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Key;
import java.sql.SQLException;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.x74R45.protocol.Storage;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class LoginHandler implements HttpHandler {
	
	private static final String SECRET_KEY = "7h3m05753cr373573v3r";
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		if (exchange.getRequestMethod().equals("GET")) {
			String query = exchange.getRequestURI().getRawQuery();
			if (query == null) {
				sendUnauthorized(exchange);
				return;
			}
			
			String login = null, password = null;
			for (String param : query.split("&")) {
				param = java.net.URLDecoder.decode(param, "UTF-8");
				String[] entry = param.split("=");
				if (entry[0].equals("login"))
					login = entry[1];
				else if (entry[0].equals("password"))
					password = entry[1];
			}
			
			try {
				if (!Storage.checkUser(login, password)) {
					sendUnauthorized(exchange);
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				sendUnauthorized(exchange);
				return;
			}
			
			SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
			
			long nowMillis = System.currentTimeMillis();
		    Date now = new Date(nowMillis);
			
			byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
		    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		    
			JwtBuilder builder = Jwts.builder()
					.setId(login)
			        .setIssuedAt(now)
			        .setSubject("login")
			        .signWith(signingKey, signatureAlgorithm);
			
			String token = builder.compact();
			TokenStorage.saveToken(token);
			
			System.out.println("Logged in successfully for user \"" + login + "\".");
			
			exchange.sendResponseHeaders(200, token.length());
			OutputStream os = exchange.getResponseBody();
			os.write(token.getBytes());
			os.close();
		}
	}
	
	private void sendUnauthorized(HttpExchange exchange) throws IOException {
		exchange.sendResponseHeaders(401, 0);
		OutputStream os = exchange.getResponseBody();
		os.close();
	}
}