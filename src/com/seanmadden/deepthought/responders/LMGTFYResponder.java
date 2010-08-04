/*
 * LMGTFYResponder.java
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

import com.seanmadden.bitly.Bitly;
import com.seanmadden.deepthought.IRCClient;
import com.seanmadden.deepthought.Message;
import com.seanmadden.deepthought.MessageHandler;

/**
 * [Insert class description here]
 *
 * @author Sean P Madden
 */
public class LMGTFYResponder implements MessageHandler {

	private Bitly bitly = new Bitly("spmadden", "R_53f4b9aea59201b8d4c1202ea89a3b0f");
	
	/**
	 * Handles a "How do I do xyztyv" message;
	 *
	 * @see com.seanmadden.deepthought.MessageHandler#handleMessage(com.seanmadden.deepthought.IRCClient, com.seanmadden.deepthought.Message)
	 * @param irc
	 * @param m
	 * @return
	 */
	@Override
	public boolean handleMessage(IRCClient irc, Message m) {
		String message = m.getMessage();
		if(!message.toLowerCase().contains("how do i")){
			return false;
		}
		String derp = message.toLowerCase().substring(message.toLowerCase().indexOf("how do i"));
		String msg = m.getNick() + ": " + bitly.shorten("http://lmgtfy.com/?q=" + derp);
		m.respondWith(msg, irc);
		return true;
	}

}
