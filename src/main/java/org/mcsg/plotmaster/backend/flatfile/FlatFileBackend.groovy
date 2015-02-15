package org.mcsg.plotmaster.backend.flatfile

import com.google.gson.Gson

import groovy.lang.Closure;
import groovy.transform.CompileStatic;

import org.mcsg.plotmaster.Plot;
import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.PlotMember;
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


	TreeMap<Integer, XZLoc> regionMap
	TreeMap<Integer, Integer> plotMap

	Gson gson = new Gson();

	public class XZLoc {
		int x, z
	}

	public void load(String world, Map settings) {
		File loc = new File(PlotMaster.getInstance().getDataFolder(), Settings.config.backend.flatfile.location);
		loc.mkdirs()

		folder = new File(loc, world)
		folder.mkdirs()

		regionFolder = new File(folder, "regions/")
		regionFolder.mkdirs()

		userFolder = new File(folder, "users/")
		userFolder.mkdirs()

		regionMapFile = new File(folder, "regionmap.json")
		regionMapFile.createNewFile()

		regionMap = gson.fromJson(regionMapFile.getText(), TreeMap.class)

		plotMapFile = new File(folder, "plotmap.json")
		plotMapFile.createNewFile()

		plotMap = gson.fromJson(regionMapFile.getText(), TreeMap.class)
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

		Region rg = gson.fromJson(file.getText(), Region.class)
		return rg
	}

	public void saveRegion(Region region) {
		assert region != null, "Region cannot be null"

		def file = new File(regionFolder, "${region.x}.${region.z}.rg")
		file.createNewFile()
		file.setText(gson.toJson(region))
	}

	public Plot getPlot(int id) {
		XZLoc loc = regionMap.get(plotMap.get(id))

		if(!loc)
			return null

		Region region = getRegionByLocation(loc.x, loc.z)

		return region.plots.get(id)
	}


	public Region createRegion(Region region) {
		def id = regionMap.getLastEntry().getKey() + 1

		region.setId(id)
		region.setCreatedAt(System.currentTimeMillis())

		return region
	}


	public Region getRegionByPlotId(int id) {
		def rid = plotMap.get(id);
		return getRegion(rid)
	}


	public Plot createPlot(Region region, Plot plot) {
		def id = plotMap.getLastEntry().getKey() + 1

		plot.setId(id)
		plot.setCreatedAt(System.currentTimeMillis())

		region.plots.put(id, plot)

		saveRegion(region)
		plotMap.put(id, plot)

		return plot;
	}

	public PlotMember getMember(String uuid) {
		def file = new File(userFolder, "${uuid}.json")

		if(!file.exists())
			return null

		gson.fromJson(file.getText(), PlotMember.class)
	}

	public void saveMember(PlotMember member) {
		def file = new File(userFolder, "${uuid}.json")
		file.createNewFile()

		gson.toJson(file.getText())
	}

	private void savePlotMap(){
		plotMapFile.setText(gson.toJson(plotMap))
	}

	private void saveRegionMap(){
		regionMapFile.setText(gson.toJson(regionMap))
	}

}
