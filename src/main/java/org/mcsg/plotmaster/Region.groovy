package org.mcsg.plotmaster

import groovy.transform.CompileStatic;

import org.bukkit.World
import org.mcsg.plotmaster.cache.Cacheable;

@CompileStatic
class Region implements Cacheable{

	int id;	
	int x, z
	int h, w
	
	long createdAt = System.currentTimeMillis()
	
	boolean changed
	
	String world
	String name = ""
	
	Map<Integer, Plot> plots = new HashMap<>();	
	

	@Override
	public boolean isStale() {
		return plots.values()*.isStale();
	}
	
	
	
}
