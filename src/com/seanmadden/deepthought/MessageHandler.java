/*
 * MessageHandler.java
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
/**
 * [Insert class description here]
 *
 * @author Sean P Madden
 */
public interface MessageHandler {
	public boolean handleMessage(IRCClient irc, Message m);
}
