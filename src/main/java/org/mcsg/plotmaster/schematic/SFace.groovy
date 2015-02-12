package org.mcsg.plotmaster.schematic

enum SFace {

	NORTH(1, false),
	EAST(2, false),
	SOUTH(3, false),
	WEST(4, false),
	
	NORTH_EAST(5, true),
	SOUTH_EAST(6, true),
	SOUTH_WEST(7, true),
	NORTH_WEST(8, true)
	
	int id
	boolean corner
	
	def SFace(id, corner){
		this.id = id
		this.corner = corner
	}
	
	
}
