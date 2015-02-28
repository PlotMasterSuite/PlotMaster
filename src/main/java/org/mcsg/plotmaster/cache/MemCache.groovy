package org.mcsg.plotmaster.cache

import org.mcsg.plotmaster.utils.SchedulerAdapter;

import groovy.transform.CompileStatic;

@CompileStatic
class MemCache<K, V extends Cacheable<K>> implements Cache{
	HashMap<K, V> cache;
	
	int cullPeriod;
	
	def MemCache(int cullPeriod){
		cache = new HashMap<>();
		this.cullPeriod = cullPeriod;
		
		cullProccessor()
	}
	
	
	boolean contains(K id){
		cache.containsKey(id)
	}

	V get(K id){
		cache.get(id);
	}
	
	
	def cache(K id, V value){
		cache.put(id, value)
	}
	
	 private cullProccessor(){
		 SchedulerAdapter.asyncRepeating(cullPeriod, cullPeriod) {
			 cache = cache.findAll {
				 if(it.value)
				 	!it.value.isStale()
				 else
					return false
			 }
		 }
	 }
}
