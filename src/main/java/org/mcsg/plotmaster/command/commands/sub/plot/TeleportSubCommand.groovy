package org.mcsg.plotmaster.command.commands.sub.plot

import groovy.transform.CompileStatic;

import java.util.List;

import org.mcsg.plotmaster.AccessLevel;
import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.PlotMember
import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.command.PlotSubCommand
import org.mcsg.plotmaster.managers.PlotManager;
import org.mcsg.plotmaster.utils.AsyncUtils;
import org.mcsg.plotmaster.utils.PlatformAdapter;

@CompileStatic
class TeleportSubCommand implements PlotSubCommand{
	
	public String help() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//TODO: really need to clean this up 
	public boolean onCommand(PMPlayer player, PlotManager manager, List<String> args) {
		if(args.size() == 0) {
			player.sendMessage("&cMust specify an ID or player")
		}  
		
		if(args[0].isNumber()) {
			int id = args[0].toInteger()
			
			manager.getPlot(id) { Plot plot ->
				if(plot)
				player.teleport(plot.getCenter())
				else
				player.sendMessage("&cPlot not found")
			}	
			return true
		}
		
		def uuid = PlatformAdapter.getUUID(args[0])
		
		if(!uuid) {
			player.sendMessage("&cPlayer not found!")
			return true
		}
		
		
		def callback = { Plot plot ->
			if(plot) {
				player.teleport(plot.getCenter())
			} 
		}
		
		AsyncUtils.asyncWrap(callback) {
			def member = manager.getPlotMember(uuid, null)
			
			if(!member) {
				player.sendMessage("&cPlayer not found!")
				return null
			}
			
			
			def getPlot = {int id ->
				def list = member.getPlotsAboveLevel(AccessLevel.MEMBER)
				
				println list?.size()
				
				if(!list || list.size() == 0) {
					player.sendMessage("&cPlayer is not a member of any plots!")
					return null
				}
				
				if(id > list.size() || id < 0) {
					player.sendMessage("&cInvalid plot! Please select a plot between 1 and ${list.size()}")
					return null
				}
				return list.get(id)
			}
			
		
			
			
			if(args.size() == 2 && args[1].isNumber()) {
				def id = args[1].toInteger() - 1
				return getPlot(id)
			} 
			
			else {
				Plot plot =  member.getHomePlot()
				
				if(plot)
					return plot
				else
					return getPlot(0)
				
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
