/*
 * DiceResponder.java
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

public class DiceResponder implements MessageHandler {

	@Override
	public boolean handleMessage(IRCClient irc, Message m) {
		String message = m.getMessage().trim();
		if(!message.startsWith("!roll")){
			return false;
		}
		String[] args = message.split(" ", 2);
		if(args.length != 2){
			return false;
		}
		
		try{
			String dice = args[1];
			String[] fmt = dice.split("d", 2);
			int die = Integer.parseInt(fmt[0]);
			int sides = Integer.parseInt(fmt[1]);
			if(die > 30 || die < 1){
				m.respondWith(m.getNick() + ": No.", irc);
				return true;
			}
			if(sides > 9999 || sides < 1){
				m.respondWith(m.getNick() + ": No.", irc);
				return true;
			}
			String response = "you rolled:";
			int total = 0;
			Random r = new Random();
			for(int i = 0; i < die; ++i){
				int res = r.nextInt(sides)+1;
				response += res + " ";
				total += res;
			}
			if(die > 1){
				response += "for a total of: " + total;
			}
			
			m.respondWith(m.getNick() + ": " + response, irc);
		}catch(NumberFormatException e){
			m.respondWith(m.getNick() + ": can't figure out what you meant!", irc);
		}
		return true;
	}

}
