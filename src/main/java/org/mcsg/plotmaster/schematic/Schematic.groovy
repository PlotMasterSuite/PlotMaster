package org.mcsg.plotmaster.schematic

class Schematic {

	SchematicBlock[][][] blocks;

	class SchematicBlock {
		String material
		byte data
	}

	SchematicBlock getBlockAt(int x,int y,int z){
		try{
			return blocks[x][y][z]
		} catch (Exception e){
			return null
		}
	}


}
