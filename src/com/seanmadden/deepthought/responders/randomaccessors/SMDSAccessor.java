/*
 * SMDSAccessor.java
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
* ShitMyDadSays Accessor to pull a random article from the RSS feed.
* http://twitter.com/statuses/user_timeline/62581962.rss
*
* @author Sean P Madden
*/
public class SMDSAccessor extends TwitterAccessor {

	@Override
	protected int getUserId() {
		return 62581962;
	}


}
