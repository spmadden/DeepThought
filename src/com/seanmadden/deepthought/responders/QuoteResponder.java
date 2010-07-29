/*
 * QuoteResponder.java
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

import java.util.Collections;
import java.util.List;

import com.seanmadden.deepthought.IRCClient;
import com.seanmadden.deepthought.Message;
import com.seanmadden.deepthought.MessageHandler;
import com.seanmadden.qdbs.Chirpy;
import com.seanmadden.qdbs.Xkcdb;

/**
 * This class accesses the chirpy quote database.
 *
 * @author Sean P Madden
 */
public class QuoteResponder implements MessageHandler {
	private Chirpy chirpy = new Chirpy("quotes.seanmadden.net");
	private Xkcdb xkcdb = new Xkcdb();
	
	/**
	 * Handle the message.
	 *
	 * @see com.seanmadden.deepthought.MessageHandler#handleMessage(com.seanmadden.deepthought.IRCClient, com.seanmadden.deepthought.Message)
	 * @param irc
	 * @param m
	 * @return
	 */
	@Override
	public boolean handleMessage(IRCClient irc, Message m) {
		String message = m.getMessage();
		
		if(!message.contains("!quote") && !message.contains("!xkcdb")){
			return false;
		}
		if(message.contains("!xkcdb")){
			message = message.substring(message.indexOf("!xkcdb") + 6).trim();
			System.out.println(message);
			try{
				int num = Integer.parseInt(message);
				if(num <= 0){
					Message msg = new Message(m.getNick() + ": What?", m.getTarget());
					irc.sendMessage(msg);
					return true;
				}
				message = xkcdb.get(message);
				Message msg = new Message(message, m.getTarget());
				irc.sendMessage(msg);
				return true;
			}catch(NumberFormatException e){
				Message msg = new Message(m.getNick() + ": What?", m.getTarget());
				irc.sendMessage(msg);
				return true;
			}
		}
		message = message.substring(message.indexOf("!quote"));
		/*
		 * Add {quote}, queryword {word}
		 * querytag {tag}, addlast {int|user}
		 * get {id}
		 */
		
		String[] components = message.split(" ", 3);
		if(components.length != 3){
			sendHelpBack(irc, m);
			return true;
		}
		
		if(components[1].equals("add")){
			String result = chirpy.add(components[2], "Submitted by " + m.getNick(), "");
			Message msg = new Message(result, m.getTarget());
			irc.sendMessage(msg);
		}else if(components[1].equals("addlast")){
			try{
				int num = Integer.valueOf(components[2]);
				List<Message> messages = irc.getMessageHistory().subList(1, num+2);
				String concat = "";
				Collections.reverse(messages);
				for(Message msg : messages){
					if(msg.getMethod().equals("PRIVMSG")){
						concat += "<" + msg.getNick()+"> " + msg.getMessage() + "\r\n";
					}
				}
				String result = chirpy.add(concat, "Submitted by " + m.getNick(), "");
				Message msg = new Message(result, m.getTarget());
				irc.sendMessage(msg);
			}catch(Exception e){
				Message msg = new Message(m.getNick() + ": That's not a number!", m.getTarget());
				irc.sendMessage(msg);
				return true;
			}
			
		}else if(components[1].equals("get")){
			try{
				Integer.valueOf(components[2]);
			}catch(Exception e){
				Message msg = new Message(m.getNick() + ": That's not a number!", m.getTarget());
				irc.sendMessage(msg);
				return true;
			}
			String result = chirpy.get(components[2]);
			Message msg = new Message(result, m.getTarget());
			irc.sendMessage(msg);
		}
		
		
		return true;
	}
	
	private void sendHelpBack(IRCClient irc, Message m){
		String[] halp = {
			"Quotes Access Help",
			"---------------------------",
			"!quote add <quote> : Submits a quote to the database.  This is best for one-line quotes",
			"!quote addlast <number> : Adds the last <number> lines as a quote.",
			//"!quote addlast <user> : Adds the last phrase <user> said as a quote.",
			"!quote get <number> : Retrieves and sends the quote with id <number>.",
			//"!quote queryword <word> : Searches for quotes with <word> in them and returns the IDs.",
			//"!quote querytag <tag> : Searches for quotes with the tag <tag> and returns the IDs."
		};
		Message msg = new Message("", m.getNick());
		for(String line : halp){
			msg.setMessage(line);
			irc.sendMessage(msg);
		}
	}

}
