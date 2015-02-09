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
import org.mcsg.plotmaster.managers.PlotManager

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
	public boolean plotExist(int x, int z, Closure c) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean regionExist(int x, int z, Closure c) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public  void createPlot(int x, int y, PlotType type, Closure c) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void createRegion(int x, int y, int h, int w, Closure c) {
		// TODO Auto-generated method stub
		return null;
	}










}
