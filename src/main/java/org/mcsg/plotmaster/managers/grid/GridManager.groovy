package org.mcsg.plotmaster.managers.grid

import java.lang.invoke.LambdaForm.Compiled;

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
import org.mcsg.plotmaster.bridge.PMVector
import org.mcsg.plotmaster.cache.Cache
import org.mcsg.plotmaster.cache.MemCache
import org.mcsg.plotmaster.events.PlotCreationEvent
import org.mcsg.plotmaster.managers.PlotCreation
import org.mcsg.plotmaster.managers.PlotCreation.PlotCreationStatus;
import org.mcsg.plotmaster.managers.PlotManager
import org.mcsg.plotmaster.managers.RegionCreation
import org.mcsg.plotmaster.managers.RegionCreation.RegionCreationStatus;
import org.mcsg.plotmaster.schematic.Border
import org.mcsg.plotmaster.utils.Callback
import org.mcsg.plotmaster.utils.LocationUtils;

import static org.mcsg.plotmaster.utils.AsyncUtils.asyncWrap;

@CompileStatic
class GridManager extends PlotManager{
	
	Cache<Integer, Region> regionCache;
	Cache<?, Region> xzRegionCache;
	
	Cache<Integer, Plot> plotCache;
	
	Cache<String, PlotMember> memberCache;
	
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
		
		regionCache = new MemCache(200, 400, 1000, 3000)
		xzRegionCache = new MemCache(200, 400, 1000, 3000)
		
		plotCache = new MemCache(200, 400, 3000, 5000)
		
