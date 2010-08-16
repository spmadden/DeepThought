/*
 * TwitterAccessor.java
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
public abstract class TwitterAccessor implements RandomAccessor {

	/**
	 * [Place method description here]
	 *
	 * @see com.seanmadden.deepthought.responders.randomaccessors.RandomAccessor#pickAtRandom()
	 * @return
	 */
	@Override
	public String pickAtRandom() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected abstract int getUserId(); 

}
