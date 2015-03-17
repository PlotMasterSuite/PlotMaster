package org.mcsg.plotmaster.backend.flatfile

import com.google.gson.Gson
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import groovy.lang.Closure;
import groovy.transform.CompileStatic;

import java.lang.reflect.Type

import org.mcsg.plotmaster.Plot;
import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.PlotMember;
import org.mcsg.plotmaster.PlotType;
import org.mcsg.plotmaster.Region;
import org.mcsg.plotmaster.Settings;
import org.mcsg.plotmaster.backend.Backend
import org.mcsg.plotmaster.utils.AsyncUtils;
import org.mcsg.plotmaster.utils.PlatformAdapter;


@CompileStatic
class FlatFileBackend implements Backend{

	File folder
	File regionMapFile
	File plotMapFile
	File regionFolder
	File userFolder


	TreeMap<Integer, XZLoc> regionMap
	TreeMap<Integer, Integer> plotMap

	Gson gson

	static class XZLoc {
		int x, z 
	}

	public void load(String world, Map settings) {
		println settings.toMapString()
		if(settings.debug) {
			gson = new GsonBuilder().setPrettyPrinting().create()
			println settings.debug
		} else {
			gson = new Gson()
		}
		
		File loc = new File(PlatformAdapter.getDataFolder(), settings.location.toString());
		loc.mkdirs()

		folder = new File(loc, world)
		folder.mkdirs()

		regionFolder = new File(folder, "regions/")
		regionFolder.mkdirs()

		userFolder = new File(folder, "users/")
		userFolder.mkdirs()

		regionMapFile = new File(folder, "regionmap.json")
		regionMapFile.createNewFile()

		Type type = new TypeToken<TreeMap<Integer, XZLoc>>(){}.getType()
		regionMap = (gson.fromJson(regionMapFile.getText(), type) ?: new TreeMap<>()) as TreeMap

		plotMapFile = new File(folder, "plotmap.json")
		plotMapFile.createNewFile()

		type = new TypeToken<TreeMap<Integer, Integer>>(){}.getType()
		plotMap = (gson.fromJson(plotMapFile.getText(), type) ?: new TreeMap<>()) as TreeMap
	}

	public Region getRegion(int id) {
		XZLoc loc = regionMap.get(id)

		if(loc == null){
			return null
		}

		return getRegionByLocation(loc.x, loc.z)
	}

	public Region getRegionByLocation(int x, int z) {
		def file = new File(regionFolder, "${x}.${z}.rg")

		if(!file.exists())
			return null

		Region rg = gson.fromJson(file.getText(), Region.class)
	}

	public void saveRegion(Region region) {
		assert region != null, "Region cannot be null"

		def file = new File(regionFolder, "${region.x}.${region.z}.rg")
		
		file.createNewFile()
		file.setText(gson.toJson(region))
				
		savePlotMap()
		saveRegionMap()
	}

	public Plot getPlot(int id) {
		XZLoc loc = regionMap.get(plotMap.get(id))

		if(!loc)
			return null

		Region region = getRegionByLocation(loc.x, loc.z)

		return region.plots.get(id)
	}


	public Region createRegion(Region region) {
		def en  = regionMap.lastEntry()
		
		def id = ((en) ? en.getKey() : 0) + 1
		
		region.setId(id)
		region.setCreatedAt(System.currentTimeMillis())

		regionMap.put(id,
			 new XZLoc(x: region.getX(), z: region.getZ()))
		
		saveRegion(region)
		
		return region
	}


	public Region getRegionByPlotId(int id) {
		def rid = plotMap.get(id);
		return getRegion(rid)
	}


	public Plot createPlot(Region region, Plot plot) {
		def en  = plotMap.lastEntry()
				
		Integer id = ((en) ? en.getKey() : 0) + 1

		plot.setId(id)
		plot.setCreatedAt(System.currentTimeMillis())

		region.plots.put(id, plot)
		
		plotMap.put(id, region.getId())
		saveRegion(region)

		return plot;
	}

	public PlotMember getMember(String uuid) {
		def file = new File(userFolder, "${uuid}.json")

		if(!file.exists())
			return null

		gson.fromJson(file.getText(), PlotMember.class)
	}

	public void saveMember(PlotMember member) {
		def file = new File(userFolder, "${member.uuid}.json")
		file.createNewFile()

		file.setText(gson.toJson(member))
	}

	private void savePlotMap(){
		plotMapFile.setText(gson.toJson(plotMap))
	}

	private void saveRegionMap(){
		regionMapFile.setText(gson.toJson(regionMap))
	}

}
