package org.mcsg.plotmaster.cache

import org.mcsg.plotmaster.utils.SchedulerAdapter;

import groovy.transform.CompileStatic;
import java.util.concurrent.ConcurrentHashMap

@CompileStatic
class MemCache<K, V extends Cacheable<K>> implements Cache{
	Map<K, V> cache;
	Map<K, Long> last;
	
	int cullPeriod;
	int cullTime;
	int softSize;
	int hardSize;
	
	int hits, misses
	
	def MemCache(int cullPeriod, int cullTime, int softSize, int hardSize){
		cache = new ConcurrentHashMap<>()
		last = new ConcurrentHashMap<>()
		
		this.cullPeriod = cullPeriod;
		this.cullTime = cullTime * 1000;
		this.softSize = softSize;
		this.hardSize = hardSize;
		
		cullProccessor()
	}
	
	
	boolean contains(K id){
		cache.containsKey(id)
	}

	V get(K id){
		def val = cache.get(id);
		if(val) {
			last.put(id, System.currentTimeMillis())
			hits++
		} else {
			misses++
		}
		return val
	}
	
	
	void cache(K id, V value){
		cache.put(id, value)
		last.put(id, System.currentTimeMillis())
	}
	
	 private cullProccessor(){
		 SchedulerAdapter.asyncRepeating(cullPeriod, cullPeriod) {
			 cache = cache.findAll {
				 if(it.value) {
				 	def val = !(it.value.isStale() 
						 && (System.currentTimeMillis() > last.get(it) + cullTime || cache.size() > hardSize)
						 && cache.size() > softSize)
					if(!val) {
						 last.remove(it)
						 println "removing ${it.toString()} from cache"
					}
					return val
				 }
				 else
					return false
			 }
		 }
	 }
	 
	 
	 int size() {
		 return cache.size()
	 }
	 
	 
	 int request() {
		 return misses + hits
	 }
	 
	 int hits() {
		 return hits
	 }
	 
	 int misses() {
		 return misses
	 }
}
