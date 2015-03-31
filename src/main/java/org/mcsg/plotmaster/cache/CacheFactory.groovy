package org.mcsg.plotmaster.cache

class CacheFactory {
	
	
	
	/**
	 * Creates a new Cache based on the settings
	 * @return a new Cache
	 */
	static <K, V extends Cacheable>Cache createCache(){
		return new MemCache<K, V<K>>(200, 400, 3000, 5000) 
	}
	
	
	
	
	
}
