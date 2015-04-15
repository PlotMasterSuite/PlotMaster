package org.mcsg.plotmaster

import groovy.transform.CompileStatic;

import java.util.Map.Entry;

import org.mcsg.plotmaster.cache.Cacheable;
import org.mcsg.plotmaster.managers.PlotManager

@CompileStatic
class PlotMember implements Cacheable{
	
	String uuid;
	String name;
	int homeid;
	
	String world
	
	transient PlotManager manager

	
	Map<AccessLevel, List<Integer>> plots = new HashMap<>()
	
	
	Map<AccessLevel, List<Integer>> getPlotAccessMap(){
		return plots;
	}
	
	Plot getHomePlot() {
		return manager.getPlot(homeid, null)
	}
	
	void setHomePlot(int id) {
		this.homeid = id
		save()
	}
	
	List<Plot> getPlots() {
		List<Plot> list = []
		plots.values().each { List<Integer> infos ->
			if(infos){
				infos.each { Integer id ->
					list.add(manager.getPlot(id, null))
				}
			}
		}
		return list
	}
	
	List<Plot> getPlots(AccessLevel access){
		List<Plot> list = []
		plots.each { AccessLevel level,List<Integer> info ->
			if(level == access){
				if(info){
					info.each { Integer id ->
						list.add(manager.getPlot(id, null))
					}
				}
			}
		}
		return list
	}
	
	List<Plot> getPlotsAboveLevel(AccessLevel access){
		List<Plot> list = []
		plots.each { AccessLevel level, List<Integer> info ->
			println "Level is ${access.getLevel()}, ${level.getLevel()} "
			if(level.getLevel() >= access.getLevel()){
				if(info){
					info.each { Integer id ->
						list.add(manager.getPlot(id, null))
					}
				}
			}
		}
		return list
	}
	
	
	AccessLevel getAccessLevel(Plot plot){
		for(Entry<AccessLevel, List<Plot>> e : plots.entrySet()) {
			for(Plot p : e.getValue()){
				if(p.id == plot.id){
					return e.getKey()
				}
			}
		}
		return AccessLevel.NONE
	}
	
	@groovy.transform.CompileDynamic
	void setAccess(AccessLevel access, Plot plot){
		def list = plots.get(access) ?: []
		list.add(new PlotInfo(id: plot.getId(), world: plot.getWorld()))
		
		plots.put(access, list)
		
		save()
		
		plot.accessMap.put(uuid, access)
		plot.save()
	}
	
	
	void save() {
		if(manager)
			manager.savePlotMember(this){}
	}
	
	public boolean isStale() {
		return false;
	}
	
	
	
	
}
