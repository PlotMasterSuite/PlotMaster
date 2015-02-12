package org.mcsg.plotmaster

import org.bukkit.block.Block
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class PlotMaster{

	static Plugin plugin

	
	//TODO: remove bukkit dependency
	void onEnable(Plugin plugin){
		plugin = plugin;
	}

	void onDisable(){

	}
	
	static PlotMaster getPlugin(){
		plugin
	}


}
