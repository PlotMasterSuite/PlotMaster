package org.mcsg.plotmaster.command

import java.util.List;

import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.managers.PlotManager

interface PlotSubCommand extends SubCommand{

	boolean onCommand(PMPlayer player, PlotManager manager, List<String> args);
	
		
}
