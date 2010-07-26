/*
 * PingResponder.java
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

/**
 * [Insert class description here]
 * 
 * @author Sean P Madden
 */
public class PingResponder implements MessageHandler {

	@Override
	public boolean handleMessage(IRCClient irc, Message m) {
		if (m.getMethod().equals("PING")) {
			Message mesg = new Message("PONG", m.getTarget(), "");
			irc.sendMessage(mesg);
			return true;
		}
		return false;
	}

}
