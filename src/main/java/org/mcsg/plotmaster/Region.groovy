package org.mcsg.plotmaster

import org.bukkit.World
import org.mcsg.plotmaster.cache.Cacheable;


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
		return false;
	}
	
	
	
}
