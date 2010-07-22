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

import com.seanmadden.deepthought.responders.DanceResponder;
import com.seanmadden.deepthought.responders.LMGTFYResponder;
import com.seanmadden.deepthought.responders.LoveResponder;
import com.seanmadden.deepthought.responders.PingResponder;
import com.seanmadden.deepthought.responders.QuestionResponder;
import com.seanmadden.deepthought.responders.QuoteResponder;
import com.seanmadden.deepthought.responders.WikiResponder;

/**
 * This class is the main entry point into the DeepThought IRC bot.
 * 
 * @author Sean P Madden
 */
public class DeepThought implements MessageObserver {

	private static Logger log = Logger.getLogger(DeepThought.class);

	private Vector<MessageHandler> handlers = new Vector<MessageHandler>();
	private IRCClient irc = null;

	public DeepThought() {

	}

	public void run() {
		irc = new IRCClient();
		irc.addCallback("PRIVMSG", this);
		irc.addCallback("PING", this);
		this.handlers.add(new PingResponder());
		this.handlers.add(new LMGTFYResponder());
		this.handlers.add(new QuoteResponder());
		this.handlers.add(new WikiResponder());
		this.handlers.add(new DanceResponder());
		this.handlers.add(new LoveResponder());
		this.handlers.add(new QuestionResponder());
		

		irc.getChannels().add("#interns");

		irc.start();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				for (String channel : irc.getChannels()) {
					Message m = new Message("", "PRIVMSG", "blargh i am ded",
							channel);
					irc.sendMessage(m);
				}
			}
		});
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
		for (MessageHandler h : this.handlers) {
			if (h.handleMessage(this.irc, m)) {
				return;
			}
		}
	}

}
