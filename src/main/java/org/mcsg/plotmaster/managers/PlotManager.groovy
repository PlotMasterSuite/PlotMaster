package org.mcsg.plotmaster.managers

import org.bukkit.Location
import org.mcsg.plotmaster.Plot;
import org.mcsg.plotmaster.PlotMember
import org.mcsg.plotmaster.PlotType;
import org.mcsg.plotmaster.Region;
import org.mcsg.plotmaster.backend.Backend
import org.mcsg.plotmaster.bridge.PMLocation
import org.mcsg.plotmaster.bridge.PMPlayer
import org.mcsg.plotmaster.utils.Callback


abstract class PlotManager {

	Backend backend;
	String world; 
	
	def PlotManager(Backend backend, String world){
		this.backend = backend;
		this.world = world;
	}
	
	
	abstract void load(Map settings)
	
	abstract Map getSettings()
	
	// For all methods, if Closure is not null, will run async with the Closure being the callback
	
	abstract Region getRegionAt(int x, int z, Callback c)
	
	abstract Region getRegion(int id, Callback c)
	
	abstract Plot getPlotAt(int x, int z, Callback c)
	
	abstract Plot getPlotAt(PMLocation location, Callback c)
	
	abstract Plot getPlot(int id, Callback c)
	
	abstract boolean plotExist(int x, int z, Callback c)
	
	abstract boolean regionExist(int x, int z, Callback c)
	
	abstract PlotCreation createPlot(int x, int y, PlotType type, Callback c)
	
	abstract PlotCreation createPlot(PMPlayer player, int x, int y, PlotType type, Callback c)
	
	abstract void saveRegion(Region plot, Callback c)
	
	abstract void savePlot(Plot plot, Callback c)
	
	abstract void deleteRegion(Region region, Callback c)
	
	abstract void deletePlot(Plot plot, Callback c)
	
	abstract RegionCreation createRegion(int x, int y, int h, int w, Callback c)
	
	abstract PlotMember getPlotMember(String uuid, Callback c)
	
	abstract PlotMember getPlotMember(PMPlayer player, Callback c)
	
	abstract PlotMember savePlotMember(PlotMember member, Callback c)
	
	/**
	 * Checks if a player can modify this location or not
	 * @param player - the player in question
	 * @param location - the location
	 * @return true if the player is able to modifiy this location
	 */
	abstract boolean canModifyLocation(PMPlayer player, PMLocation location)
	
	/**
	 * Checks if a player can enter this location. Please note, this ONLY checks the access level of the player 
	 * to the plot in question, and whatever the default access mode is. This will NOT check for per-plot settings
	 * such as specific plot's setting their access level. This must be done with the PlayerEnterPlot event
	 * 
	 * @param player the player
	 * @param location the location 
	 * @return if the player is able to enter the plot
	 */
	abstract boolean canEnterLocation(PMPlayer player, PMLocation location)
	
	/**
	 * Checks if a player has entered a region
	 * @param player
	 * @param c
	 * @return
	 */
	abstract boolean isInRegion(PMPlayer player, Callback c)
	
	/**
	 * Checks if a player is in a plot. Please note, it is not a good idea to call this often, as it will
	 * force load of the plot every time its called. IF you need to call it often, please consider calling
	 * {@link #isInPlotFast(PMPlayer, Callback) isInPlotFast} as this will attempt to not load the plot if
	 * not needed 
	 * @param player the player 
	 * @param c
	 * @return
	 */
	abstract boolean isInPlot(PMPlayer player, Callback c)
	
	/**
	 * Checks if a player is in a plot. However, unlike {@link #isInPlot(PMPlayer, Callback) isInPlot}, this method will attempt
	 * to NOT load the plot if unneeded. This means that this method may not always return true even if the location is in a plot.
	 * 
	 * The reason for this is, if a player is flying quick over a bunch of plots fast, constantly calling isInPlot will trigger a 
	 * large number of unneded plot loads. This method will attempt to check if the player is actually going to be staying in the plot
	 * before tiggering the plot load.
	 * 
	 * @param player
	 * @param c
	 * @return
	 */
	abstract boolean isInPlotFast(PMPlayer player, Callback c)
	
	
	
	abstract void playerOffline(PMPlayer player)
	
	abstract void playerOnline(PMPlayer player)
}
