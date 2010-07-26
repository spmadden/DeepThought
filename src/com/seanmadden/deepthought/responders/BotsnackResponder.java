/*
 * BotsnackResponder.java
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

public class BotsnackResponder implements MessageHandler {

	@Override
	public boolean handleMessage(IRCClient irc, Message m) {
		String message = m.getMessage();
		if(!message.toLowerCase().contains("botsnack")){
			return false;
		}
		Message msg = new Message(":D", m.getTarget());
		irc.sendMessage(msg);
		return true;
	}

}
