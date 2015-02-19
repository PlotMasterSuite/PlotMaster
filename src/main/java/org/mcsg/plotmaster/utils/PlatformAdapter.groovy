package org.mcsg.plotmaster.utils

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location
import org.bukkit.block.Block;
import org.mcsg.plotmaster.schematic.SchematicBlock;

import groovy.lang.Closure;
import groovy.transform.CompileStatic;
import bukkit.org.mcsg.plotmaster.util.BukkitBlockUpdate;

@CompileStatic
class PlatformAdapter {

	enum PlatformType {
		BUKKIT
	}

	static PlatformType platform

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

	static File getDataFolder(){
		if(platform == PlatformType.BUKKIT) {
			def file  = new File("plugins/PlotMaster")
			file.mkdirs()
			return file
		}
	}






}
