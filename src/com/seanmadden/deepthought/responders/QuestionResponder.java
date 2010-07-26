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
			"are", "did", "has" };

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
			String response = m.getUsermask() + ": " + opts[choice];
			Message msg = new Message(response, m.getTarget());
			irc.sendMessage(msg);
			return true;
		}

		for (String q : booleanQuestions) {
			if (message.toLowerCase().contains(q)
					&& message.length() > q.length()) {
				int choice = new Random().nextInt(responses.length);
				String response = m.getUsermask() + ": " + responses[choice];
				Message msg = new Message(response, m.getTarget());
				irc.sendMessage(msg);
				return true;
			}
		}
		Message msg = new Message("You rang?", m.getTarget());
		irc.sendMessage(msg);
		return false;
	}

}
