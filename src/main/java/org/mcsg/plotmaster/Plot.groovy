package org.mcsg.plotmaster

import com.google.gson.Gson

import groovy.transform.CompileStatic;

import org.mcsg.plotmaster.bridge.PMLocation
import org.mcsg.plotmaster.bridge.PMPlayer
import org.mcsg.plotmaster.cache.Cacheable;
import org.mcsg.plotmaster.events.PlotOfflineEvent;
import org.mcsg.plotmaster.events.PlotOnlineEvent
import org.mcsg.plotmaster.managers.PlotManager
import org.mcsg.plotmaster.schematic.Border;
import org.mcsg.plotmaster.schematic.Schematic;
import org.mcsg.plotmaster.schematic.SchematicBlock
import org.mcsg.plotmaster.utils.AsyncUtils;
import org.mcsg.plotmaster.utils.BlockUpdateTask;
import org.mcsg.plotmaster.utils.Callback
import org.mcsg.plotmaster.utils.PlatformAdapter;
import org.mcsg.plotmaster.utils.TaskQueue;

@CompileStatic
class Plot implements Cacheable{
	
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
	Map<String, AccessLevel> accessMap
	
	//eg gamemode, pvp mod etc
	Map settings = [
		gamemode: "creative",
		pvp: false,
		fly: true,
		time: "default",
		"time-progression": true,
		"clear-entities-unload" : true,
		"save-entities" : true,
	]
	
	//eg mobs on plot, time in plot, other data
	Map metadata = [:]
	
	transient Region region
	transient PMPlayer player
	transient PlotManager manager
	transient long loadedAt
	transient int online
	
	
	@Override
	public boolean isStale() {
		return isOnline()
	}
	
	/**
	 * @return true if members of this plot are online
	 */
	public boolean isOnline() {
		return online != 0
	}
	
	/**
	 * Returns the settings specified for this plot
	 */
	def getSetting(setting){
		return settings[setting]
	}
	
	def getSetting(setting, defaultval) {
		return setting.get(setting) ?: defaultval
	}
	
	/**
	 * returns the metadata specified for this plot
	 */
	def getMetaData(data){
		return metadata[data]
	}
	
	/**
	 * Set a setting for this plot
	 * @param key
	 * @param value
	 * 
	 * @return this
	 */
	Plot setSetting(key, value){
		settings.put(key, value)
		return this
	}
	
	/**
	 * Set metadata for this plot
	 * @param key
	 * @param value
	 * 
	 * @return this
	 */
	Plot setMetaData(key, value){
		metadata.put(key, value)
		return this
	}
	
	/**
	 * Internal use. Return the settings for this plot is JSON form
	 * @return
	 */
	String settingsToJson(){
		return gson.toJson(settings)
	}
	
	/**
	 * Internal use. Set the settings for this plot in json form
	 * @param json
	 */
	void settingsFromJson(String json){
		this.settings = gson.fromJson(json, Map.class)
	}
	
	/**
	 * Internal use, return the metadata for htis plot in Json form
	 * @return
	 */
	String metadataToJson(){
		return gson.toJson(metadata)
	}
	
	/**
	 * Internal use, set the metadata for htis plot in json form
	 * 
	 * @param json
	 */
	void metadataFromJson(String json){
		this.metadata = gson.fromJson(json, Map.class)
	}
	
	/**
	 * Gets the max location for this plot. Y = 255
	 * @return
	 */
	PMLocation getMax(){
		return PlatformAdapter.toLocation(world, x + h - 1, 255, z + w - 1)
	}
	
	/**
	 * Gest the min location for htis plot, y = 0
	 * @return
	 */
	PMLocation getMin(){
		return PlatformAdapter.toLocation(world, x, 0, z)
	}
	
	PMLocation getCenter() {
		def cx = x + w / 2
		def cz = z + h / 2
		
		def cy = getTop(world, cx, cz) + 1
		
		return PlatformAdapter.toLocation(world, cx, cy, cz)
	}
	
	/**
	 * Internal use. Sets a member of this plot as offline. 
	 * @param member
	 * @return
	 */
	def memberOffline(PlotMember member) {
		if(member) {
			online--
			if(online == 0){
				PlotOfflineEvent e = new PlotOfflineEvent(plot: this)
				PlotMaster.getInstance().fireEvent(e)
			}
		}
	}
	
	/**
	 * Internal use. Set a member of this plot as online
	 * @param member
	 * @return
	 */
	def memberOnline(PlotMember member) {
		if(member) {
			if(online == 0){
				PlotOnlineEvent e = new PlotOnlineEvent(plot: this)
				PlotMaster.getInstance().fireEvent(e)
			}
			online++
		}
	}
	
	/**
	 * Paints this plot. Draws the borders and schematics for this plot
	 * 
	 * @param c
	 */
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
	
	/**
	 * Clears everything from this plot and resets it to the base generator
	 * @param settings
	 * @param c
	 */
	@CompileStatic
	public void clear(Map settings, Callback c) {
		List<Map> levels = (List<Map>)(settings.get("generator") as Map).get("levels")
		Map<Integer, String> map = [:]
		
		levels.each { Map m ->
			map.put(m.y as Integer, m.block as String)
		}
				
		List updates = new ArrayList()
		
		def values = [:]
		
		String material = "AIR"
		def cur = "AIR"
		def air = 0
		for(int y = 255; y > 0; y--){ //we want to go from bottom to top
			if(map.get(y)) {          //but teh definition is top to bottom
				values.put(y + 1, cur)
				if(cur == "AIR")
					air = y + 1
				cur = map.get(y)
			}
		}
		
		values.put(0, cur)
		
		//clear air top to bottom. Fixes some client lag issues. Also cleans up liquids correctly
		for(int y = 255; y >= air; y--){			
			for(int xx = 0; xx < w; xx++){
				for(int zz = 0; zz < h; zz++){
					updates.add(PlatformAdapter.createBlockUpdate(world, xx + x, y, zz + z, "AIR", 0 as byte))
				}
			}
		}
		
		for(int y = 0; y < air; y++){
			material = (values.get(y)) ?: material
			
			for(int xx = 0; xx < w; xx++){
				for(int zz = 0; zz < h; zz++){
					updates.add(PlatformAdapter.createBlockUpdate(world, xx + x, y, zz + z, material, 0 as byte))
				}
			}
		}
		
		
		BlockUpdateTask but = new BlockUpdateTask(updates, c)
		
		TaskQueue.addTask(but)
	}
	
	/**
	 * Clears, then paints this plot
	 * @param settings
	 * @param c
	 */
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
	
	/**
	 * Returns true if this x,z loc is part of this plot
	 */
	public boolean isPartOf(int x, int z){
		return x >= this.x && x < this.x + this.w && z >= this.z && z < this.z + this.h
	}
	
	/**
	 * Saves this plots data 
	 */
	public void save(){
		manager?.savePlot(this) {}
	}
	
}
