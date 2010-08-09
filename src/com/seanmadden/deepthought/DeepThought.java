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

import com.seanmadden.deepthought.responders.*;

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
		this.handlers.add(new ShutupResponder());
		this.handlers.add(new IgnoreResponder());
		this.handlers.add(new ChannelResponder());
		
		this.handlers.add(new DeathResponder());
		this.handlers.add(new RouletteResponder());
		this.handlers.add(new SayResponder());
		this.handlers.add(new LMGTFYResponder());
		this.handlers.add(new QuoteResponder());
		this.handlers.add(new ManPageResponder());
		this.handlers.add(new WikiResponder());
		this.handlers.add(new DiceResponder());
		this.handlers.add(new SaluteResponder());
		
		//this.handlers.add(new LoveResponder());
		//this.handlers.add(new DanceResponder());
		//this.handlers.add(new BotsnackResponder());
		this.handlers.add(new InventoryResponder());
		this.handlers.add(new FactoidResponder());
		this.handlers.add(new QuestionResponder());
		

		//irc.getChannels().add("#interns");
		irc.getChannels().add("#bots");

		irc.start();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				for (String channel : irc.getChannels()) {
					Message m = new Message("blargh i am ded",
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
