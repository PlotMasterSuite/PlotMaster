package org.mcsg.plotmaster.backend.sql

import groovy.lang.Closure;

import org.mcsg.plotmaster.PlotType;
import org.mcsg.plotmaster.Region;
import org.mcsg.plotmaster.backend.Backend;

abstract class AbstractSQLBackend implements Backend{



	public void getRegion(int id, Closure c) {
		// TODO Auto-generated method stub
		
	}

	public void getRegionByLocation(int x, int z, Closure c) {
		// TODO Auto-generated method stub
		
	}

	public void saveRegion(Region region, Closure c) {
		// TODO Auto-generated method stub
		
	}

	public void getPlot(int id, Closure c) {
		// TODO Auto-generated method stub
		
	}

	public void createRegion(int x, int y, Closure c) {
		// TODO Auto-generated method stub
		
	}

	public void createPlot(Region region, int x, int y, PlotType type, Closure c) {
		// TODO Auto-generated method stub
		
	}

}
