/*
 * Shortener.java
 * 
 *  $Id $
 *  Author: smadden
 *
 *    Copyright (C) 2010 Sean Madden
 *
 *    Please see the pertinent documents for licensing information.
 *
 */

package com.seanmadden.bitly;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class implements the Bit.Ly shorten API.
 * 
 * @author Sean P Madden
 */
public class Bitly {

	private String username;
	private String apiKey;

	public Bitly(String username, String apiKey) {
		this.username = username;
		this.apiKey = apiKey;
	}

	public String shorten(String url) {

		try {
			String s = "http://api.bit.ly/v3/shorten?";
			s += "login=" + username;
			s += "&apiKey=" + apiKey;
			s += "&format=json";
			s += "&longUrl=" + URLEncoder.encode(url, "UTF-8");

			URL l = new URL(s);
			
			HttpURLConnection conn = (HttpURLConnection)l.openConnection();
			InputStream is = conn.getInputStream();
			conn.connect();
			StringBuilder bld = new StringBuilder();
			int in = 0;
			while((in = is.read()) != -1){
				bld.append((char) in);
			}
			JSONObject json = new JSONObject(bld.toString());
			int status = json.getInt("status_code");
			if(status != 200){
				return null;
			}
			JSONObject data = json.getJSONObject("data");
			return data.getString("url");
			
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
