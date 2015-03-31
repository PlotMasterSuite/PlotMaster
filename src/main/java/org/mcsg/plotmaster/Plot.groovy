package org.mcsg.plotmaster

import com.google.gson.Gson

import groovy.transform.CompileStatic;

import org.mcsg.plotmaster.bridge.PMLocation
import org.mcsg.plotmaster.bridge.PMPlayer
import org.mcsg.plotmaster.cache.Cacheable;
import org.mcsg.plotmaster.events.PlotLoadEvent;
import org.mcsg.plotmaster.managers.PlotManager
import org.mcsg.plotmaster.schematic.Border;
import org.mcsg.plotmaster.schematic.Schematic;
import org.mcsg.plotmaster.schematic.SchematicBlock
import org.mcsg.plotmaster.utils.AsyncUtils;
import org.mcsg.plotmaster.utils.BlockUpdateTask;
import org.mcsg.plotmaster.utils.Callback
import org.mcsg.plotmaster.utils.PlatformAdapter;
import org.mcsg.plotmaster.utils.TaskQueue;


class Plot implements Cacheable<Integer>{

	static transient final Gson gson = new Gson()

	enum AccessMode {
		ALLOW, DENY
	}

	int id

	String plotName = ""
	String ownerName = "", ownerUUID = ""
	String world
	int x, z, h, w

	long createdAt = System.currentTimeMillis()
	PlotType type
	AccessMode accessMode = AccessMode.ALLOW

	//eg gamemode, pvp mod etc
	Map settings = [
		gamemode: "creative",
		pvp: false,
		fly: true,
		time: "default",
		"time-progression": true,
		"clear-entities-unload" : true,
		"save-entities" : false,
		
	]

	//eg mobs on plot, time in plot, other data
	Map metadata = [:]

	transient Region region
	transient PMPlayer player
	transient PlotManager manager
	transient long loadedAt

	
	Integer getId(){
		return id
	}

	
	
	@Override
	public boolean isStale() {
		if(!player)
			player = PlatformAdapter.getPlayerByUUID(ownerUUID)

		return (player) && player.isOnline()
	}


	def getSetting(setting){
		return settings[setting]
	}

	def getMetaData(data){
		return metadata[data]
	}

	void setSetting(key, value){
		settings.put(key, value)
	}

	void setMetaData(key, value){
		metadata.put(key, value)
	}


	String settingsToJson(){
		return gson.toJson(settings)
	}

	void settingsFromJson(String json){
		this.settings = gson.fromJson(json, Map.class)
	}

	String metadataToJson(){
		return gson.toJson(metadata)
	}

	void metadataFromJson(String json){
		this.metadata = gson.fromJson(json, Map.class)
	}

	PMLocation getMax(){
		return PlatformAdapter.toLocation(world, x + h, 255, z + w)
	}
	PMLocation getMin(){
		return PlatformAdapter.toLocation(world, x, 0, z)
	}

	@CompileStatic
	public void paint(Callback c) {
		Border border = Border.load(type.border)
		Schematic schematic = Schematic.load(type.schematic)
		List updates = new ArrayList()

		for(int a = 0; a < w; a++){
			for(int b = 0; b <  h; b++){

				int top = getTop(world, a + x, b + z)

				SchematicBlock[] sblocks = null
				if(schematic)
					sblocks = schematic.getColumn(a, b)

				if(sblocks){
					int originTop = top
					sblocks.eachWithIndex { SchematicBlock block, int i->
						if(block){
							top = originTop + i
							updates.add(PlatformAdapter.createBlockUpdate(world, a + x, top, b + z, block.material, block.data as byte))
						}
					}
				}

				SchematicBlock[] bblocks = null
				if(border)
					bblocks = border.getColumnAt(a + 1, b + 1, w , h )

				if(bblocks){
					bblocks.eachWithIndex { SchematicBlock block, int i ->
						updates.add(PlatformAdapter.createBlockUpdate(world, a + x, top + i, b + z, block.material, block.data as byte))
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
		Map<Integer, String> map = [:]

		levels.each { Map m ->
			map.put(m.y as Integer, m.block as String)
		}

		println map.toMapString()

		List updates = new ArrayList()

		String material = "AIR"
		for(int y = 255; y > 0; y--){
			material = (map.get(y)) ?: material

			for(int xx = 0; xx < w; xx++){
				for(int zz = 0; zz < h; zz++){
					updates.add(PlatformAdapter.createBlockUpdate(world, xx + x, y, zz + z, material, 0 as byte))
				}
			}
		}


		BlockUpdateTask but = new BlockUpdateTask(updates, c)

		TaskQueue.addTask(but)
	}

	public void reset(Map settings, Callback c) {
		Thread.start {
			clear(settings) {
				paint(c)
			}
		}
	}

	@CompileStatic
	private int getTop(String world, int x, int z){
		int top = 32
		while(PlatformAdapter.toSchematicBlock(world, x, top,  z).material != "AIR" && top < 256){
			top += 16
		}

		while(PlatformAdapter.toSchematicBlock(world, x, top, z).material == "AIR" && top > -1){
			top--
		}

		return top + 1
	}


	public boolean isPartOf(int x, int z){
		return x >= this.x && x < this.x + this.w && z >= this.z && z < this.z + this.h
	}

	public void save(){
		manager?.savePlot(this) {}
	}



	public boolean onLoad() {
		loadedAt = System.currentTimeMillis()
		
		PlotLoadEvent e = new PlotLoadEvent(plot : this)
		PlotMaster.fireEvent(e)
		
		if(!e.isCancelled()) {
			
			
			
			
			
		}
		
		
		return e.isCancelled()
	}
	
	public boolean onUnload() {
		
	}

}
