/*
 * QueryCommand.java
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

import com.seanmadden.deepthought.Configuration;
import com.seanmadden.deepthought.IRCClient;
import com.seanmadden.deepthought.Message;
import com.seanmadden.deepthought.responders.FactoidResponder;

/**
 * [Insert class description here]
 * 
 * @author Sean P Madden
 */
public class QueryCommand extends FactoidCommand {

	public QueryCommand(FactoidResponder resp) {
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
		if (message.getMessage().matches(".*[:,] what was that(?:\\?)?$")) {
			if (resp.lastID == 0) {
				message.respondWith("I haven't said anything yet. >_>", irc);
				return true;
			}
			Connection conn = Configuration.getInstance().getConn();
			PreparedStatement st;
			try {
				st = conn
						.prepareStatement("select * from factoids where id is ? limit 1;");
				st.setInt(1, resp.lastID);

				st.execute();
				ResultSet rs = st.getResultSet();
				if (rs == null) {
					return false;
				}else if(!rs.next()){
					message.respondWith("What was what?  Dunno what you're talking about!", irc);
					return true;
				}
				int id = rs.getInt("id");
				String trigger = rs.getString("trigger");
				String response = rs.getString("response");
				trigger = trigger.replaceAll("%", "");
				message.respondWith("That was " + trigger + " (#" + id + "): "
						+ response, irc);
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return false;
	}

}
