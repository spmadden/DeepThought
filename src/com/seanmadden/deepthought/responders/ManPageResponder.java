/*
 * ManPageResponder.java
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.seanmadden.bitly.Bitly;
import com.seanmadden.deepthought.IRCClient;
import com.seanmadden.deepthought.Message;
import com.seanmadden.deepthought.MessageHandler;

public class ManPageResponder implements MessageHandler {
	private Bitly bitly = new Bitly("spmadden", "R_53f4b9aea59201b8d4c1202ea89a3b0f");
	
	@Override
	public boolean handleMessage(IRCClient irc, Message m) {
		String message = m.getMessage();
		if(!message.contains("!man")){
			return false;
		}
		
		String page = message.substring(message.indexOf("!man") + 4);
		String url = "http://www.google.com/search?btnI=I%27m+Feeling+Lucky&aq=f&q=site%3Alinux.die.net+";
		try {
			url += URLEncoder.encode(page, "UTF-8");
			url = bitly.shorten(url);
			Message msg = new Message( m.getUsermask()+": " + url, m.getTarget());
			irc.sendMessage(msg);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return true;
	}

}
