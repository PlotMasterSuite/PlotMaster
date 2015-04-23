package org.mcsg.plotmaster.command.commands.sub.plot

import java.util.List;

import org.mcsg.plotmaster.AccessLevel;
import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.PlotMember
import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.command.PlotSubCommand;
import org.mcsg.plotmaster.managers.PlotManager;

class RemoveSubCommand implements PlotSubCommand{

	public String help() {
		return "Removes a players access from this plot";
	}

	public String getCommand() {
		return "remove <player>";
	}

	public String getPermission() {
		return "player.access.remove";
	}

	public boolean onCommand(PMPlayer player, PlotManager manager, List<String> args) {
		def loc = player.getLocation()
		
		if(args.size() > 0) {
			manager.getPlotAt(loc) { Plot plot ->
				if(plot) {
					def level = plot.getAccessMap().get(player.getUUID())
					def p = args[0]
					
					manager.getPlotMember(p) { PlotMember member ->
						if(member) {
							if(level.getLevel() >= AccessLevel.ADMIN.level) {
								member.setAccess(AccessLevel.NONE, plot)
							} else {
								player.sendMessage("&cYou do not have the required plot access")
							}
						} else {
							player.sendMessage("&cPlayer not found or invalid player name")
						}
					}
				}
			}
		}
		return false;
	}
	
}
