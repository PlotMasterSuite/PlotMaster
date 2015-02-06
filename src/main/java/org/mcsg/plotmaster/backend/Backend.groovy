package org.mcsg.plotmaster.backend

import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.PlotType;
import org.mcsg.plotmaster.Region

interface Backend {

	
	
	
	void load(String world);
	
	void getRegion(int id,Closure c)
	void getRegionByLocation(int x, int z, Closure c)
	
    void saveRegion(Region region, Closure c)
	
	void getPlot(int id, Closure c)
	
	void createRegion(int x, int y, Closure c)
	void createPlot(Region region, int x, int y, PlotType type, Closure c)
}
