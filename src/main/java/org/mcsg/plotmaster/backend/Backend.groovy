package org.mcsg.plotmaster.backend

import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.PlotType;
import org.mcsg.plotmaster.Region

interface Backend {

	 
	
	
	void load(String world);
	
	Region getRegion(int id)
	Region getRegionByLocation(int x, int z)
	Region getRegionByPlotId(int id)	
	
    void saveRegion(Region region)
	
	Plot getPlot(int id)
	
	
	Region createRegion(String world, int x, int z, int h, int w)
	Plot createPlot(Region region, int x, int y, int h, int w, PlotType type)
}
