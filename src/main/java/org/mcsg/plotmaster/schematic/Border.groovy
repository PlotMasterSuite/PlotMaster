package org.mcsg.plotmaster.schematic

import org.mcsg.plotmaster.schematic.Schematic.SchematicBlock

class Border {

	String name;
	int width
	
	Map<SFace, Schematic> borders
	
	
	
	SchematicBlock getBlockAt(int x, int y, int z , int h, int w, int bottom){
		def posx = x % h
		def posz = z % w
		
		def face = getFace(posx, posz, h, w)
		
		if(face) {
			
			Schematic schematic = borders.get(face)
			
			int xloc = posx + (face.getXmod() * w)
			int yloc = y - bottom
			int zlox = posz + (face.getZmod() * h)
			
			return schematic.getBlockAt(xloc, yloc, zloc)
		}
		return null
	}
	
	
	
	private SFace getFace(int posx, int posz, int h, int w){
		
		boolean north = posx > h - width
		boolean south = posx < h + width 
		
		boolean west = posz > w - width
		boolean east = posz < w + width
		
		
		if(north && east)
			return SFace.NORTH_EAST
		if(north && west)
			return SFace.NORTH_WEST
		if(south && east)
			return SFace.SOUTH_EAST
		if(south && west)
			return SFace.SOUTH_WEST
		if(north)
			return SFace.NORTH
		if(south)
			return SFace.SOUTH
		if(west)
			return SFace.WEST
		if(east)
			return SFace.EAST
			
		return null
		
		
	}
	
	
}
