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

	Map<AccessLevel, List<PlotInfo>> plots = [:]




	List<Plot> getPlots() {
		List<Plot> list = []
		plots.values().each { List<PlotInfo> infos ->
			if(infos){
				infos.each {
					list.add(manager.getPlot(it.id, null))
				}
			}
		}
		return list
	}

	List<Plot> getPlots(AccessLevel access){
		List<Plot> list = []
		plots.each { AccessLevel level,List<PlotInfo> info ->
			if(level == access){
				if(info){
					info.each {
						list.add(manager.getPlot(it.id, null))
					}
				}
			}
		}
		return list
	}

	List<Plot> getPlotsAboveLevel(AccessLevel access){
		List<Plot> list = []
		plots.each { AccessLevel level, List<PlotInfo> info ->
			println "Level is ${access.getLevel()}, ${level.getLevel()} "
			if(level.getLevel() > access.getLevel()){
				if(info){
					info.each {
						list.add(manager.getPlot(it.id, null))
					}
				}
			}
		}
		return list
	}

	void setAccess(AccessLevel access, Plot plot){
		def list = plots.get(access) ?: []
		list.add(new PlotInfo(id: plot.getId(), world: plot.getWorld(), coords: new RegionCoord(x: plot.getRegion().getX(), z: plot.getRegion().getZ())))

		plots.put(access, list)

		if(manager)
			manager.savePlotMember(this)
	}


}
