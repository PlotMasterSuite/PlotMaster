package org.mcsg.plotmaster.cache


 interface Cache<K, V extends Cacheable> {
	 
	 /**
	  * Does this cache contain this item
	  * @param id
	  * @return
	  */
	 boolean contains(K id)
	 
	 /**
	  * Retrieve an item from this cache
	  * @param id
	  * @return value
	  */
	 V get(K id)
	 
	 /**
	  * Insert an item into this cache
	  * @param id
	  * @param value
	  * @return
	  */
	 void cache(K id, V value)
	 
	 	/**
	 	 * Remove from cache
	 	 * @param id
	 	 * @return
	 	 */
	 V remove(K id)
	 
	 
	 /**
	  * 
	  * @return size of this cache
	  */
	 int size()
	 
	 
	 int request()
	 
	 int hits()
	 
	 int misses()
	 
}
