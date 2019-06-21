package com.x74R45.rest;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.sun.net.httpserver.HttpServer;
import com.x74R45.protocol.DBInteractor;

public class HttpServerLauncher {

	private static final int PORT = 7474;
	private static final int NTHREADS = 100;
	private static final Executor exec = Executors.newFixedThreadPool(NTHREADS);
	
	public static void main(String[] args) throws Exception {
		HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
		
		DBInteractor.initialize();
		TokenStorage.clearTokens();
		
		server.createContext("/login", new LoginHandler());
		server.createContext("/api/good", new GoodHandler());
		server.setExecutor(exec);
		server.start();
	}
}