		//remove users from cache when they go offline
		memberCache = new MemCache(-1, 0, 0, 0)
		
	}
	
	@groovy.transform.CompileDynamic
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
		
		//println "getRegionAt() regx: $regx, regz: $regz"
		
		(Region) asyncWrap(c) {
			return xzRegionCache.get("$regx:$regz") {
				Region r = backend.getRegionByLocation(regx, regz)
				xzRegionCache.cache("$regx:$regz", r)
				
				if(r) {
					r.plots.values().each {
						it.setManager(this)
					}
				}
				return r
			}
		}
	}
	
	@Override
	public Region getRegion(int id, Callback c) {
		(Region) asyncWrap(c) {
			return regionCache.get(id) {
				Region r = backend.getRegion(id)
				r.plots.values().each {
					it.setManager(this)
				}
				return r
			}
		}
	}
	
	
	@Override
	public Plot getPlotAt(int x, int z, Callback c ) {
		//print "getPlotAt() cellx: ${x},  cellz: ${z}"
		
		(Plot) asyncWrap(c) {
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
	public Plot getPlotAt(PMLocation loc, Callback c){
		return getPlotAt(loc.getX(), loc.getZ(), c)
	}
	
	@Override
	public Plot getPlot(int id, Callback c) {
		(Plot) asyncWrap(c) {
			return plotCache.get(id) {
				Plot p = backend.getPlot(id)
				p?.setManager(this)
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
		(PlotCreation) asyncWrap(c){
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
			///println "CREATE PLOT AT ${plox}, ${ploz}"
			
			
			plox += cellWidth / 2
			plox -= type.w / 2
			plox++
			
			ploz += cellHeight / 2
			ploz -= type.h / 2
			ploz++
			
			if(region.getPlots().size() > 0){
				return new PlotCreation(status: PlotCreationStatus.REGION_FULL)
			}
			
			Plot plot = new Plot(world: world, region: region, x: plox, z: ploz, w: type.w, h: type.h, type: type, settings: type.settings);
			
			PlotCreationEvent e = new PlotCreationEvent(plot: plot)
			PlotMaster.getInstance().fireEvent(e)
			
			if(!e.isCancelled()){
				return new PlotCreation(status: PlotCreationStatus.SUCCESS, plot: backend.createPlot(region, plot))
			} else {
				return new PlotCreation(status: PlotCreationStatus.CANCELLED, message: e.getMessage())
			}
		}
	}
	
	@Override
	public PlotCreation createPlot(PMPlayer player, int x, int z, PlotType type, Callback c) {
		(PlotCreation) asyncWrap(c){
			
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
		
		//println "CreateRegion() ${regx}, ${regz}"
		
		(RegionCreation) asyncWrap(c){
			if(regionExist(regx, regz, null))
				return new RegionCreation(status: RegionCreationStatus.REGION_EXISTS, region: getRegionAt(regx, regz, null))
			
			Region region = new Region(world: world, x: regx, z: regz, h: h, w: w)
			
			region = backend.createRegion(region)
			
			xzRegionCache.cache("${region.getX()}:${region.getZ()}", region)
			
			return new RegionCreation(status: RegionCreationStatus.SUCCESS, region: region)
		}
	}
	
	private int getRegionX(int x) {
		def width = cellWidth + bw2
		
		if(x > 0)
			return (x - (x % width)) + bw
		else
			return (x - (width - Math.abs(x % width))) + bw
	}
	
	private int getRegionZ(int z) {
		def height = cellHeight + bw2
		
		
		if(z > 0)
			return (z - (z % height)) + bw
		else
			return (z - (height - Math.abs(z % height))) + bw
	}
	
	@Override
	public PlotMember getPlotMember(String uuid, Callback c) {
		(PlotMember) asyncWrap(c) {
			return memberCache.get(uuid) {
				return backend.getMember(uuid)
			}
		}
	}
	
	@Override
	public PlotMember getPlotMember(PMPlayer player, Callback c) {
		(PlotMember) asyncWrap(c){
			return memberCache.get(player.getUUID()) {
				PlotMember member =  backend.getMember(player.getUUID())
				if(!member){
					member = new PlotMember(uuid: player.getUUID(), name : player.getName())
					savePlotMember(member, null)
				}
				member.setManager(this)
				return member
			}
		}
	}
	
	@Override
	public PlotMember savePlotMember(PlotMember member, Callback c) {
		(PlotMember) asyncWrap(c) {
			backend.saveMember(member)
		}
	}
	
	@Override
	public boolean canModifyLocation(PMPlayer player, PMLocation location) {
		PlotMember member = getPlotMember(player, null)
		
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
		PlotMember member = getPlotMember(player, null)
		
		def mode = settings.get("default-access-mode")?.toString()?.toLowerCase()
		def isPart = !"deny".equals(mode)
		
		
		if(isPart) {
			member?.getPlots(AccessLevel.DENY)?.each {
				if(it.isPartOf(location.x, location.z)) {
					isPart = false
					return
				}
			}
		} else {
			member?.getPlotsAboveLevel(AccessLevel.ALLOW)?.each {
				if(it.isPartOf(location.x, location.z)) {
					isPart = true
					return
				}
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
	
	@Override
	public void deleteRegion(Region region, Callback c) {
		asyncWrap(c){
			region.getPlots().values().each {
				it.reset(settings, null)
			}
			backend.deleteRegion(region)
		}
	}
	
	@Override
	public void deletePlot(Plot plot, Callback c) {
		asyncWrap(c) {
			plot.clear(settings, null)
			backend.deletePlot(plot)
			plot.getRegion().getPlots().remove(plot.id)
		}
	}
	
	@Override
	public void playerOffline(PMPlayer player) {
		PlotMember member = memberCache.remove(player.getUUID())
		
		member.getPlots().each {
			it.memberOffline(member)
		}
	}
	
	@Override
	public void playerOnline(PMPlayer player) {
		getPlotMember(player){ PlotMember member ->
			memberCache.cache(member.uuid, member)
			
			member.getPlots().each {
				it.memberOnline(member)
			}
		}
	}
	
	@Override
	public boolean isInRegion(PMPlayer player, Callback c) {
		asyncWrap(c){
			
		}
	}
	
	@Override
	public boolean isInPlot(PMPlayer player, Callback c) {
		asyncWrap(c){
			return plotExist(player.getLocation().getX(), player.getLocation().getZ(), null)
		}
	}
	
	Map<String, PMVector> lastloc = [:]
	Map<String, Double> lastspeed = [:]
	Map<String, Boolean> value = [:]
	Map<String, Long> lasttime = [:]
	
	//So much work into not loading a plot lol
	
	@Override
	public boolean isInPlotFast(PMPlayer player, Callback c) {
		asyncWrap(c){
			def uuid = player.getUUID()
			def ll = lastloc[uuid]
			def ls = lastspeed[uuid]?.doubleValue()
			def lt = (lasttime[uuid]) ?: 0
			
			def loc = player.getLocation().asVector()
			
			double change = 0
			if(ll)
				change = Math.abs(ll.getX() - loc.getX()) +  Math.abs(ll.getZ() - loc.getZ())
			
			lastloc[uuid] = loc
			lastspeed[uuid] = change
			
			boolean val = value.get(player.getUUID()) ?: false;
			
			if(!player.isFlying()) {
				val = isInPlot(player, null)
			}
			else {
				if(ll) {
					if(change < ls && change < 0.3  && lt + 100 < System.currentTimeMillis()) {
						val =  isInPlot(player, null)
					}
					lasttime[uuid] = System.currentTimeMillis()
				}
			}
			value[player.getUUID()] = val
			return val
		}
	}
}
