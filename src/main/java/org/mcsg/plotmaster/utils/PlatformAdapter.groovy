package org.mcsg.plotmaster.utils

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.block.Block;
import org.bukkit.event.Event
import org.mcsg.plotmaster.bridge.PMLocation
import org.mcsg.plotmaster.bridge.PMPlayer
import org.mcsg.plotmaster.events.Cancellable
import org.mcsg.plotmaster.events.PlotCreationEvent;
import org.mcsg.plotmaster.events.RegionCreationEvent
import org.mcsg.plotmaster.events.RegionLoadEvent;
import org.mcsg.plotmaster.events.RegionUnloadEvent;
import org.mcsg.plotmaster.schematic.SchematicBlock;

import groovy.lang.Closure;
import groovy.transform.CompileStatic;
import bukkit.org.mcsg.plotmaster.bridge.BukkitLocation
import bukkit.org.mcsg.plotmaster.bridge.BukkitPlayer;
import bukkit.org.mcsg.plotmaster.util.BukkitBlockUpdate;

@CompileStatic
class PlatformAdapter {
	
	enum PlatformType {
		NONE, BUKKIT
	}
	
	static PlatformType platform = PlatformType.NONE
	
	static BlockUpdate createBlockUpdate(String world, int x, int y, int z, String material, byte data){
		if(platform == PlatformType.BUKKIT){
			return new BukkitBlockUpdate(world, x, y, z, material, data)
		}
	}
	
	static SchematicBlock toSchematicBlock(String world, int x, int y, int z){
		if(platform == PlatformType.BUKKIT){
			Block b = new Location(Bukkit.getWorld(world), x, y, z).getBlock()
			
			return new SchematicBlock(material: b.getType().toString(), data: b.getData())
		}
		
	}
	
	static PMLocation toLocation(String world, int x, int y, int z){
		if(platform == PlatformType.BUKKIT){
			return new BukkitLocation(new Location(Bukkit.getWorld(world), x, y, z))
		}
	}
	
	static PMPlayer getPlayer(String name){
		if(platform == PlatformType.BUKKIT){
			return new BukkitPlayer(Bukkit.getPlayer(name))
		}
	}
	
	static PMPlayer getPlayerByUUID(String uuid){
		if(platform == PlatformType.BUKKIT){
			return new BukkitPlayer(Bukkit.getPlayer(UUID.fromString(uuid)))
		}
	}
	
	
	
	static String registerUUID(String name, String uuid) {
		
	}
	
	static String getUUID(String name) {
		if(platform == PlatformType.BUKKIT) {
			String uuid = Bukkit.getPlayer(name)?.getUniqueId().toString()
			if(uuid)
				return uuid
			
			OfflinePlayer offline = Bukkit.getOfflinePlayer(name)
			if(offline && offline.hasPlayedBefore())
				return offline.getUniqueId().toString()
				
		}
		
		return null
	}
	
	
	static File getDataFolder(){
		if(platform == PlatformType.BUKKIT) {
			def file  = new File("plugins/PlotMaster")
			file.mkdirs()
			return file
		} else {
			def file = new File("PlotMaster/")
			file.mkdirs()
			return file
		}
	}
	
	static Material toMaterial(org.bukkit.Material material){
		return Material.valueOf(material.toString())
	}
	
	
}
