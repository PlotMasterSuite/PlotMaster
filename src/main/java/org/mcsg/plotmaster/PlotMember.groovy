package org.mcsg.plotmaster

import org.mcsg.plotmaster.PlotMember.PlotInfo.RegionCoord
import org.mcsg.plotmaster.managers.PlotManager


class PlotMember {

	String uuid;
	String name;

	transient PlotManager manager

	static class PlotInfo {
		int id;
		RegionCoord coords

		static class RegionCoord {
			int x, z
		}
	}

	Map<AccessType, ArrayList<PlotInfo>> plots = [:]

	List<Plot> getPlots() {

	}

	List<Plot> getPlots(AccessType access){

	}

	void setAccess(AccessType access, Plot plot){
		def list = plots.get(access) ?: []
		list.add(new PlotInfo(id: plot.getId(), coords: new RegionCoord(x: plot.getRegion().getX(), z: plot.getRegion().getZ())))

		plots.put(access, list)
		
		if(manager)
			manager.savePlotMember(this)
	}


}
