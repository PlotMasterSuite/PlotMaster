package org.mcsg.plotmaster

import org.bukkit.World
import org.mcsg.plotmaster.cache.Cacheable;


class Region implements Cacheable<Integer>{

	int id;	
	int x, z
	int h, w
	
	long createdAt
	
	boolean changed
	
	String world;
	String name;
	
	Map<Integer, Plot> plots = new HashMap<>();	
	
	Integer getId(){
		return id
	}

	@Override
	public boolean isStale() {
		return false;
	}
	
	
	
}
