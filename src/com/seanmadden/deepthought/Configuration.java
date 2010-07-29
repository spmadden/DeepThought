/*
 * Configuration.java
 * 
 *  $Id $
 *  Author: smadden
 *
 *    Copyright (C) 2010 Sean Madden
 *
 *    Please see the pertinent documents for licensing information.
 *
 */

package com.seanmadden.deepthought;

import java.sql.*;

/**
 * [Insert class description here]
 * 
 * @author Sean P Madden
 */
public class Configuration {
	private static Configuration config = new Configuration();
	private Connection conn = null;

	private Configuration() {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:factoids.db");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the conn
	 *
	 * @return conn the conn
	 */
	public Connection getConn() {
		return conn;
	}

	public static Configuration getInstance() {
		return config;
	}
}
