package org.mcsg.plotmaster

import org.mcsg.plotmaster.bridge.PMPlayer
import org.mcsg.plotmaster.cache.Cacheable;
import org.mcsg.plotmaster.utils.PlatformAdapter;


class Plot implements Cacheable<Integer>{

	int id
	
	String plotName
	String ownerName, OwnerUUID
	int x, z, h, w
	
	long createdAt	
	PlotType type
	
	List<String> members
	List<String> deny

	
	transient boolean changed
	transient Region region
	transient PMPlayer player
	
	
	Integer getId(){
		return id
	}


	@Override
	public boolean isStale() {
		if(!player)
			player = PlatformAdapter.getPlayerByUUID(OwnerUUID)
		
		return player.isOnline()
		
	}
}
