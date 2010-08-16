/*
 * InventoryResponder.java
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
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.seanmadden.deepthought.Configuration;
import com.seanmadden.deepthought.IRCClient;
import com.seanmadden.deepthought.Message;
import com.seanmadden.deepthought.MessageHandler;

/**
 * [Insert class description here]
 * 
 * @author Sean P Madden
 */
public class InventoryResponder implements MessageHandler {

	private static String[] NOTHANKS = {
			"No thanks, $who, I've already got one", "I already have $item",
			"But I've already got $item!", "$who: I already have $item" };
	private static String[] CONTAINS = { "now contains $item",
			"is now carrying $item", "is now holding $item", "takes $item" };

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
		if (handleGives(m, irc)) {
			return true;
		}
		String message = m.getMessage();
		if (message.matches(irc.getNick() + "[:,] inventory")) {
			Vector<String> items = Configuration.getInstance().getItems();
			String response = "contains ";
			if(items.size() == 0){
				m.respondWith("I'm empty!", irc);
				return true;
			}
			if (items.size() >= 3) {
				response += items.elementAt(0);
				for(int i = 1; i < items.size()-1; i++){
					response += ", " + items.elementAt(i);
				}
				response += " and " + items.lastElement();
			}else if(items.size() == 2){
				response += items.firstElement() + " and " + items.lastElement();
			}else {
				response += items.firstElement();
			}
			m.respondWith("\u0001ACTION " + response + "\u0001", irc);
			return true;
		}

		return false;
	}

	private void addItem(String item, Message m, IRCClient irc) {
		item = item.trim();
		if(item.matches("(his|her) (.+)")){
			item = item.replaceFirst("(his|her)", m.getNick()+"'s");
		}
		for (String i : Configuration.getInstance().getItems()) {
			if (i.toLowerCase().equals(item.toLowerCase())) {
				String respond = NOTHANKS[new Random().nextInt(NOTHANKS.length)];
				respond = respond.replaceAll("\\$who", m.getNick());
				respond = respond.replaceAll("\\$item", item);
				m.respondWith(respond, irc);
				return;
			}
		}
		Configuration.getInstance().getItems().add(item);
		String respond = CONTAINS[new Random().nextInt(CONTAINS.length)];
		respond = respond.replaceAll("\\$item", item);
		m.respondWith("\u0001ACTION " + respond + "\u0001", irc);
		return;
	}

	private boolean handleGives(Message m, IRCClient irc) {
		String message = m.getMessage();
		if (message.matches(".*?puts (.+) in " + irc.getNick() + ".*?")) {
			Matcher mat = Pattern.compile(
					".*?puts (.+) in " + irc.getNick() + ".*?")
					.matcher(message);
			if (!mat.matches()) {
				return false;
			}
			addItem(mat.group(1), m, irc);
			return true;
		} else if (message.matches(".*?gives (.+) to " + irc.getNick() + ".*?")) {
			Matcher mat = Pattern.compile(
					".*?gives (.+) to " + irc.getNick() + ".*?").matcher(
					message);
			if (!mat.matches()) {
				return false;
			}
			addItem(mat.group(1), m, irc);
			return true;
		} else if (message.matches(".*?gives " + irc.getNick() + " (.+)")) {
			Matcher mat = Pattern
					.compile(".*?gives " + irc.getNick() + " (.+)").matcher(
							message);
			if (!mat.matches()) {
				return false;
			}
			addItem(mat.group(1), m, irc);
			return true;
		} else if (message.matches(irc.getNick() + "[:,] take(?: this| the)*[:]? (.+)")) {
			Matcher mat = Pattern.compile(
					irc.getNick() + "[:,] take this[:]? (.+)").matcher(message);
			if (!mat.matches()) {
				return false;
			}
			addItem(mat.group(1), m, irc);
			return true;
		} else if (message.matches(irc.getNick() + "[:,] have a[:]? (.+)")) {
			Matcher mat = Pattern.compile(
					irc.getNick() + "[:,] have a[:]? (.+)").matcher(message);
			if (!mat.matches()) {
				return false;
			}
			addItem(mat.group(1), m, irc);
			return true;
		}
		return false;
	}

}
