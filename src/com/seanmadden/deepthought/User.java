/*
 * User.java
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.Timer;


/**
 * This class represents a single user on an IRC channel.
 *
 * @author Sean P Madden
 */
public class User implements ActionListener {
	private boolean isOpper = false;
	private String nick = "";
	private String usermask = "";
	
	private Timer time = null;
	private int restarts = 0;
	
	private LinkedList<Message> messages = new LinkedList<Message>();
	private boolean spamming = false;;
	
	public User(String nick, String usermask){
		this.nick = nick;
		this.usermask = usermask;
		time = new Timer(10000, this);
		time.start();
	}

	public long userLastSeen(){
		Message m =messages.peekFirst();
		if(m == null){
			return 0;
		}
		return m.getTimestamp();
	}
	
	public void gotMessage(Message m){
		this.messages.addFirst(m);
		if(++restarts >= 8){
			this.spamming = true;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		restarts = 0;
		spamming = false;
	}
	
	/**
	 * Returns the isOpper
	 *
	 * @return isOpper the isOpper
	 */
	public boolean isOpper() {
		return isOpper;
	}

	/**
	 * Sets the isOpper
	 *
	 * @param isOpper the isOpper to set
	 */
	public void setOpper(boolean isOpper) {
		this.isOpper = isOpper;
	}

	/**
	 * Returns the nick
	 *
	 * @return nick the nick
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * Sets the nick
	 *
	 * @param nick the nick to set
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * Returns the usermask
	 *
	 * @return usermask the usermask
	 */
	public String getUsermask() {
		return usermask;
	}

	/**
	 * Sets the usermask
	 *
	 * @param usermask the usermask to set
	 */
	public void setUsermask(String usermask) {
		this.usermask = usermask;
	}

	/**
	 * Returns the spamming
	 *
	 * @return spamming the spamming
	 */
	public boolean isSpamming() {
		return spamming;
	}

	/**
	 * Sets the spamming
	 *
	 * @param spamming the spamming to set
	 */
	public void setSpamming(boolean spamming) {
		this.spamming = spamming;
	}

	/**
	 * [Place method description here]
	 *
	 * @see java.lang.Object#hashCode()
	 * @return
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nick == null) ? 0 : nick.hashCode());
		result = prime * result
				+ ((usermask == null) ? 0 : usermask.hashCode());
		return result;
	}

	/**
	 * [Place method description here]
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		User other = (User) obj;
		if (nick == null) {
			if (other.nick != null) {
				return false;
			}
		} else if (!nick.equals(other.nick)) {
			return false;
		}
		if (usermask == null) {
			if (other.usermask != null) {
				return false;
			}
		} else if (!usermask.equals(other.usermask)) {
			return false;
		}
		return true;
	}
	
}
