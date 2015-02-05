package org.mcsg.plotmaster.backend

import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.Region

interface Backend {

	
	
	
	void load(String world, Closure c);
	
	void getRegion(int id,Closure c)
	void getRegionByLocation(int x, int z, Closure c)
	
    void saveRegion(Region region, Closure c)
	
	void getPlot(int id, Closure c)
	
}
