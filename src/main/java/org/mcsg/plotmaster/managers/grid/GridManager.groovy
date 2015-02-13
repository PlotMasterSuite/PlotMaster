package org.mcsg.plotmaster.managers.grid

import groovy.lang.Closure;
import groovy.transform.CompileStatic;

import org.bukkit.Location;
import org.mcsg.plotmaster.Plot;
import org.mcsg.plotmaster.PlotType;
import org.mcsg.plotmaster.Region;
import org.mcsg.plotmaster.Settings;
import org.mcsg.plotmaster.cache.Cache
import org.mcsg.plotmaster.cache.CacheFactory;
import org.mcsg.plotmaster.managers.PlotCreation
import org.mcsg.plotmaster.managers.PlotCreation.PlotCreationStatus;
import org.mcsg.plotmaster.managers.PlotManager
import org.mcsg.plotmaster.managers.RegionCreation
import org.mcsg.plotmaster.managers.RegionCreation.RegionCreationStatus;

import static org.mcsg.plotmaster.utils.AsyncUtils.asyncWrap;

class GridManager extends PlotManager{

	Cache regionCache;
	Cache xzRegionCache;

	Cache plotCache;

	String world
	int cellWidth
	int cellHeight


	def GridManager(String world, int width, int height) {
		super()
		this.world = world
		this.cellHeight = height
		this.cellWidth = width


		regionCache = CacheFactory.createCache()
		xzRegionCache = CacheFactory.createCache()
		plotCache = CacheFactory.createCache()

	}

	void load(){
		
	}
	

	@Override
	public Region getRegionAt(int x, int z, Closure c) {
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
	public Region getRegion(int id, Closure c) {
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
	public Plot getPlot(int x, int z, Closure c ) {
		int cellx = (x / cellWidth).toInteger()
		int cellz = (z / cellHeight).toInteger()


		asyncWrap(c) {
			Region r = getRegionAt(cellx, cellz)
			def plots = r.getPlots()

			for(plot in plots){
				if(plot.getX() == cellx && plot.getZ() == cellz){
					return plot;
				}
			}
			return null;
		}
	}

	@Override
	public Plot getPlot(int id, Closure c) {
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
	public boolean regionExist(int x, int z, Closure c) {
		asyncWrap(c) {
			return getRegionAt(x, z, null) != null
		}
	}


	@Override
	public boolean plotExist(int x, int z, Closure c) {
		asyncWrap(c) {
			return getPlot(x, z, null) != null
		}
	}



	@Override
	public  PlotCreation createPlot(int x, int z, PlotType type, Closure c) {
		asyncWrap(c){
			if(plotExist(x, z, null))
				return new PlotCreation(status: PlotCreationStatus.PLOT_EXISTS)

			Region region = getRegionAt(x, z, null)
			if(!region)
				return new PlotCreation(status: PlotCreationStatus.NO_PARENT_REGION)

			Plot plot = new Plot(region: region, x: x, z: z, w: type.w, h: type.h, type: type);
			return new PlotCreation(status: PlotCreationStatus.SUCCESS, plot: backend.createPlot(region, plot))
		}
	}


	@Override
	public RegionCreation createRegion(int x, int z, int h, int w, Closure c) {
		asyncWrap(c){
			if(regionExist(x, z, null))
				return new RegionCreation(status: RegionCreationStatus.REGION_EXISTS)

			Region region = new Region(x: x, z: z, h: h, w: w)

			region = backend.createRegion(region)
			return new RegionCreation(status: RegionCreationStatus.SUCCESS, region: region)
		}
	}

}
