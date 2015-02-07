package org.mcsg.plotmaster.backend

import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.PlotType;
import org.mcsg.plotmaster.Region

interface Backend {

	
	
	
	void load(String world);
	
	void getRegion(int id)
	void getRegionByLocation(int x, int z)
	
    void saveRegion(Region region)
	
	void getPlot(int id)
	
	void createRegion(int x, int y)
	void createPlot(Region region, int x, int y, PlotType type)
}
