package org.mcsg.plotmaster.command.commands.sub.plot

import java.util.List;

import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.command.PlayerSubCommand
import org.mcsg.plotmaster.command.PlotSubCommand
import org.mcsg.plotmaster.managers.PlotManager;

class DeleteSubCommand implements PlotSubCommand {

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCommand(PMPlayer player,PlotManager manager, List<String> args) {
		def location = player.getLocation()
		
		manager.getPlotAt(location.getX(), location.getZ()){
			manager.deletePlot(it) {
				player.sendMessage("&aPlot has been deleted!")
			}
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
