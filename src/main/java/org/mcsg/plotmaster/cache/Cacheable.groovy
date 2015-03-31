package org.mcsg.plotmaster.cache

interface Cacheable {

	
	/**
	 * Returns true if this item can be removed from the cache
	 * @return boolean
	 */
	boolean isStale();
	
	
	

	
	
	
}
