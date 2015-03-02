package org.mcsg.plotmaster

import org.mcsg.plotmaster.bridge.PMPlayer
import org.mcsg.plotmaster.cache.Cacheable;
import org.mcsg.plotmaster.utils.BlockUpdateTask;
import org.mcsg.plotmaster.utils.Callback
import org.mcsg.plotmaster.utils.PlatformAdapter;


class Plot implements Cacheable<Integer>{

	int id
	
	String plotName
	String ownerName, OwnerUUID
	String world
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
	
	public void paint() {
		
	}
	
	public void clear(List<Map> levels, Callback c) {
		Map map = [:]
		
		levels.each { Map m ->
			map.put(m.y, m.block)
		}
		
		List updates = new ArrayList()
		
		
		
		String material = "AIR" 
		for(int y = 255; y > 0; y--){
			material = (map.get(y)) ?: material
			
			for(int xx = 0; xx < w; x++){
				for(int zz = 0; zz < h; z++){
					updates.add(PlatformAdapter.createBlockUpdate(world, xx, y, zz, material, 0))
				}	
			}
		}
		
		BlockUpdateTask but = new BlockUpdateTask(updates, c)
		
		
	}
	
	public void reset(List<Map> levels) {
		
	}
}
