/*
 * SayResponder.java
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

public class SayResponder implements MessageHandler {

	@Override
	public boolean handleMessage(IRCClient irc, Message m) {
		String message = m.getMessage();
		if(m.getTarget().contains("#")){
			return false;
		}
		if(!message.contains("sayto ")){
			return false;
		}
		// sayto #interns xxxxx
		String[] args = message.split(" ", 3);
		if(!args[0].equals("sayto")){
			return false;
		}
		Message msg = new Message("", "PRIVMSG", args[2], args[1]);
		irc.sendMessage(msg);
		return true;
	}

}
