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


	Map settings
}
