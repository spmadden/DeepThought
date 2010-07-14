/*
 * QuoteResponder.java
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
 * This class accesses the chirpy quote database.
 *
 * @author Sean P Madden
 */
public class QuoteResponder implements MessageHandler {

	/**
	 * Handle the message.
	 *
	 * @see com.seanmadden.deepthought.MessageHandler#handleMessage(com.seanmadden.deepthought.IRCClient, com.seanmadden.deepthought.Message)
	 * @param irc
	 * @param m
	 * @return
	 */
	@Override
	public boolean handleMessage(IRCClient irc, Message m) {
		String message = m.getMessage();
		if(!message.contains("!wiki")){
			return false;
		}
		message = message.substring(message.indexOf("!wiki"));
		/*
		 * Add {quote}, queryword {word}
		 * querytag {tag}, addlast {int|user}
		 * get {id}
		 */
		
		String[] components = message.split(" ", 3);
		if(components.length != 3){
			
		}
		
		return true;
	}
	
	private void sendHelpBack(IRCClient irc, Message m){
		String[] halp = {
			"Quotes Access Help",
			"---------------------------",
				
		};
	}

}
