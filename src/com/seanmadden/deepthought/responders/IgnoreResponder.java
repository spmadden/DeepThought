/*
 * IgnoreResponder.java
 * 
 *  $Id $
 *  Author: smadden
 *
 *    Copyright (C) 2010 Sean Madden
 *
 *    Please see the pertinent documents for licensing information.
 *
 */

package com.seanmadden.deepthought.responders;

import java.sql.*;

import com.seanmadden.deepthought.Configuration;
import com.seanmadden.deepthought.IRCClient;
import com.seanmadden.deepthought.Message;
import com.seanmadden.deepthought.MessageHandler;

/**
 * This class implements an ignore list for all users.
 *
 * @author Sean P Madden
 */
public class IgnoreResponder implements MessageHandler {

	private Connection conn = null;
	
	public IgnoreResponder(){
		conn = Configuration.getInstance().getConn();
	}
	
	/**
	 * [Place method description here]
	 *
	 * @see com.seanmadden.deepthought.MessageHandler#handleMessage(com.seanmadden.deepthought.IRCClient, com.seanmadden.deepthought.Message)
	 * @param irc
	 * @param m
	 * @return
	 */
	@Override
	public boolean handleMessage(IRCClient irc, Message m) {
		String message = m.getMessage();
		try {
			PreparedStatement st = conn.prepareStatement("select * from ignored_users where user = ?;");
			st.setString(1, m.getNick());
			st.execute();
			ResultSet set = st.getResultSet();
			if(set.next()){
				return true;
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
