package org.mcsg.plotmaster

import groovy.transform.CompileStatic;

import org.codehaus.groovy.binding.PropertyBinding.UpdateStrategy;
import org.mcsg.plotmaster.bridge.PMPlayer
import org.mcsg.plotmaster.command.PlayerCommand;
import org.mcsg.plotmaster.schematic.Schematic;
import org.mcsg.plotmaster.schematic.SchematicBlock
import org.mcsg.plotmaster.utils.BlockUpdate
import org.mcsg.plotmaster.utils.BlockUpdateTask
import org.mcsg.plotmaster.utils.PlatformAdapter;
import org.mcsg.plotmaster.utils.TaskQueue;

class PlotType {

	String name
	String schematic
	String border
	int h, w


	@CompileStatic
	void paintPlot(PMPlayer player, String world, int x, int y,  int z){
		List<BlockUpdate> updates = []


		if(schematic){
			Schematic schem = Schematic.load(schematic)

			if(schem){
				for (int xx in [x..x+w]) {
					for(int zz in [z..z+h]){
						SchematicBlock[] blocks = schem.getColumn(xx, zz)
						for(int yy in [0..blocks.length]){
							updates.add(PlatformAdapter.createBlockUpdate(world, xx, yy, zz, blocks[yy].material, blocks[yy].data))
						}
					}
				}
			} else {
				PlotMaster.getInstance().sendConsoleMessage("&aCould not load schematic ${schematic} for PlotType ${name}")
			}

		}


		TaskQueue.addTask(
				new BlockUpdateTask(updates) {
					
				}
			)


	}



}
