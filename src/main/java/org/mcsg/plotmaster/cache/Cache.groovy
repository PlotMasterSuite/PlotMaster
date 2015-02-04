package org.mcsg.plotmaster.cache


 interface Cache<K, V extends Cacheable<K>> {
	 
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
	 def cache(K id, V value)
	 
	 
}
