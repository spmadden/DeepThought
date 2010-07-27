/*
 * LiteralCommand.java
 * 
 *  $Id $
 *  Author: smadden
 *
 *    Copyright (C) 2010 Sean Madden
 *
 *    Please see the pertinent documents for licensing information.
 *
 */

package com.seanmadden.deepthought.responders.factoidcommands;

import com.seanmadden.deepthought.IRCClient;
import com.seanmadden.deepthought.responders.FactoidResponder;

/**
 * [Insert class description here]
 *
 * @author Sean P Madden
 */
public class LiteralCommand extends FactoidCommand {

	public LiteralCommand(FactoidResponder resp) {
		super(resp);
	}

	/**
	 * [Place method description here]
	 *
	 * @see com.seanmadden.deepthought.responders.factoidcommands.FactoidCommand#checkCommand(java.lang.String)
	 * @param message
	 * @return
	 */
	@Override
	public boolean checkCommand(String message, IRCClient irc) {
		// TODO Auto-generated method stub
		return false;
	}

}
