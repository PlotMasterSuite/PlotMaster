package org.mcsg.plotmaster.managers.grid

import org.bukkit.Location;
import org.mcsg.plotmaster.Plot;
import org.mcsg.plotmaster.PlotType;
import org.mcsg.plotmaster.Region;
import org.mcsg.plotmaster.cache.Cache
import org.mcsg.plotmaster.managers.PlotManager

class GridManager extends PlotManager{

	Cache idCache;
	Cache xyCache;
	
	
	def GridManager() {
		
		
		
		
		
	}
	
	
	@Override
	public Region getRegionAt(int x, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Region getRegionAt(Location l) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Region getRegion(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Plot getPlot(int x, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Plot getPlotAt(Location l) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Plot getPlot(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean plotExist(int x, int z) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean regionExist(int x, int z) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object createPlot(int x, int y, PlotType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object createRegion(int x, int y, int h, int w) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
	
	


}
