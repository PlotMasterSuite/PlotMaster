package org.mcsg.plotmaster.command.commands.sub.plot

import groovy.transform.CompileStatic;

import java.util.List;

import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.bridge.PMCommandSender;
import org.mcsg.plotmaster.bridge.PMPlayer
import org.mcsg.plotmaster.command.ConsoleSubCommand
import org.mcsg.plotmaster.command.PlayerSubCommand;
import org.mcsg.plotmaster.command.PlotSubCommand
import org.mcsg.plotmaster.managers.PlotManager;

@CompileStatic
class SettingsSubCommand implements PlotSubCommand{

	public String help() {
		return "Changes plot settings";
	}

	public boolean onCommand(PMPlayer player,PlotManager manager, List<String> args) {
		if(args.size() > 1) {
			def loc = player.getLocation()
			
			manager.getPlotAt(loc.getX(), loc.getZ()) { Plot plot
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

	public String getCommand() {
		return "setting [setting] [value]";
	}


	public String getPermission() {
		return "plot.setting";
	}

}
