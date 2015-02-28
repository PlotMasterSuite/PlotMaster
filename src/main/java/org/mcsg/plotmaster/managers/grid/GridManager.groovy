package org.mcsg.plotmaster.managers.grid

import groovy.lang.Closure;
import groovy.transform.CompileStatic;

import org.bukkit.Location;
import org.mcsg.plotmaster.Plot;
import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.PlotType;
import org.mcsg.plotmaster.Region;
import org.mcsg.plotmaster.Settings;
import org.mcsg.plotmaster.backend.Backend
import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.cache.Cache
import org.mcsg.plotmaster.cache.CacheFactory;
import org.mcsg.plotmaster.managers.PlotCreation
import org.mcsg.plotmaster.managers.PlotCreation.PlotCreationStatus;
import org.mcsg.plotmaster.managers.PlotManager
import org.mcsg.plotmaster.managers.RegionCreation
import org.mcsg.plotmaster.managers.RegionCreation.RegionCreationStatus;
import org.mcsg.plotmaster.utils.Callback
import org.mcsg.plotmaster.utils.LocationUtils;

import static org.mcsg.plotmaster.utils.AsyncUtils.asyncWrap;

class GridManager extends PlotManager{

	Cache regionCache;
	Cache xzRegionCache;

	Cache plotCache;

	String world
	int cellWidth
	int cellHeight


	def GridManager(Backend backend, String world) {
		super(backend, world)

		this.world = world

		regionCache = CacheFactory.createCache()
		xzRegionCache = CacheFactory.createCache()
		plotCache = CacheFactory.createCache()

	}

	void load(Map settings){
		this.cellHeight = settings.grid.height
		this.cellWidth = settings.grid.width
	}


	@Override
	public Region getRegionAt(int x, int z, Callback c) {
		asyncWrap(c) {
			Region r = xzRegionCache.get("$x:$z")
			if(r) {
				return r
			} else {
				r = backend.getRegionByLocation(x, z)
				xzRegionCache.cache("$x:$z", r)
				return r
			}
		}
	}

	@Override
	public Region getRegion(int id, Callback c) {
		asyncWrap(c) {
			Region r = regionCache.get(id)
			if(r) {
				return r
			} else {
				r = backend.getRegion(id)
				regionCache.cache(id, r)
				return r
			}
		}
	}


	@Override
	public Plot getPlot(int x, int z, Callback c ) {
		int cellx = (x / cellWidth).toInteger()
		int cellz = (z / cellHeight).toInteger()


		asyncWrap(c) {
			Region r = getRegionAt(cellx, cellz, null)
			if(!r) return null
			def plots = r.getPlots()

			for(Plot plot in plots.values){
				if(LocationUtils.isInRegion(x, z, plot.getX(), plot.getZ(), plot.getX() + plot.getType().getW(), plot.getZ() + plot.getType().getH())){
					return plot;
				}
			}
			return null;
		}
	}

	@Override
	public Plot getPlot(int id, Callback c) {
		asyncWrap(c) {
			Plot p = plotCache.get(id)
			if(p) {
				return p
			} else {
				p = backend.getPlot(id)
				plotCache.cache(id, p)
				return p
			}
		}
	}


	@Override
	public boolean regionExist(int x, int z, Callback c) {
		asyncWrap(c) {
			return getRegionAt(x, z, null) != null
		}
	}


	@Override
	public boolean plotExist(int x, int z, Callback c) {
		asyncWrap(c) {
			return getPlot(x, z, null) != null
		}
	}



	@Override
	public  PlotCreation createPlot(int x, int z, PlotType type, Callback c) {
		asyncWrap(c){
			if(plotExist(x, z, null))
				return new PlotCreation(status: PlotCreationStatus.PLOT_EXISTS)

			Region region = getRegionAt(x, z, null)
			if(!region) {
				def regCre = createRegion(x - (x % cellWidth), z - (z % cellHeight), cellWidth, cellHeight, null)

				if(regCre.getStatus() == RegionCreationStatus.SUCCESS || regCre.getStatus() == RegionCreationStatus.REGION_EXISTS){
					region = regCre.getRegion()
				} else {
					PlotMaster.getInstance().console.warn("Failed to create region ${regCre.status.getMessage()}")
					return new PlotCreation(status: PlotCreationStatus.NO_PARENT_REGION)
				}
			}

			Plot plot = new Plot(region: region, x: x, z: z, w: type.w, h: type.h, type: type);


			return new PlotCreation(status: PlotCreationStatus.SUCCESS, plot: backend.createPlot(region, plot))
		}
	}

	@Override
	public PlotCreation createPlot(PMPlayer player, int x, int z, PlotType type, Callback c) {
		asyncWrap(c){

			def creation =  createPlot(x, z, type, null)
			println "OMG ${creation.getStatus().toString()}"
			
			if(creation.getStatus() == PlotCreationStatus.SUCCESS){
				creation.getPlot().setOwnerName(player.getName())
				creation.getPlot().setOwnerUUID(player.getUUID())

				backend.saveRegion(creation.getPlot().getRegion())

				return creation
			} else {
				return creation
			}
		}
	}


	@Override
	public RegionCreation createRegion(int x, int z, int w, int h, Callback c) {
		asyncWrap(c){
			if(regionExist(x, z, null))
				return new RegionCreation(status: RegionCreationStatus.REGION_EXISTS, region: getRegionAt(x,z,null))

			Region region = new Region(x: x, z: z, h: h, w: w)

			region = backend.createRegion(region)
			return new RegionCreation(status: RegionCreationStatus.SUCCESS, region: region)
		}
	}


}
