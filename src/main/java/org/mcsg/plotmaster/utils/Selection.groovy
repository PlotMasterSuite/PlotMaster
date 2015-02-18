package org.mcsg.plotmaster.utils

import org.mcsg.plotmaster.schematic.Schematic

class Selection {

	static Map<String, Selection> selections = [:]
	
	
	static Selection select(String player, boolean left, String world, int x, int y, int z){
		def selection = selections.get(player) ?: new Selection(world: world)
		
		if(left) {
			selection.x1 = x
			selection.y1 = y
			selection.z1 = z
		} else {
			selection.x2 = x
			selection.y2 = y
			selection.z2 = z
		}
		
		return selection
	}
	
	static Selection getSelection(String player){
		return selections.get(player)
	}
	
	
	Schematic toSchematic(){
		Schematic schematic = new Schematic(x1 - x2, y1 - y2, z1 - z2)
		
		for(x in x1..x2){
			for(z in z1..z2){
				for(y in y1..y2){
					schematic.setBlockAt(x, y, z, PlatformAdapter.toSchematicBlock(world, x, y, z))
				}
			}
		}
	}
	
	String world
	
	int x1, y1, z1, x2, y2, z2
	
}
