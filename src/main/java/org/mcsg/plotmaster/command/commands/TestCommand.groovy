package org.mcsg.plotmaster.command.commands

import groovy.transform.CompileStatic;

import java.util.List;

import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.command.PlayerCommand
import org.mcsg.plotmaster.managers.PlotManager;

@CompileStatic
class TestCommand extends PlayerCommand {

	@Override
	public boolean onCommand(PMPlayer player, List<String> args) {
		def master = PlotMaster.getInstance()
		def manager = master.getManager(player.getLocation().getWorld())
		
		
		manager.createPlot(player, 0, 0, master.getPlotType(player.getLocation().getWorld(), "donator"), null)
		manager.createPlot(player, 100, 100, master.getPlotType(player.getLocation().getWorld(), "donator"), null)
		manager.createPlot(player, 150, 100, master.getPlotType(player.getLocation().getWorld(), "donator"), null)
		manager.createPlot(player, 100, 150, master.getPlotType(player.getLocation().getWorld(), "donator"), null)
		manager.createPlot(player, 100, -100, master.getPlotType(player.getLocation().getWorld(), "donator"), null)
	
		
		
		
		
		
		
		
	}

	@Override
	public void registerCommands() {
		// TODO Auto-generated method stub
		
	}

}
