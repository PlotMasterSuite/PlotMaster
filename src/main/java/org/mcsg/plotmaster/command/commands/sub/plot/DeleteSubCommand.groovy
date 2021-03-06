package org.mcsg.plotmaster.command.commands.sub.plot

import groovy.transform.CompileStatic;

import java.util.List;

import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.command.PlayerSubCommand
import org.mcsg.plotmaster.command.PlotSubCommand
import org.mcsg.plotmaster.managers.PlotManager;

@CompileStatic
class DeleteSubCommand implements PlotSubCommand {

	@Override
	public String help() {
		return "Clears a plot and removes all owners & users";
	}

	@Override
	public boolean onCommand(PMPlayer player,PlotManager manager, List<String> args) {
		def location = player.getLocation()
		
		manager.getPlotAt(location.getX(), location.getZ()){ Plot plot ->
			manager.deletePlot(plot) {
				player.sendMessage("&aPlot has been deleted!")
			}
		}
		
	}

	public String getCommand() {
		return "delete";
	}
	
	public String getPermission() {
		return "plot.delete";
	}

}
