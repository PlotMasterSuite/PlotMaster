package org.mcsg.plotmaster.command.commands.sub

import java.util.List;

import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.command.PlayerSubCommand
import org.mcsg.plotmaster.managers.PlotManager;

class InfoSubCommand implements PlayerSubCommand{

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCommand(PMPlayer player, List<String> args) {
		def loc = player.getLocation()
		PlotMaster.getInstance().getManager(loc.getWorld()).getPlot(loc.getX(), loc.getZ()) { Plot plot ->
			
			if(plot) {
				
				player.sendMessage "&6--[ X: ${plot.getX()}, Z: ${plot.getZ()} ]--"
				player.sendMessage "&aOwner: ${plot.getOwnerName()}"
				player.sendMessage "&aRegion: ${plot.getRegion().getX()}, ${plot.getRegion().getZ()}"
				
				
			} else {
			
				player.sendMessage "&cNo plot found"
			
			}
			
		}
	}
}
