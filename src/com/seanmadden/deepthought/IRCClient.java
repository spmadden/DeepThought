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
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	private String server = "irc.thesse.org";
	private int port = 6667;
	private String realname = "ImmaBOT";
	private String host = "LOCALHOST...Duh";
	private String username = "Bottastic";
	private String nick = "DeepThought";
	private String identpass = "pdntspa";
	private Vector<String> channels = new Vector<String>();
	
	private static Pattern MODES = Pattern.compile("([+-].) (.+)");

	private LinkedList<Message> messageHistory = new LinkedList<Message>();
	private Hashtable<String, List<MessageObserver>> calls = new Hashtable<String, List<MessageObserver>>();
	private Hashtable<String, Hashtable<String, User>> users = new Hashtable<String, Hashtable<String, User>>();
	private ExecutorService exec = Executors.newCachedThreadPool();
	private boolean run = true;

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

		Message nick = new Message("NICK", "", this.nick);
		Message user = new Message("USER", "", this.username + " " + this.host
				+ " " + this.server + " " + this.realname);
		Message ident = new Message("identify " + identpass, "nickserv");
		this.sendMessage(nick);
		this.sendMessage(user);
		this.sendMessage(ident);

		for (String channel : this.channels) {
			Message chan = new Message("JOIN", "", channel);
			this.sendMessage(chan);
			users.put(channel, new Hashtable<String, User>());
		}

		while (run && sock.isConnected()) {
			try {
				String message = reader.readLine();
				log.debug(message.trim());
				final Message m = Message.fromString(message);
				if (m.getMethod().equals("PRIVMSG")) {
					User u = users.get(m.getTarget()).get(m.getNick());
					if(u == null){
						u = new User(m.getNick(), m.getUsermask());
						users.get(m.getTarget()).put(m.getNick(), u);
					}
					m.setUser(u);
					synchronized (this) {
						// notify the user object we got a message
						u.gotMessage(m);
						if(u.isSpamming() && !u.isOpper()){
							log.info(u.getNick() + " IS SPAMMING!");
							Message msg = new Message("KICK", "SPAMMER!", m.getTarget() + " " + m.getNick());
							this.sendMessage(msg);
							users.get(m.getTarget()).remove(u);
							continue;
						}

						// append the message to the local history
						messageHistory.push(m);
						while (messageHistory.size() > 50) {
							messageHistory.removeLast();
						}
					}
				}else if(m.getMethod().equals("MODE")){
					String msg = m.getMessage().trim();
					Matcher matcher = MODES.matcher(msg);
					if(!matcher.matches()){
						continue;
					}
					User u = users.get(m.getTarget()).get(matcher.group(2));
					if(matcher.group(1).equals("-o")){
						// remove opper.
						log.info(matcher.group(2) + " no longer has OPPER status.");
						u.setOpper(false);
					}else if(matcher.group(1).equals("+o")){
						// set opper
						log.info(matcher.group(2) + " now has OPPER status.");
						u.setOpper(true);
					}
				} else if (m.getMethod().equals("JOIN")) {
					// process a JOIN command
					// make sure we don't process ourselves.
					if (m.getNick().equals(this.getNick())) {
						continue;
					}
					log.info(m.getNick() + " HAS JOINED " + m.getTarget());
					users.get(m.getTarget()).put(m.getNick(),
							new User(m.getNick(), ""));
				} else if (m.getMethod().equals("NAMES")) {
					// process a NAMES command
					String[] users = m.getNick().split(" ");
					for (String s : users) {
						User u = new User(s, "");
						if(s.startsWith("@")){
							u.setOpper(true);
							s = s.replaceAll("@", "");
							u.setNick(s);
						}else if(s.startsWith("+")){
							s = s.replaceAll("\\+", "");
							u.setNick(s);
						}
						this.users.get(m.getTarget()).put(s, u);
					}
					System.out.println(this.users);
				} else if (m.getMethod().equals("PART")) {
					// process a PART command
					String s = m.getNick();
					if(s.startsWith("@")){
						s = s.replaceAll("@", "");
					}else if(s.startsWith("+")){
						s = s.replaceAll("\\+", "");
					}
					log.info(m.getNick() + " HAS LEFT " + m.getTarget());
					Hashtable<String, User> tb = users.get(m.getTarget());
					if(tb != null){
						tb.remove(s);
					}
				} else if (m.getMethod().equals("QUIT")) {
					log.info(m.getNick() + " HAS LEFT THE BUILDING");
					// iterate over all users and remove those with the same
					// nick within the same channel
					this.removeUser(m.getNick());
				}else if(m.getMethod().equals("NICK")){
					log.info(m.getNick() + " has NICK'd to " + m.getTarget());
					this.changeNick(m.getNick(), m.getTarget());
				}
				// if a caller has registered for this type of message, notify
				// them in their own thread.
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
	
	public void joinChannel(String channel){
		Message chan = new Message("JOIN", "", channel);
		this.sendMessage(chan);
		users.put(channel, new Hashtable<String, User>());
		channels.add(channel);
	}
	
	public void partChannel(String channel){
		Message chan = new Message("PART", "", channel);
		this.sendMessage(chan);
		users.remove(channel);
		channels.remove(channel);
	}
	
	private void removeUser(String nick){
		for (Map.Entry<String, Hashtable<String, User>> set : users
				.entrySet()) {
			for (Map.Entry<String, User> us : set.getValue()
					.entrySet()) {
				if (us.getValue().getNick().equals(nick)) {
					set.getValue().remove(us);
				}
			}
		}
	}
	private void changeNick(String from, String to){
		for(Map.Entry<String, Hashtable<String, User>> set: users.entrySet()){
			for(Map.Entry<String, User> us: set.getValue().entrySet()){
				if(us.getKey().equals("from")){
					User u = set.getValue().remove(from);
					u.setNick(to);
					set.getValue().put(to, u);
					break;
				}
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
			m.setUsermask(this.getNick());
			messageHistory.push(m);
		} catch (IOException e) {
			log.error(e.getMessage());
			return false;
		}
		return true;
	}

	public void addCallback(String method, MessageObserver cb) {
		if (!this.calls.contains(method)) {
			this.calls.put(method, new Vector<MessageObserver>());
		}
		this.calls.get(method).add(cb);
	}
	
	public void stopServer(){
		this.run = false;
		try {
			for (String channel : getChannels()) {
				Message m = new Message("blargh i am ded",
						channel);
				sendMessage(m);
			}
			this.sock.close();
			this.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	/**
	 * Returns the messageHistory
	 * 
	 * @return messageHistory the messageHistory
	 */
	public LinkedList<Message> getMessageHistory() {
		return messageHistory;
	}

	public Collection<User> getUsersFor(String channel) {
		return users.get(channel).values();
	}

}
