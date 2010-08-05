/*
 * ShutupResponder.java
 * 
 *  $Id $
 *  Author: smadden
 *
 *    Copyright (C) 2010 Factset Systems
 *
 *    Please see the pertinent documents for licensing information.
 *
 */

package com.seanmadden.deepthought.responders;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Timer;

import com.seanmadden.deepthought.IRCClient;
import com.seanmadden.deepthought.Message;
import com.seanmadden.deepthought.MessageHandler;

public class ShutupResponder implements MessageHandler, ActionListener {

	private boolean shutup = false;
	private Timer timer = null;

	public ShutupResponder() {
		this.timer = new Timer(0, this);
		timer.setRepeats(false);
	}

	@Override
	public boolean handleMessage(IRCClient irc, Message m) {
		String message = m.getMessage();
		if (message.matches(irc.getNick() + "[:,] shut up$")) {
			m.respondWith("Okay, " + m.getNick() + ", I'm shutting up now.",
					irc);
			shutup = true;
			return true;
		} else if (message.matches(irc.getNick() + "[:,] go away$")) {
			m.respondWith("Okay, " + m.getNick() + ", I'm shutting up now.",
					irc);
			shutup = true;
			return true;
		} else if (message.matches(irc.getNick() + "[:,] come back$")) {
			m.respondWith("Okay, I'm back!", irc);
			timer.stop();
			shutup = false;
			return true;
		} else if (message.matches(irc.getNick() + "[:,] unshut up$")) {
			m.respondWith("Okay, I'm back!", irc);
			timer.stop();
			shutup = false;
			return true;
		} else if (message.matches(irc.getNick() + "[:,] shut up for (.+)$")) {
			// shut up for (a min|a moment|a bit | a while)
			// (60 s |1m+-30s |6m+-2m|45m+-15m)
			Matcher match = Pattern.compile(
					irc.getNick() + "[:,] shut up for (.+)$").matcher(message);
			if(!match.matches()){
				return false;
			}
			String id = match.group(1).trim();
			int secs = 0;
			if(id.equals("a min")){
				secs = 60;
			}else if(id.equals("a moment")){
				secs = 60 + new Random().nextInt(60);
			}else if(id.equals("a bit")){
				secs = 6 * 60 + new Random().nextInt(2 * 60);
			}else if(id.equals("a while")){
				secs = 45 * 60 + new Random().nextInt(15 * 60);
			}
			timer.setInitialDelay(secs * 1000);
			timer.start();
			this.shutup = true;
			m.respondWith("Okay, I'll be back in " + secs/60 + "m " + secs%60 + "s", irc);
			return true;
		} else if (message.matches(irc.getNick()
				+ "[:,] shut up for (\\d+)([smh])$")) {
			Matcher match = Pattern.compile(
					irc.getNick() + "[:,] shut up for (\\d+)([smh])$").matcher(
					message);
			if (!match.matches()) {
				return false;
			}
			int secs = Integer.parseInt(match.group(1));
			if (match.group(2).equals("m")) {
				secs *= 60;
			} else if (match.group(2).equals("h")) {
				secs *= 3600;
			}
			timer.setInitialDelay(secs * 1000);
			timer.start();
			m.respondWith("Okay, I'll be back in " + match.group(1)
					+ match.group(2), irc);
			this.shutup = true;
			return true;
		}
		return shutup;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.shutup = false;
	}

}
