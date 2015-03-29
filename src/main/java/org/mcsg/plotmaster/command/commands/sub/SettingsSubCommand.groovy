package org.mcsg.plotmaster.command.commands.sub

import java.util.List;

import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.bridge.PMCommandSender;
import org.mcsg.plotmaster.bridge.PMPlayer
import org.mcsg.plotmaster.command.ConsoleSubCommand
import org.mcsg.plotmaster.command.PlayerSubCommand;

class SettingsSubCommand implements PlayerSubCommand{

	public String help() {
		return null;
	}

	public boolean onCommand(PMPlayer player, List<String> args) {
		if(args.size() > 1) {
			def loc = player.getLocation()
			def manager = PlotMaster.getInstance().getManager(loc.getWorld())
			
			manager.getPlot(loc) { Plot plot
				if(plot) {
					def set = args.get(0)
					
					if(plot.getSetting(set)) {
						plot.setSetting(set, args[1])
						plot.save()
					} else {
						player.sendMessage("&cSetting \"${args[0]}\" does not exists")
						
						player.sendMessage("&cPossible options: ${plot.getSettings().keySet().toString()}")
					}
					
				}
			}
		}
	}



}
