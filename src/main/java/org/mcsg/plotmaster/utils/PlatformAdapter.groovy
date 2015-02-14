package org.mcsg.plotmaster.utils

import java.io.File;

import groovy.lang.Closure;
import bukkit.org.mcsg.plotmaster.util.BukkitBlockUpdate;

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

	static File getDataFolder(){
		if(platform == PlatformType.BUKKIT) {
			def file  = new File("plugins/PlotMaster")
			file.mkdirs()
			return file
		}
	}






}
