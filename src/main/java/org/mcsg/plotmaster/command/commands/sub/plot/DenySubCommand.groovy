package org.mcsg.plotmaster.command.commands.sub.plot

import groovy.transform.CompileStatic;

import java.util.List;

import org.mcsg.plotmaster.AccessLevel;
import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.PlotMember
import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.command.PlayerSubCommand;
import org.mcsg.plotmaster.command.PlotSubCommand
import org.mcsg.plotmaster.managers.PlotManager;
import org.mcsg.plotmaster.utils.PlatformAdapter;

@CompileStatic
class DenySubCommand implements PlotSubCommand{
	
	public String help() {
		return "Denies a player access to your plot";
	}
	
	public String getCommand() {
		return "deny";
	}
	
	public String getPermission() {
		return "plot.access.deny";
	}
	
	public boolean onCommand(PMPlayer player, PlotManager manager, List<String> args) {
		def loc = player.getLocation()
		
		if(args.size() > 0) {
			manager.getPlotAt(loc) { Plot plot ->
				if(plot) {
					def level = plot.getAccessMap().get(player.getUUID())
					def p = args[0]
					String uuid = PlatformAdapter.getUUID(p)
					
					manager.getPlotMember(uuid) { PlotMember member ->
						if(member) {
							if(level.getLevel() >= AccessLevel.ADMIN.level) {
								member.setAccess(AccessLevel.DENY, plot)
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
