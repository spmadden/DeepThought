/*
 * FactoidCommand.java
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
import com.seanmadden.deepthought.Message;
import com.seanmadden.deepthought.responders.FactoidResponder;

/**
 * [Insert class description here]
 *
 * @author Sean P Madden
 */
public abstract class FactoidCommand {
	
	protected FactoidResponder resp;

	public FactoidCommand(FactoidResponder resp){
		this.resp = resp;
	}
	
	public abstract boolean checkCommand(Message message, IRCClient client);
}
