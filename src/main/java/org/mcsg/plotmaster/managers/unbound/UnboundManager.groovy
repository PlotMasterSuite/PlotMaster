package org.mcsg.plotmaster.managers.unbound

import groovy.lang.Closure;

import org.bukkit.Location;
import org.mcsg.plotmaster.Plot;
import org.mcsg.plotmaster.PlotType;
import org.mcsg.plotmaster.Region
import org.mcsg.plotmaster.managers.PlotManager

class UnboundManager extends PlotManager{

	List<Region> regions;


	@Override
	public Region getRegionAt(int x, int z, Closure c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Region getRegion(int id, Closure c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Plot getPlot(int x, int z, Closure c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Plot getPlot(int id, Closure c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean plotExist(int x, int z, Closure c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean regionExist(int x, int z, Closure c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPlot(int x, int y, PlotType type, Closure c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createRegion(int x, int y, int h, int w, Closure c) {
		// TODO Auto-generated method stub
		
	}
	
	
}
