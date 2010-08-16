/*
 * ChannelResponder.java
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.seanmadden.deepthought.IRCClient;
import com.seanmadden.deepthought.Message;
import com.seanmadden.deepthought.MessageHandler;

/**
 * [Insert class description here]
 *
 * @author Sean P Madden
 */
public class ChannelResponder implements MessageHandler{
	private static Pattern JOIN = Pattern.compile(".*: join (.+)$");
	private static Pattern PART = Pattern.compile(".*: part (.+)$");
	
	@Override
	public boolean handleMessage(IRCClient irc, Message m) {
		String message = m.getMessage();
		if(m.getUser() != null && !m.getUser().isOpper()){
			return false;
		}
		
		Matcher matcher =JOIN.matcher(message);
		if(matcher.matches()){
			irc.joinChannel(matcher.group(1));
			return true;
		}
		matcher = PART.matcher(message);
		if(matcher.matches()){
			String channel = matcher.group(1);
			irc.partChannel(channel);
			return true;
		}
		return false;
	}

}
