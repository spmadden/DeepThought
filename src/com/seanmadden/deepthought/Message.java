/*
 * Message.java
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
 * This class represents a single message To/From an irc server.
 * 
 * @author Sean P Madden
 */
public class Message {
	private String usermask = "";
	private String method = "";
	private String message = "";
	private String target = "";

	/**
	 * Makes a message
	 * 
	 * @param usermask
	 * @param method
	 * @param message
	 * @param target
	 */
	public Message(String message, String target) {
		this("", "PRIVMSG", message, target);
		
	}
	
	public Message(String method, String message, String target){
		this("", method, message, target);
	}
	public Message(String usermask, String method, String message, String target){
		this.usermask = usermask;
		this.method = method;
		this.message = message;
		this.target = target;
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
	 * @param usermask
	 *            the usermask to set
	 */
	public void setUsermask(String usermask) {
		this.usermask = usermask;
	}

	/**
	 * Returns the method
	 * 
	 * @return method the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Sets the method
	 * 
	 * @param method
	 *            the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * Returns the message
	 * 
	 * @return message the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message
	 * 
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Returns the target
	 * 
	 * @return target the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * Sets the target
	 * 
	 * @param target
	 *            the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * Makes a message!
	 * 
	 * @see java.lang.Object#toString()
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(this.method);
		builder.append(" ");
		builder.append(this.target);
		if (!this.message.equals("")) {
			builder.append(" :");
			builder.append(this.message);
		}
		builder.append("\r\n");

		return builder.toString();
	}

	public static Message fromString(String message) {
		String user = "", method = "", target = "", msg = "";

		if (message.contains("PRIVMSG")) {
			String[] arr = message.split(":", 3);
			msg = arr[2];
			arr = arr[1].split(" ");
			user = arr[0].split("!")[0];
			method = arr[1];
			target = arr[2];
		} else if (message.contains("PING")) {
			method = "PING";
			target = message.split(":")[1];
		}

		return new Message(user, method, msg, target);
	}

}
