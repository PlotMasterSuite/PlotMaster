package org.mcsg.plotmaster.backend

import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.PlotMember
import org.mcsg.plotmaster.PlotType;
import org.mcsg.plotmaster.Region

interface Backend {

	 
	
	/**
	 * Initialize the backend
	 * @param world
	 */
	void load(String world);
	
	////////////////////////////////// REGIONS ///////////////////////////////////
	
	/**
	 * Load the region identified by the given ID
	 * @param id
	 * @return
	 */
	Region getRegion(int id)
	
	
	/**
	 * Load the region identifed by the given Location coords (x,y)
	 * 
	 * 
	 * These may be different depending on the Plot Manger type. 
	 * UnboundLayout will use the x/z coordinate of their location in the world
	 * while GridManger will use the grid location
	 * 
	 * 
	 * @param x
	 * @param z
	 * @return
	 */
	Region getRegionByLocation(int x, int z)
	
	/**
	 * Loads a region from the given plot ID
	 * @param id
	 * @return
	 */
	Region getRegionByPlotId(int id)	
	
	/**
	 * Saves the region provided. Will also save all plots associated with this region
	 * @param region
	 */
    void saveRegion(Region region)
	
	/**
	 * Creates a new region from the given paramters, and sets it up on the backend
	 * @param world
	 * @param x
	 * @param z
	 * @param h
	 * @param w
	 * @return
	 */
	Region createRegion(String world, int x, int z, int h, int w)
	
	
	///////////////////////////////////// PLOTS ////////////////////////////////
	
	/**
	 * Loads a plot from the given plot ID. 
	 * @param id
	 * @return
	 */
	Plot getPlot(int id)
	
	
	/**
	 * Creates a plot from the given plot information, and assigns it to the region provided
	 * @param region
	 * @param x
	 * @param y
	 * @param h
	 * @param w
	 * @param type
	 * @return
	 */
	Plot createPlot(Region region, int x, int y, int h, int w, PlotType type)
	
	///////////////////////////////////// MEMBERS //////////////////////////////
	
	/**
	 * Loads a members information from the given UUID
	 * @param uuid
	 * @return
	 */
	PlotMember getMemeber(String uuid)
	
	/**
	 * Saves a given members information
	 * @param member
	 */
	void saveMember(PlotMember member)
	
	
}
