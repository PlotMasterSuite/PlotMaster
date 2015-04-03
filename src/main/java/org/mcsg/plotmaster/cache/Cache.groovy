package org.mcsg.plotmaster.cache

import org.mcsg.plotmaster.utils.Callback


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
	  * Retrieve a value from this cache. If it doesn't exist, run the callback 
	  * to create it. Insert callback value into cache and return
	  * @param id
	  * @param backup
	  * @return
	  */
	 V get(K id, Callback backup)
	 
	 
	 
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
