package org.mcsg.plotmaster.command.commands.sub

import java.util.List;

import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.command.PlayerSubCommand

class DeleteSubCommand implements PlayerSubCommand {

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCommand(PMPlayer player, List<String> args) {
		def location = player.getLocation()
		def manager = PlotMaster.getInstance().getManager(location.getWorld())
		
		manager.getPlotAt(location.getX(), location.getZ()){
			manager.deletePlot(it) {
				player.sendMessage("&aPlot has been deleted")
			}
		}
		
	}

}
