/*
 * Xkcdb.java
 * 
 *  $Id $
 *  Author: smadden
 *
 *    Copyright (C) 2010 Sean Madden
 *
 *    Please see the pertinent documents for licensing information.
 *
 */

package com.seanmadden.qdbs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;

public class Xkcdb {
	public String get(String id){
		try {
			String s = "http://www.xkcdb.com/?"+id;
			URL l = new URL(s);
			HttpURLConnection conn = (HttpURLConnection)l.openConnection();
			conn.connect();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuffer buf = new StringBuffer();
			while(reader.ready() || buf.length() == 0){
				buf.append(reader.readLine());
			}
			String result = buf.toString();
			Pattern p = Pattern.compile(".*<span class=\"quote\">(.*)</span>.*", Pattern.DOTALL);
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
	
	public static void main(String [] args){
		Xkcdb x = new Xkcdb();
		System.out.println(x.get("7110"));
	}
}
