package org.mcsg.plotmaster.backend.flatfile

import groovy.lang.Closure;

import org.mcsg.plotmaster.Plot;
import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.PlotType;
import org.mcsg.plotmaster.Region;
import org.mcsg.plotmaster.Settings;
import org.mcsg.plotmaster.backend.Backend
import org.mcsg.plotmaster.utils.AsyncUtils;


class FlatFileBackend implements Backend{

	File folder
	File regionMapFile
	File plotMapFile
	File regionFolder
	File userFolder


	Map regionMap
	Map plotMap


	public void load(String world) {
		File loc = new File(PlotMaster.getPlugin().getDataFolder(), Settings.config.backend.flatfile.location);
		if(!loc.exists())
			loc.mkdirs()

		folder = new File(loc, world)
		if(!folder.exists())
			folder.mkdirs()

		regionMapFile = new File(folder, "regionmap.json")
		regionMapFile.createNewFile()

		plotMapFile = new File(folder, "plotmap.json")
		plotMapFile.createNewFile()
	}

	public void getRegion(int id) {
		// TODO Auto-generated method stub

	}

	public void getRegionByLocation(int x, int z) {
		// TODO Auto-generated method stub

	}

	public void saveRegion(Region region) {
		// TODO Auto-generated method stub

	}

	public void getPlot(int id) {
		// TODO Auto-generated method stub

	}

	public void createRegion(int x, int y) {
		// TODO Auto-generated method stub

	}

	public void createPlot(Region region, int x, int y, PlotType type) {
		// TODO Auto-generated method stub

	}



}
