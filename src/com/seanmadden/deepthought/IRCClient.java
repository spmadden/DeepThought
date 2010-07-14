/*
 * IRCClient.java
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

import java.io.*;
import java.net.Socket;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

/**
 * [Insert class description here]
 * 
 * @author Sean P Madden
 */
public class IRCClient extends Thread {
	private static Logger log = Logger.getLogger(IRCClient.class);
	private Socket sock = null;

	private BufferedWriter writer = null;
	private BufferedReader reader = null;

	private String server = "irc.seanmadden.net";
	private int port = 6667;
	private String realname = "ImmaBOT";
	private String host = "LOCALHOST...Duh";
	private String username = "Bottastic";
	private String nick = "DeepThought";
	private Vector<String> channels = new Vector<String>();

	private LinkedList<Message> messageHistory = new LinkedList<Message>();
	private Hashtable<String, List<MessageObserver>> calls = new Hashtable<String, List<MessageObserver>>();

	private ExecutorService exec = Executors.newCachedThreadPool();

	public IRCClient() {

	}

	public IRCClient(String server, int port) {
		this.server = server;
		this.port = port;
	}

	public void run() {
		try {
			log.debug("Created Socket");
			sock = new Socket(server, port);
			reader = new BufferedReader(new InputStreamReader(sock
					.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(sock
					.getOutputStream()));

		} catch (Exception e) {
			log.error(e.getMessage());
			return;
		}

		Message nick = new Message("", "NICK", "", this.nick);
		Message user = new Message("", "USER", "", this.username + " "
				+ this.host + " " + this.server + " " + this.realname);
		this.sendMessage(nick);
		this.sendMessage(user);
		
		for(String channel : this.channels){
			Message chan = new Message("", "JOIN", "", channel);
			this.sendMessage(chan);
		}

		while (sock.isConnected()) {
			try {
				String message = reader.readLine();
				final Message m = Message.fromString(message);
				if (m.getMethod().equals("PRIVMSG")) {
					messageHistory.push(m);
					while (messageHistory.size() > 50) {
						messageHistory.removeLast();
					}
				}
				if (calls.containsKey(m.getMethod())) {
					for (final MessageObserver obs : calls.get(m.getMethod())) {
						exec.execute(new Runnable() {
							public void run() {
								obs.wildMessageAppeared(m);
							}
						});
					}
				}

			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}

	}

	public synchronized boolean sendMessage(Message m) {
		if (writer == null) {
			log.error("IRCClient is not opened yet.");
			return false;
		}
		try {
			log.debug("SENDING: " + m.toString());
			writer.append(m.toString());
			writer.flush();

		} catch (IOException e) {
			log.error(e.getMessage());
			return false;
		}
		return true;
	}

	public void addCallback(String method, MessageObserver cb){
		if(!this.calls.contains(method)){
			this.calls.put(method, new Vector<MessageObserver>());
		}
		this.calls.get(method).add(cb);
	}
	
	
	/**
	 * Returns the server
	 * 
	 * @return server the server
	 */
	public String getServer() {
		return server;
	}

	/**
	 * Sets the server
	 * 
	 * @param server
	 *            the server to set
	 */
	public void setServer(String server) {
		log.debug("Setting server to: " + server);
		this.server = server;
	}

	/**
	 * Returns the port
	 * 
	 * @return port the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the port
	 * 
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		log.debug("Setting port to: " + port);
		this.port = port;
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
	 * @param nick
	 *            the nick to set
	 */
	public void setNick(String nick) {
		log.debug("Setting nickname to: " + nick);
		this.nick = nick;
	}

	/**
	 * Returns the username
	 * 
	 * @return username the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username
	 * 
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		log.debug("Setting username to: " + username);
		this.username = username;
	}

	/**
	 * Returns the host
	 * 
	 * @return host the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Sets the host
	 * 
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		log.debug("Setting host to: " + host);
		this.host = host;
	}

	/**
	 * Returns the realname
	 * 
	 * @return realname the realname
	 */
	public String getRealname() {
		return realname;
	}

	/**
	 * Sets the realname
	 * 
	 * @param realname
	 *            the realname to set
	 */
	public void setRealname(String realname) {
		log.debug("Setting Realname to: " + realname);
		this.realname = realname;
	}

	/**
	 * Returns the channels
	 * 
	 * @return channels the channels
	 */
	public Collection<String> getChannels() {
		return channels;
	}

	/**
	 * Sets the channels
	 * 
	 * @param channels
	 *            the channels to set
	 */
	public void setChannels(Vector<String> channels) {
		log.debug("Setting channels to: " + channels);
		this.channels = channels;
	}

}
