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
import java.util.Vector;

/**
 * [Insert class description here]
 * 
 * @author Sean P Madden
 */
public class Configuration {
	private static Configuration config = new Configuration();
	private Connection conn = null;
	private Vector<String> items = new Vector<String>();

	private Configuration() {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:mysql://seanmadden.net:3306/deepthought?user=deepthought&password=");
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
	
	public Vector<String> getItems(){
		return items;
	}

	public static Configuration getInstance() {
		return config;
	}
}
