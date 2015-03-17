package org.mcsg.plotmaster.managers.grid

import groovy.lang.Closure;
import groovy.transform.CompileStatic;

import org.bukkit.Location;
import org.mcsg.plotmaster.AccessLevel;
import org.mcsg.plotmaster.Plot;
import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.PlotMember;
import org.mcsg.plotmaster.PlotType;
import org.mcsg.plotmaster.Region;
import org.mcsg.plotmaster.Settings;
import org.mcsg.plotmaster.backend.Backend
import org.mcsg.plotmaster.bridge.PMLocation;
import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.cache.Cache
import org.mcsg.plotmaster.cache.CacheFactory;
import org.mcsg.plotmaster.managers.PlotCreation
import org.mcsg.plotmaster.managers.PlotCreation.PlotCreationStatus;
import org.mcsg.plotmaster.managers.PlotManager
import org.mcsg.plotmaster.managers.RegionCreation
import org.mcsg.plotmaster.managers.RegionCreation.RegionCreationStatus;
import org.mcsg.plotmaster.schematic.Border
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

	Map settings

	Border border
	int bw2
	int bw

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
		this.settings = settings

		border = Border.load(settings.grid.border)
		if(border) {
			bw = border.getWidth()
			bw2 = bw + bw
		}
	}


	@Override
	public Region getRegionAt(int x, int z, Callback c) {
		def regx = getRegionX(x)
		def regz = getRegionZ(z)
		
		println "getRegionAt() regx: $regx, regz: $regz"
		
		asyncWrap(c) {
			Region r = xzRegionCache.get("$regx:$regz")
			if(r) {
				return r
			} else {
				r = backend.getRegionByLocation(regx, regz)
				xzRegionCache.cache("$regx:$regz", r)
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
	public Plot getPlotAt(int x, int z, Callback c ) {
		print "getPlotAt() cellx: ${x},  cellz: ${z}"

		asyncWrap(c) {
			Region r = getRegionAt(x, z, null)
			if(!r) return null
			def plots = r.plots

			for(Plot plot in plots.values()){
				if(LocationUtils.isInRegion(x, z, plot.getX(), plot.getZ(), plot.getX() + plot.getW(), plot.getZ() + plot.getH())){
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
			return getPlotAt(x, z, null) != null
		}
	}



	@Override
	public  PlotCreation createPlot(int x, int z, PlotType type, Callback c) {
		asyncWrap(c){
			if(plotExist(x, z, null))
				return new PlotCreation(status: PlotCreationStatus.PLOT_EXISTS)



			Region region = getRegionAt(x, z, null)
			//println "Got region ${region.getX()}, ${region.getZ()}"
			if(!region) {
				def regCre = createRegion(x, z, cellWidth, cellHeight, null)


				if(regCre.getStatus() == RegionCreationStatus.SUCCESS || regCre.getStatus() == RegionCreationStatus.REGION_EXISTS){
					region = regCre.getRegion()
				} else {
					PlotMaster.getInstance().console.warn("Failed to create region ${regCre.status.getMessage()}")
					return new PlotCreation(status: PlotCreationStatus.NO_PARENT_REGION)
				}
			}

			//we'll get to cell packing later, for now just create a single plot in the center of the region

			def plox = region.getX()
			def ploz = region.getZ()
			println "CREATE PLOT AT ${plox}, ${ploz}"


			plox += cellWidth / 2
			plox -= type.w / 2
			plox++

			ploz += cellHeight / 2
			ploz -= type.h / 2
			ploz++

			if(region.getPlots().size() > 0){
				return new PlotCreation(status: PlotCreationStatus.REGION_FULL)
			}

			Plot plot = new Plot(world: world, region: region, x: plox, z: ploz, w: type.w, h: type.h, type: type);

			return new PlotCreation(status: PlotCreationStatus.SUCCESS, plot: backend.createPlot(region, plot))
		}
	}

	@Override
	public PlotCreation createPlot(PMPlayer player, int x, int z, PlotType type, Callback c) {
		asyncWrap(c){

			def creation =  createPlot(x, z, type, null)

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
		def regx = getRegionX(x)
		def regz = getRegionZ(z)

		println "CreateRegion() ${regx}, ${regz}"

		asyncWrap(c){
			if(regionExist(regx, regz, null))
				return new RegionCreation(status: RegionCreationStatus.REGION_EXISTS, region: getRegionAt(regx, regz, null))

			Region region = new Region(world: world, x: regx, z: regz, h: h, w: w)

			region = backend.createRegion(region)

			xzRegionCache.cache("${region.getX()}:${region.getZ()}", region)

			return new RegionCreation(status: RegionCreationStatus.SUCCESS, region: region)
		}
	}

	private int getRegionX(int x) {
		//return x / cellWidth + ((x < 0) ? -1 : 1)

		def width = cellWidth + bw2

		if(x > 0)
			return (x - (x % width)) + bw
		else
			return (x - (width - Math.abs(x % width))) + bw
	}

	private int getRegionZ(int z) {
		//return z / cellWidth + ((z < 0) ? -1 : 1)

		def height = cellHeight + bw2


		if(z > 0)
			return (z - (z % height)) + bw
		else
			return (z - (height - Math.abs(z % height))) + bw
	}

	@Override
	public PlotMember getPlotMember(PMPlayer player) {
		PlotMember member =  backend.getMember(player.getUUID())
		if(!member){
			member = new PlotMember(uuid: player.getUUID(), name : player.getName())
			savePlotMember(member)
		}
		member.setManager(this)
		return member
	}

	@Override
	public PlotMember savePlotMember(PlotMember member) {
		backend.saveMember(member)
	}

	@Override
	public boolean canModifyLocation(PMPlayer player, PMLocation location) {
		PlotMember member = getPlotMember(player)

		def isPart = false

		member.getPlotsAboveLevel(AccessLevel.MEMBER).each {
			if(it.isPartOf(location.x, location.z)) {
				isPart = true
				return
			}
		}
		return isPart
	}

	@Override
	public boolean canEnterLocation(PMPlayer player, PMLocation location) {
		PlotMember member = getPlotMember(player)

		def isPart = true

		member.getPlots(AccessLevel.DENY).each {
			if(it.isPartOf(location.x, location.z)) {
				isPart = false
				return
			}
		}
		return isPart
	}

	@Override
	public void saveRegion(Region region, Callback c) {
		asyncWrap(c) {
			backend.saveRegion(region)
		}

	}

	@Override
	public void savePlot(Plot plot, Callback c) {
		saveRegion(plot.getRegion(), c)
	}


}
