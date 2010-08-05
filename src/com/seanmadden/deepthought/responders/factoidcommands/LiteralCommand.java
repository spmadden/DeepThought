/*
 * LiteralCommand.java
 * 
 *  $Id $
 *  Author: smadden
 *
 *    Copyright (C) 2010 Sean Madden
 *
 *    Please see the pertinent documents for licensing information.
 *
 */

package com.seanmadden.deepthought.responders.factoidcommands;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.seanmadden.deepthought.Configuration;
import com.seanmadden.deepthought.IRCClient;
import com.seanmadden.deepthought.Message;
import com.seanmadden.deepthought.responders.FactoidResponder;

/**
 * [Insert class description here]
 * 
 * @author Sean P Madden
 */
public class LiteralCommand extends FactoidCommand {

	public LiteralCommand(FactoidResponder resp) {
		super(resp);
	}

	/**
	 * [Place method description here]
	 * 
	 * @see com.seanmadden.deepthought.responders.factoidcommands.FactoidCommand#checkCommand(java.lang.String)
	 * @param message
	 * @return
	 */
	@Override
	public boolean checkCommand(Message message, IRCClient irc) {
		if (message.getMessage().matches(irc.getNick() + "[:,] literal (.+)$")) {
			Matcher m = Pattern.compile(irc.getNick()+ "[:,] literal (.+)$").matcher(
					message.getMessage());
			if (!m.matches()) {
				return false;
			}
			Connection conn = Configuration.getInstance().getConn();
			try {
				PreparedStatement ps = conn
						.prepareStatement("select * from factoids where ? like trigger;");
				ps.setString(1, m.group(1));
				ps.execute();
				ResultSet rs = ps.getResultSet();
				if (rs == null) {
					message.respondWith("I can't find " + m.group(1), irc);
					return false;
				}
				String response = m.group(1) + " is ";
				int count = 0;
				while (rs.next()) {
					response += rs.getString("response") + " | ";
					++count;
				}
				if(count == 0){
					response = "I can't find " + m.group(1);
				}else{
					response = response.substring(0, response.length() - 2);
				}
				message.respondWith(response, irc);
				return true;

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
