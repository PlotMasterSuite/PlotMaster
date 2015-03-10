package org.mcsg.plotmaster

class PlotMember {
	
	String uuid;
	String name;
	
	class PlotInfo {
		int id;
		PlotCoord coords
		
		class PlotCoord {
			int x, y
		}
	}
	
	Map<AccessType, ArrayList<PlotInfo>> plots
	
	List<Plot> getPlots() {
		
	}
	
	List<Plot> getPlots(AccessType access){
		
	}
	
	
	
	
}
