package org.mcsg.plotmaster.utils

import org.mcsg.plotmaster.schematic.Schematic
import org.mcsg.plotmaster.schematic.SchematicBlock
import static java.lang.Math.*

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

		selections.put(player, selection)

		return selection
	}

	static Selection getSelection(String player){
		return selections.get(player)
	}


	Schematic toSchematic(){
		Schematic schematic = new Schematic(abs(x1 - x2) + 1, abs(y1 - y2) + 1, abs(z1 - z2) + 1)

		int mx = min(x1, x2)
		int my = min(y1, y2)
		int mz = min(z1, z2)


		for(x in mx..max(x1, x2)){
			for(z in mz..max(z1, z2)){
				for(y in my..max(y1, y2)){
					SchematicBlock b = PlatformAdapter.toSchematicBlock(world, x, y, z)

					if(b.material != "AIR")
						schematic.setBlockAt(x - mx, y - my, z - mz, )
				}
			}
		}
		return schematic
	}

	String world

	int x1, y1, z1, x2, y2, z2

}
