package org.mcsg.plotmaster.command.commands.sub

import java.util.List;

import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.command.PlayerSubCommand;
import org.mcsg.plotmaster.managers.PlotManager

class ResetSubCommand implements PlayerSubCommand {

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCommand(PMPlayer player, List<String> args) {
		
		PlotManager manager = PlotMaster.getInstance().getManager(player.getLocation().getWorld())
		Plot p = manager.getPlotAt(player.getLocation().getX(), player.getLocation().getZ(), null)
		
		p.reset(manager.getSettings()) {
			player.sendMessage("&aPlot has been cleared!")
		}
		
		
	}

}
