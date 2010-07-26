/*
 * DanceResponder.java
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

public class DanceResponder implements MessageHandler {

	@Override
	public boolean handleMessage(IRCClient irc, Message m) {
		if(!m.getMessage().toLowerCase().contains("dance")){
			return false;
		}
		String[] message = {
			/*	"DANCE?  OKAY!",
				"\u0001ACTION dances\u0001",
				"\u0001ACTION (>'.')>\u0001",
				"\u0001ACTION (7'.')7\u0001",
				"\u0001ACTION <('.'7)\u0001",
				*/
				"Sean says I can't dance anymore, I break things =/"
				};
		for(String action : message){
			Message msg = new Message(action, m.getTarget());
			irc.sendMessage(msg);
		}
		
		return true;
	}

}
