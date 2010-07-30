/*
 * DeathResponder.java
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

import com.seanmadden.deepthought.IRCClient;
import com.seanmadden.deepthought.Message;
import com.seanmadden.deepthought.MessageHandler;
import com.seanmadden.deepthought.User;

/**
 * [Insert class description here]
 *
 * @author Sean P Madden
 */
public class DeathResponder implements MessageHandler{

	@Override
	public boolean handleMessage(IRCClient irc, Message m) {
		String message = m.getMessage();
		User u = m.getUser();
		if(u == null || !u.isOpper()){
			return false;
		}
		if(message.contains(irc.getNick()) && message.contains("die")){
			irc.stopServer();
		}
		return true;
	}

}
