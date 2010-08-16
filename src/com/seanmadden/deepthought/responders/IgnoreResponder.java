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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
			
			if(m.getUser() != null && m.getUser().isOpper()){
				if(!message.startsWith(irc.getNick())){
					return false;
				}
				if(!message.contains("ignore")){
					return false;
				}
				Pattern p = Pattern.compile("^"+irc.getNick() + "[:,] ignore (.+)$");
				Matcher match = p.matcher(message);
				if(match.matches()){
					String user = match.group(1);
					PreparedStatement s = conn.prepareStatement("insert into ignored_users (user) values (?);");
					s.setString(1, user.trim());
					conn.setAutoCommit(false);
					s.execute();
					s.close();
					conn.commit();
					conn.setAutoCommit(true);
					
					m.respondWith("Okay, " + m.getNick() + ".  I'm ignoring " + user, irc);
					return true;
				}
				match = Pattern.compile("^"+irc.getNick()+"[:,] unignore (.+)$").matcher(message);
				if(match.matches()){
					String user = match.group(1);
					PreparedStatement s = conn.prepareStatement("delete from ignored_users where user = ?;");
					s.setString(1, user.trim());
					conn.setAutoCommit(false);
					s.execute();
					s.close();
					conn.commit();
					conn.setAutoCommit(true);
					
					m.respondWith("Okay, " + m.getNick() + ".  I'm unignoring " + user, irc);
					return true;
				}
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
