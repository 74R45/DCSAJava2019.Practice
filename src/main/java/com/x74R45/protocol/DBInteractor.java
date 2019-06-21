package com.x74R45.protocol;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBInteractor {

	private static Connection con;
	
	public static void initialize() {
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/storage_db", "root", "password");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		return con;
	}
}