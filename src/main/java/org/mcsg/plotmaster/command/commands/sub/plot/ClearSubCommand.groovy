package org.mcsg.plotmaster.command.commands.sub.plot

import groovy.transform.CompileStatic;

import java.util.List;

import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.command.PlayerSubCommand
import org.mcsg.plotmaster.managers.PlotManager


@CompileStatic
class ClearSubCommand implements PlayerSubCommand{

	@Override
	public String help() {
		return "Clear a plot";
	}

	@Override
	public boolean onCommand(PMPlayer player, List<String> args) {

		PlotManager manager = PlotMaster.getInstance().getManager(player.getLocation().getWorld())
		Plot p = manager.getPlotAt(player.getLocation().getX(), player.getLocation().getZ(), null)

		if(p){
			p.clear(manager.getSettings()) {
				player.sendMessage("&aPlot has been cleared!")
			}
		}

	}

	public String getCommand() {
		return "clear";
	}
	public String getPermission() {
		return "plot.clear";
	}

}
