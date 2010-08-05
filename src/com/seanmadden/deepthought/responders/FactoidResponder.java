/*
 * FactoidResponder.java
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
import java.util.Random;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.seanmadden.deepthought.Configuration;
import com.seanmadden.deepthought.IRCClient;
import com.seanmadden.deepthought.Message;
import com.seanmadden.deepthought.MessageHandler;
import com.seanmadden.deepthought.User;
import com.seanmadden.deepthought.responders.factoidcommands.*;

/**
 * [Insert class description here]
 * 
 * @author Sean P Madden
 */
public class FactoidResponder implements MessageHandler {
	private Connection conn = null;
	public int lastID = 0;

	private Vector<FactoidCommand> commands = new Vector<FactoidCommand>(){
		private static final long serialVersionUID = 6257238308939609540L;

		{
			add(new LiteralCommand(FactoidResponder.this));
			add(new QueryCommand(FactoidResponder.this));
			add(new ForgetCommand(FactoidResponder.this));
		}
	};
	
	private Pattern IS = Pattern.compile(".*[,:] (.+) is (.+)");
	private Pattern ARE = Pattern.compile(".*[,:] (.+) are (.+)");
	private Pattern ACTION = Pattern.compile(".*[,:] (.+?) <(.+?)> (.+)");

	public FactoidResponder() {
		conn = Configuration.getInstance().getConn();
	}

	/**
	 * [Place method description here]
	 * 
	 * @see com.seanmadden.deepthought.MessageHandler#handleMessage(com.seanmadden.deepthought.IRCClient,
	 *      com.seanmadden.deepthought.Message)
	 * @param irc
	 * @param m
	 * @return
	 */
	@Override
	public boolean handleMessage(IRCClient irc, Message m) {
		String message = m.getMessage();
		for(FactoidCommand c : this.commands){
			if(c.checkCommand(m, irc)){
				return true;
			}
		}
		User user = m.getUser();
		
		if (message.contains(irc.getNick()) && user != null ) {
			Matcher match = ACTION.matcher(message);
			if (match.matches()) {
				String trigger = match.group(1);
				String action = match.group(2);
				String response = match.group(3);
				try {
					this.addNewFact(irc, trigger, response, action, m);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return true;
			}
			match = IS.matcher(message);
			if (match.matches()) {
				String trigger = match.group(1);
				String response = match.group(2);
				String action = "is";
				try {
					this.addNewFact(irc, trigger, response, action, m);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return true;
			}
			match = ARE.matcher(message);
			if (match.matches()) {
				String trigger = match.group(1);
				String response = match.group(2);
				String action = "are";
				try {
					this.addNewFact(irc, trigger, response, action, m);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return true;
			}
		}/*else if(message.contains(irc.getNick()) && user != null && !user.isOpper()){
			Message msg = new Message("You are not an op, " + m.getNick(), m.getTarget());
			irc.sendMessage(msg);
			return true;
		}*/
		try {
			PreparedStatement s = conn
					.prepareStatement("select count(*) as length from factoids where ? like trigger;");
			s.setString(1, message);
			if (!s.execute()) {
				return false;
			}
			
			ResultSet set = s.getResultSet();
			if (set == null) {
				return false;
			}
			int length = set.getInt("length");
			if (length <= 0) {
				return false;
			}

			s = conn.prepareStatement("select * from factoids where ? like trigger;");
			s.setString(1, message);
			s.execute();
			set = s.getResultSet();

			if(!set.next()){
				return false;
			}
			
			Random rand = new Random();
			int pos = rand.nextInt(length);
			while (--pos >= 0) {
				if (!set.next()) {
					set.close();
					return false;
				}
			}
			String response = set.getString("response");
			String action = set.getString("action");
			String trigger = set.getString("trigger").replaceAll("%", "");
			response = response.replaceAll("\\$who", m.getNick());
			int id = set.getInt("id");
			set.close();
			if (response.equals("")) {
				return false;
			}
			
			this.lastID = id;
			if(response.contains("<reply>")){
				response = response.replaceAll("<reply>", "").trim();
			}else if(response.contains("<action>")){
				response = response.replaceAll("<action>", "").trim();
				response = "\u0001ACTION " + response + "\u0001";
			}else{
				response = trigger + " " + action + " " + response;
			}
			
			m.respondWith(response, irc);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void addNewFact(IRCClient irc, String trigger, String response,
			String action, Message m) throws SQLException {
		PreparedStatement s = conn
				.prepareStatement("insert into factoids (trigger, response, submitted_by, action) values (?, ?, ?, ?);COMMIT;");
		s.setString(1, "%" + trigger + "%");
		s.setString(2, response);
		s.setString(3, m.getNick());
		s.setString(4, action);
		conn.setAutoCommit(false);
		s.execute();
		s.close();
		conn.commit();
		conn.setAutoCommit(true);
		m.respondWith("As you wish, " + m.getNick(), irc);
	}
}
