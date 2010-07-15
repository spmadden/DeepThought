/*
 * DeepThought.java
 * 
 *  $Id $
 *  Author: smadden
 *
 *    Copyright (C) 2010 Sean Madden
 *
 *    Please see the pertinent documents for licensing information.
 *
 */

package com.seanmadden.deepthought;

import java.util.Vector;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.seanmadden.deepthought.responders.LMGTFYResponder;
import com.seanmadden.deepthought.responders.PingResponder;
import com.seanmadden.deepthought.responders.QuoteResponder;

/**
 * This class is the main entry point into the DeepThought IRC bot.
 *
 * @author Sean P Madden
 */
public class DeepThought implements MessageObserver{
	
	private static Logger log = Logger.getLogger(DeepThought.class);
	
	private Vector<MessageHandler> handlers = new Vector<MessageHandler>();
	private IRCClient irc = null;
	
	public DeepThought(){
		
	}
	
	public void run(){
		irc = new IRCClient();
		irc.addCallback("PRIVMSG", this);
		irc.addCallback("PING", this);
		this.handlers.add(new PingResponder());
		this.handlers.add(new LMGTFYResponder());
		this.handlers.add(new QuoteResponder());
		
		irc.getChannels().add("#interns");
		
		irc.start();
	}
	
	

	/**
	 * Kick off the bot.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();
		log.debug("Entering Main");
		DeepThought dt = new DeepThought();
		
		log.debug("Staring Bot");
		dt.run();

	}

	@Override
	public void wildMessageAppeared(Message m) {
		log.debug(m.toString());
		for(MessageHandler h : this.handlers){
			if(h.handleMessage(this.irc, m)){
				return;
			}
		}
	}

}
