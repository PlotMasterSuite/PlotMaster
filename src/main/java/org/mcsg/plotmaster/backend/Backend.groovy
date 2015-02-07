package org.mcsg.plotmaster.backend

import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.PlotType;
import org.mcsg.plotmaster.Region

interface Backend {

	 
	
	
	void load(String world);
	
	Region getRegion(int id)
	Region getRegionByLocation(int x, int z)
	
    void saveRegion(Region region)
	
	Plot getPlot(int id)
	
	Region createRegion(int x, int y)
	Plot createPlot(Region region, int x, int y, PlotType type)
}
