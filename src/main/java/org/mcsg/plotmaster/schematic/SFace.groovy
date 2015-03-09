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
