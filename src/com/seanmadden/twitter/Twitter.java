/*
 * Twitter.java
 * 
 *  $Id $
 *  Author: smadden
 *
 *    Copyright (C) 2010 Sean Madden
 *
 *    Please see the pertinent documents for licensing information.
 *
 */

package com.seanmadden.twitter;
/**
 * An interface to the twitter API.
 *
 * @author Sean P Madden
 */
public class Twitter {

	private String username;
	private String password;

	public Twitter(String username, String password){
		this.username = username;
		this.password = password;
	}
}
