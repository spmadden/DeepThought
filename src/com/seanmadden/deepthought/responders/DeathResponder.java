/*
 * DeathResponder.java
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

import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.seanmadden.deepthought.IRCClient;
import com.seanmadden.deepthought.Message;
import com.seanmadden.deepthought.MessageHandler;
import com.seanmadden.deepthought.User;

/**
 * [Insert class description here]
 *
 * @author Sean P Madden
 */
public class DeathResponder implements MessageHandler{

	private TreeSet<String> bots = new TreeSet<String>();
	private static Pattern IS = Pattern.compile(".*[:,] (.+) is a bot$");
	private static Pattern NOT = Pattern.compile(".*[:,] (.+) is not a bot$");
	
	@Override
	public boolean handleMessage(IRCClient irc, Message m) {
		String message = m.getMessage();
		User u = m.getUser();
		if(u == null || !u.isOpper()){
			return false;
		}
		if(message.matches(irc.getNick() + "[:,] die$")){
			irc.stopServer();
			return true;
		}
		
		if(message.matches(irc.getNick() + "[:,] execute the bots$")){
			for(String bot : bots){
				m.respondWith("\u0001ACTION slowly points a gun at "
						+bot+"\u0001", irc);
				try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = new Message("KICK", "BOT!", m.getTarget() + " " + bot);
				irc.sendMessage(msg);
			}
			return true;
		}
		Matcher match = IS.matcher(message);
		if(match.matches()){
			String bot = match.group(1);
			bots.add(bot);
			m.respondWith("\u0001ACTION hisses at " + bot + "\u0001", irc);
			return true;
		}
		match = NOT.matcher(message);
		if(match.matches()){
			String bot = match.group(1);
			bots.remove(bot);
			m.respondWith("Okay, " + m.getNick(), irc);
			m.respondWith("\u0001ACTION pets " + bot + "\u0001", irc);
			return true;
		}
		return false;
	}

}
