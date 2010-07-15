/*
 * Chirpy.java
 * 
 *  $Id $
 *  Author: smadden
 *
 *    Copyright (C) 2010 Sean Madden
 *
 *    Please see the pertinent documents for licensing information.
 *
 */

package com.seanmadden.chirpy;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTMLEditorKit;

import org.jsoup.Jsoup;


/**
 * This class maintains an interface to the chirpy quotes system.
 *
 * @author Sean P Madden
 */
public class Chirpy extends HTMLEditorKit.ParserCallback{
	private String host;

	public Chirpy(String host){
		this.host = host;
	}
	
	public String add(String quote, String notes, String tags){
		try {
			String s = "http://" + host + "/index.cgi?action=submit";

			URL l = new URL(s);
			
			HttpURLConnection conn = (HttpURLConnection)l.openConnection();
			conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setDoOutput(true);
			conn.connect();
			OutputStream is = conn.getOutputStream();
			
			quote = URLEncoder.encode("quote", "UTF-8") + "="+ URLEncoder.encode(quote, "UTF-8");
			notes = URLEncoder.encode("notes", "UTF-8") + "="+URLEncoder.encode(notes, "UTF-8");
			tags = URLEncoder.encode("tags", "UTF-8") + "="+URLEncoder.encode(tags, "UTF-8");
			
			String response =  quote;
			response += "&" + notes;
			response += "&" + tags;
			response += "\r\n";
			System.out.println(response);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(is));
			writer.write(response);
			writer.close();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while(reader.ready()){
				System.out.println(reader.readLine());
			}
			
			return "Everything's shiny Cap'm!";
			
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return "Something went wrong cap'm =(";
	}
	
	public String get(String id){
		try {
			String s = "http://" + host + "/index.cgi?id="+id;

			URL l = new URL(s);
			HttpURLConnection conn = (HttpURLConnection)l.openConnection();
			conn.connect();
						
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuffer buf = new StringBuffer();
			while(reader.ready()){
				buf.append(reader.readLine());
			}
			String result = buf.toString();
			Pattern p = Pattern.compile(".*<blockquote class=\"quote-body\">(.*)</blockquote>.*", Pattern.DOTALL);
			Matcher m = p.matcher(result);
			if(m.find()){
				String match = m.group(1).trim();
				match = match.replaceAll("<br/>","\r\n");
				match = Jsoup.parse(match).text();
				return match;
			}else{
				return "Couldn't find it cap'm";
			}
			
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return "Something went wrong cap'm";
	}
	
}
