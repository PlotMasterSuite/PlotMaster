package org.mcsg.plotmaster


class Plot {

	int id
	
	String plotName
	String ownerName, OwnerUUID
	int x, z, h, w
	
	long createdAt	
	PlotType type
	
	List<String> members
	List<String> deny

	
	transient boolean changed
	transient Region region

}
