/*
 * RouletteResponder.java
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

import java.util.Random;

import com.seanmadden.deepthought.IRCClient;
import com.seanmadden.deepthought.Message;
import com.seanmadden.deepthought.MessageHandler;

/**
 * [Insert class description here]
 * 
 * @author Sean P Madden
 */
public class RouletteResponder implements MessageHandler {

	/**
	 * [Place method description here]
	 * 
	 * @see com.seanmadden.deepthought.MessageHandler#handleMessage(com.seanmadden.deepthought.IRCClient,
	 *      com.seanmadden.deepthought.Message)
	 * @param irc
	 * @param m
	 * @return
	 */
	@Override
	public boolean handleMessage(IRCClient irc, Message m) {
		String message = m.getMessage();
		if (!message.toLowerCase().startsWith("!roulette")) {
			return false;
		}
		Message msg = new Message("\u0001ACTION slowly points a gun at "
				+ m.getNick()+"\u0001", m.getTarget());
		irc.sendMessage(msg);
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Random rand = new Random();
		if(rand.nextInt(6) == 0){
			msg = new Message("KICK", "BAM!", m.getTarget() + " " + m.getNick());
		}else{
			msg.setMessage(m.getNick() + " hears the click of a hammer.  They are safe for now.");
		}
		irc.sendMessage(msg);
		return true;
	}

}
