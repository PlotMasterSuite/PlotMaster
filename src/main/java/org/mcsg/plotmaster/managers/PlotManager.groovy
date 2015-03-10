package org.mcsg.plotmaster.managers

import org.bukkit.Location
import org.mcsg.plotmaster.Plot;
import org.mcsg.plotmaster.PlotMember
import org.mcsg.plotmaster.PlotType;
import org.mcsg.plotmaster.Region;
import org.mcsg.plotmaster.backend.Backend
import org.mcsg.plotmaster.bridge.PMPlayer
import org.mcsg.plotmaster.utils.Callback


abstract class PlotManager {

	Backend backend;
	String world; 
	
	def PlotManager(Backend backend, String world){
		this.backend = backend;
		this.world = world;
	}
	
	
	abstract void load(Map settings)
	
	abstract Map getSettings()
	
	// For all methods, if Closure is not null, will run async with the Closure being the callback
	
	abstract Region getRegionAt(int x, int z, Callback c)
	
	abstract Region getRegion(int id, Callback c)
	
	abstract Plot getPlot(int x, int z, Callback c)
	
	abstract Plot getPlot(int id, Callback c)
	
	abstract boolean plotExist(int x, int z, Callback c)
	
	abstract boolean regionExist(int x, int z, Callback c)
	
	abstract PlotCreation createPlot(int x, int y, PlotType type, Callback c)
	
	abstract PlotCreation createPlot(PMPlayer player, int x, int y, PlotType type, Callback c)
	
	abstract RegionCreation createRegion(int x, int y, int h, int w, Callback c)
	
	abstract PlotMember getPlotMemeber(String uuid)
	
	abstract PlotMember savePlotMember(PlotMember member)
	
}
