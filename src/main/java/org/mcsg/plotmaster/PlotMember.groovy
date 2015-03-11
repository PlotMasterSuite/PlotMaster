package org.mcsg.plotmaster

import org.mcsg.plotmaster.PlotMember.PlotInfo.RegionCoord
import org.mcsg.plotmaster.managers.PlotManager


class PlotMember {

	String uuid;
	String name;

	transient PlotManager manager

	static class PlotInfo {
		int id;
		String world
		RegionCoord coords

		static class RegionCoord {
			int x, z
		}
	}

	Map<AccessLevel, ArrayList<PlotInfo>> plots = [:]

	
	
	
	List<Plot> getPlots() {
		List<Plot> list = []
		plots.values().forEach { PlotInfo info
			list.add(thisObject)
		}
	}

	List<Plot> getPlots(AccessLevel access){

	}
	
	List<Plot> getPlotsAboveLevel(AccessLevel access){
		
	}

	void setAccess(AccessLevel access, Plot plot){
		def list = plots.get(access) ?: []
		list.add(new PlotInfo(id: plot.getId(), world: plot.getWorld(), coords: new RegionCoord(x: plot.getRegion().getX(), z: plot.getRegion().getZ())))

		plots.put(access, list)
		
		if(manager)
			manager.savePlotMember(this)
	}


}
