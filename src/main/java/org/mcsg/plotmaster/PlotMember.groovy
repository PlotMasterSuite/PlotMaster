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
	
	
	Map<Integer, AccessLevel> plots = new HashMap<>()
	
	
	Map<Integer, AccessLevel> getPlotAccessMap(){
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
		plots.keySet().each { Integer id ->
			list.add(manager.getPlot(id, null))
		}
		
		return list
	}
	
	List<Plot> getPlots(AccessLevel access){
		List<Plot> list = []
		plots.each { Integer id, AccessLevel level ->
			if(level == access){
				list.add(manager.getPlot(id, null))
			}
		}
		return list
	}
	
	List<Plot> getPlotsAboveLevel(AccessLevel access){
		List<Plot> list = []
		plots.each { Integer id, AccessLevel level ->
			if(level.getLevel() >= access.getLevel()){
				list.add(manager.getPlot(id, null))
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
	
	void setAccess(AccessLevel access, Plot plot){
		plots.put(plot.id, access)
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
