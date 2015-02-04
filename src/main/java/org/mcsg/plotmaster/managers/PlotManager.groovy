package org.mcsg.plotmaster.managers

import org.bukkit.Location
import org.mcsg.plotmaster.Plot;
import org.mcsg.plotmaster.PlotType;
import org.mcsg.plotmaster.Region;
import org.mcsg.plotmaster.backend.Backend

abstract class PlotManager {

	Backend backend;
	
	def PlotManager(){
		
	}
	
	abstract Region getRegionAt(int x, int z)
	
	abstract Region getRegionAt(Location l)
	
	abstract Region getRegion(int id)
	
	abstract Plot getPlot(int x, int z)
	
	abstract Plot getPlotAt(Location l)
	
	abstract Plot getPlot(int id)
	
	abstract boolean plotExist(int x, int z)
	
	abstract boolean regionExist(int x, int z)
	
	abstract createPlot(int x, int y, PlotType type)
	
	abstract createRegion(int x, int y, int h, int w)
	
}
