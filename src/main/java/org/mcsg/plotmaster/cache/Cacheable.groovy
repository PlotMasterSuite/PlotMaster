package org.mcsg.plotmaster.cache

interface Cacheable<T> {

	
	/**
	 * Returns true if this item can be removed from the cache
	 * @return boolean
	 */
	boolean isStale();
	
	
	
	/**
	 * 
	 *  Get the Unique ID of this cache item
	 * 
	 * @return ID
	 */
	T getId();
	
	
	
	
}
