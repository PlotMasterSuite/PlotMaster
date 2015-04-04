package org.mcsg.plotmaster.command.commands.sub.plot

import java.util.List;

import org.mcsg.plotmaster.AccessLevel;
import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.PlotMember
import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.command.PlayerSubCommand;
import org.mcsg.plotmaster.command.PlotSubCommand
import org.mcsg.plotmaster.managers.PlotManager
import org.mcsg.plotmaster.utils.PlatformAdapter;

class HomeSubCommand implements PlotSubCommand {
	
	public String help() {
		return null;
	}
	
	public boolean onCommand(PMPlayer player,PlotManager manager,  List<String> args) {
		def tpid = { PlotMember member, int id ->
			def list = member.getPlotsAboveLevel(AccessLevel.MEMBER)
			
			if(list.size() == 0) {
				player.sendMessage("&cYou are not a member of any plots!")
				return true
			}
			
			if(id > list.size() || id < 0) {
				player.sendMessage("&cInvalid plot! Please select a plot between 1 and ${list.size()}")
				return true
			}
			player.teleport(list.get(id))
			return true
		}
		
		
		manager.getPlotMember(player) { PlotMember member ->
			if(args.size() > 0) {
				if(args[0].equalsIgnoreCase("set")) {
					manager.getPlotAt(player.getLocation()) { Plot plot ->
						member.setHomePlot(plot.getId())
						player.sendMessage("&aHome set!")
					}
				}else if(args[0].isNumber()) {
					def id = args[0] - 1
					return tpid(member, id)
				}
			} else {
				def plot = member.getHomePlot()
				if(!plot) {
					return tpid(member, 0)
				}
				player.teleport(plot.getCenter())
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
