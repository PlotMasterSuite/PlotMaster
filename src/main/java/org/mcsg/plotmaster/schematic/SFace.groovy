package org.mcsg.plotmaster.schematic

enum SFace {

	NORTH(1, false, 1, 0),
	EAST(2, false, 0, 1),
	SOUTH(3, false, -1, 0),
	WEST(4, false, 0, -1),
	
	NORTH_EAST(5, true, 1, 1),
	SOUTH_EAST(6, true, -1, 1),
	SOUTH_WEST(7, true, -1, -1),
	NORTH_WEST(8, true, 1, -1)
	
	int id
	boolean corner
	
	int xmod
	int zmod
	
	
	def SFace(id, corner, int xmod, int zmod){
		this.id = id
		this.corner = corner
	}
	
	/**
	 * 
	 * Allow to get from the format "southeast"
	 * 
	 * @param s
	 * @return
	 */
	static SFace fromString(String s){
		String str = s.toUpperCase()
		
		if(str.length() > 5 && !str.contains("_")){
			str = str.substring(0, 5) + "_" + str.substring(6)
		}
		
		return this.valueOf(str)
	}
	
}
