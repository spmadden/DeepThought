/*
 * StigQuotesAccessor.java
 * 
 *  $Id $
 *  Author: smadden
 *
 *    Copyright (C) 2010 Sean Madden
 *
 *    Please see the pertinent documents for licensing information.
 *
 */

package com.seanmadden.deepthought.responders.randomaccessors;
/**
 * Picks a random RSS item from the Stig Quotes Database
 * http://api.twitter.com/1/statuses/user_timeline.json?user_id=19551694&trim_user=true"
 *
 * @author Sean P Madden
 */
public class StigQuotesAccessor extends TwitterAccessor {

	protected int getUserId() {
		return 19551694;
	}


}
