package org.mcsg.plotmaster


class Plot {

	int id
	
	String plotName
	String ownerName, OwnerUUID
	int x, z, h, w
	
	long createdAt
	long loadedAt
	
	boolean changed
	
	PlotType type
	
	List<String> members
	List<String> deny

	

}
