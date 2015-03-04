package org.mcsg.plotmaster

import groovy.transform.CompileStatic;

import org.mcsg.plotmaster.bridge.PMPlayer
import org.mcsg.plotmaster.cache.Cacheable;
import org.mcsg.plotmaster.schematic.Border;
import org.mcsg.plotmaster.schematic.SchematicBlock
import org.mcsg.plotmaster.utils.BlockUpdateTask;
import org.mcsg.plotmaster.utils.Callback
import org.mcsg.plotmaster.utils.PlatformAdapter;
import org.mcsg.plotmaster.utils.TaskQueue;


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

	@CompileStatic
	public void paint(Callback c) {
		Border border = Border.load(type.border)
		List updates = new ArrayList()
		
		for(int a = 0; a <= w; a++){
			for(int b = 0; b <=  h; b++){

				int top = 255
				
				while(PlatformAdapter.toSchematicBlock(world, a + x, top, b + z).material == "AIR"){
					top--
				}

				SchematicBlock[] blocks = border.getColumnAt(a, b, w, h)

				if(blocks){
					blocks.eachWithIndex { SchematicBlock block, int i ->
						updates.add(PlatformAdapter.createBlockUpdate(world, a + x, top + i, b + z, block.material, block.data as byte))
						println "Adding ${block.material} at ${a + x}, ${top + i}, ${b + z}"
					}
				}
			}
		}
		
		
		def but = new BlockUpdateTask(updates, c)

		TaskQueue.addTask(but)
	}

	@CompileStatic
	public void clear(Map settings, Callback c) {
		List<Map> levels = (List<Map>)(settings.get("generator") as Map).get("levels")
		Map map = [:]

		levels.each { Map m ->
			map.put(m.y, m.block)
		}
		
		
		List updates = new ArrayList()

		String material = "AIR"
		for(int y = 255; y > 0; y--){
			material = (map.get(y)) ?: material

			for(int xx = 0; xx < w; xx++){
				for(int zz = 0; zz < h; zz++){
					updates.add(PlatformAdapter.createBlockUpdate(world, xx, y, zz, material, 0 as byte))
				}
			}
			
		}

		
		BlockUpdateTask but = new BlockUpdateTask(updates, c)

		TaskQueue.addTask(but)
		
	}

	public void reset(Map settings, Callback c) {
		clear(settings) {
			paint(c)
		}
	}
}
