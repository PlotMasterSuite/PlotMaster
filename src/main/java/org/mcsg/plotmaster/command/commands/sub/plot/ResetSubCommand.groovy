package org.mcsg.plotmaster.command.commands.sub.plot

import groovy.transform.CompileStatic;

import java.util.List;

import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.command.PlayerSubCommand;
import org.mcsg.plotmaster.command.PlotSubCommand
import org.mcsg.plotmaster.managers.PlotManager


@CompileStatic
class ResetSubCommand implements PlotSubCommand {

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCommand(PMPlayer player,PlotManager manager, List<String> args) {
		
		Plot p = manager.getPlotAt(player.getLocation().getX(), player.getLocation().getZ(), null)
		
		p.reset(manager.getSettings()) {
			player.sendMessage("&aPlot has been reset!")
		}
		
		
	}

	public String getCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getPermission() {
		// TODO Auto-generated method stub
		return null;
	}
}
