package org.mcsg.plotmaster.managers.unbound

import java.util.Map;

import groovy.lang.Closure;

import org.bukkit.Location;
import org.mcsg.plotmaster.Plot;
import org.mcsg.plotmaster.PlotMember;
import org.mcsg.plotmaster.PlotType;
import org.mcsg.plotmaster.Region
import org.mcsg.plotmaster.bridge.PMLocation;
import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.managers.PlotCreation;
import org.mcsg.plotmaster.managers.PlotManager
import org.mcsg.plotmaster.managers.RegionCreation
import org.mcsg.plotmaster.utils.Callback;

class UnboundManager extends PlotManager{

	List<Region> regions;

	Map settings


	@Override
	public void load(Map settings) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Region getRegionAt(int x, int z, Callback c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Region getRegion(int id, Callback c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Plot getPlotAt(int x, int z, Callback c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Plot getPlotAt(PMLocation location, Callback c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Plot getPlot(int id, Callback c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean plotExist(int x, int z, Callback c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean regionExist(int x, int z, Callback c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PlotCreation createPlot(int x, int y, PlotType type, Callback c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlotCreation createPlot(PMPlayer player, int x, int y,
			PlotType type, Callback c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveRegion(Region plot, Callback c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void savePlot(Plot plot, Callback c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteRegion(Region region, Callback c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePlot(Plot plot, Callback c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RegionCreation createRegion(int x, int y, int h, int w, Callback c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlotMember getPlotMember(PMPlayer player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlotMember savePlotMember(PlotMember member) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canModifyLocation(PMPlayer player, PMLocation location) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canEnterLocation(PMPlayer player, PMLocation location) {
		// TODO Auto-generated method stub
		return false;
	}	

	@Override
	public PlotMember getPlotMember(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void playerOffline(PMPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerOnline(PMPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isInPlot(PMPlayer player, Callback c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInPlotFast(PMPlayer player, Callback c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInRegion(PMPlayer player, Callback c) {
		// TODO Auto-generated method stub
		return false;
	}


}
