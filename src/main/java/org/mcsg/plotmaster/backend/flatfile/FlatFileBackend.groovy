package org.mcsg.plotmaster.backend.flatfile

import com.google.gson.Gson

import groovy.lang.Closure;
import groovy.transform.CompileStatic;

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


	Map<Integer, XZLoc> regionMap
	Map<Integer, Integer> plotMap

	Gson gson = new Gson();

	public class XZLoc {
		int x, z
	}

	public void load(String world) {
		File loc = new File(PlotMaster.getPlugin().getDataFolder(), Settings.config.backend.flatfile.location);

		if(!loc.exists())
			loc.mkdirs()

		folder = new File(loc, world)
		if(!folder.exists())
			folder.mkdirs()

		regionMapFile = new File(folder, "regionmap.json")
		regionMapFile.createNewFile()

		regionMap = gson.fromJson(regionMapFile.getText(), Map.class)

		plotMapFile = new File(folder, "plotmap.json")
		plotMapFile.createNewFile()

		plotMap = gson.fromJson(regionMapFile.getText(), Map.class)

	}

	public Region getRegion(int id) {
		XZLoc loc = regionMap.get(id.toString())

		if(loc == null){
			return null
		}

		return getRegionByLocation(loc.x, id)
	}

	public Region getRegionByLocation(int x, int z) {
		def file = new File(regionFolder, "${x}.${z}.rg")

		if(!file)
			return null

		gson.fromJson(file.getText(), Region.class)
	}

	public void saveRegion(Region region) {
		assert region != null, "Region cannot be null"
		
		def file = new File(regionFolder, "${region.x}.${region.z}.rg")
		file.createNewFile()
		file.setText(gson.toJson(region))
	}

	public Plot getPlot(int id) {
		XZLoc loc = plotMap.get(id.toString())
		
		if(!loc)
			return null
			
		Region region = getRegionByLocation(loc.x, loc.z)
		
		return region.plots.get(id)
	}

	public Region createRegion(int x, int y) {
		// TODO Auto-generated method stub

	}

	public Plot createPlot(Region region, int x, int y, PlotType type) {
		// TODO Auto-generated method stub

	}



}
