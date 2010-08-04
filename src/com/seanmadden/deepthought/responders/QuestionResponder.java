/*
 * QuestionResponder.java
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.seanmadden.deepthought.IRCClient;
import com.seanmadden.deepthought.Message;
import com.seanmadden.deepthought.MessageHandler;

public class QuestionResponder implements MessageHandler {

	private static String[] responses = { "As I see it, yes", "It is certain",
			"It is decidedly so", "Most likely", "Outlook good",
			"Signs point to yes", "Without a doubt", "Yes", "Yes, definitely",
			"You may rely on it", "Reply hazy, try again", "Ask again later",
			"Better not tell you now", "Cannot predict now",
			"Concentrate and ask again", "Don't count on it", "My reply is no",
			"My sources say no", "Outlook not so good", "Very doubtful" };

	private static String[] booleanQuestions = { "do", "does", "should",
			"will", "might", "may", "can", "must", "could", "would", "is",
			"are", "did", "has", "was", "have" };
	private static Pattern BOOLEANQ;

	public QuestionResponder() {
		String pattern;
		pattern = ".*(?:";
		for (String q : booleanQuestions) {
			pattern += "(?:" + q + ")|";
		}
		pattern = pattern.substring(0, pattern.length()-1);
		pattern += ") (.+)\\?$";
		BOOLEANQ = Pattern.compile(pattern);
		System.out.println(pattern);
	}

	@Override
	public boolean handleMessage(IRCClient irc, Message m) {
		String message = m.getMessage();

		// respond to "xxx or yyyy" // either or

		if (!message.contains(irc.getNick())) {
			return false;
		}
		if (message.contains("?") && message.toLowerCase().contains(" or ")) {
			String options = message.split("\\? ", 2)[1];
			String opts[] = options.split(" or ");
			int choice = new Random().nextInt(opts.length);
			String response = m.getNick() + ": " + opts[choice];
			m.respondWith(response, irc);
			return true;
		}

		Matcher matcher = BOOLEANQ.matcher(message);
		if (matcher.matches()) {
			int choice = new Random().nextInt(responses.length);
			String response = m.getNick() + ": " + responses[choice];
			m.respondWith(response, irc);
			return true;
		}

		m.respondWith("You rang?", irc);
		return false;
	}

}
