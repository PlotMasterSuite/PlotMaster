package org.mcsg.plotmaster.cache

import org.mcsg.plotmaster.utils.NullCachable;
import org.mcsg.plotmaster.utils.SchedulerAdapter;

import groovy.transform.CompileStatic;

import java.util.concurrent.ConcurrentHashMap

@CompileStatic
class MemCache<K, V extends Cacheable> implements Cache{
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
		return cache.containsKey(id)
	}
	
	V get(K id){
		def val = cache.get(id);
		if(val) {
			last.put(id, System.currentTimeMillis())
			hits++
		} else {
			misses++
		}
		return (val instanceof NullCachable) ? null : val
	}
	
	
	void cache(K id, V value){
		assert id != null, "Id cannot be null!"
				
		if(value == null) {
			value = new NullCachable()
		}
		
		cache.put(id, value)
		last.put(id, System.currentTimeMillis())
	}
	
	private cullProccessor(){
		SchedulerAdapter.asyncRepeating(cullPeriod, cullPeriod) {
			cache = cache.findAll {
				if(it) {
					def val = !(it.value.isStale()
					&& (System.currentTimeMillis() > last.get(it.key) + cullTime || cache.size() > hardSize)
					&& cache.size() > softSize)
					if(!val) {
						last.remove(it)
					}
					return val
				} else {
					return false
				}
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
