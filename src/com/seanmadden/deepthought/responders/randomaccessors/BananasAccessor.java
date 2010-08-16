/*
 * BananasAccessor.java
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
 * [Insert class description here]
 *
 * @author Sean P Madden
 */
public class BananasAccessor implements RandomAccessor {

	/**
	 * [Place method description here]
	 *
	 * @see com.seanmadden.deepthought.responders.randomaccessors.RandomAccessor#pickAtRandom()
	 * @return
	 */
	@Override
	public String pickAtRandom() {
		return "Bananas!";
	}

}
