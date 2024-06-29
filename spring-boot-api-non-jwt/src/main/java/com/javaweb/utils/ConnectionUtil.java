package com.javaweb.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionUtil {
    static final String DB_URL = "jdbc:mysql://localhost:3306/estatebasic";
    static final String USER = "root";
    static final String PASS = "190502";
    
	public static Connection getConnection() {
		try {
			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}