package org.mcsg.plotmaster.command.commands.sub

import org.mcsg.plotmaster.AccessLevel;
import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.command.PlayerSubCommand;
import org.mcsg.plotmaster.managers.PlotCreation;
import org.mcsg.plotmaster.managers.PlotCreation.PlotCreationStatus;
import org.mcsg.plotmaster.utils.Messages;

class ClaimSubCommand implements PlayerSubCommand{

	@Override
	public String help() {
		"""
			&9Creates a new plot at this location
			&b/plot create [type]
		"""
	}

	@Override
	public boolean onCommand(PMPlayer player, List<String> args) {
		if(player.hasPermission("plot.create")){
			def tname = (args[0]) ?: "default"
			
			def loc = player.getLocation() 
			def type = PlotMaster.getInstance().getPlotType(loc.getWorld(), tname)
			
			if(!type){
				player.sendMessage("&cRequested PlotType does not exist!")
			} else {
				def man = PlotMaster.getInstance().getManager(loc.getWorld())
				
				man.createPlot(player, loc.getX(), loc.getZ(), type){
					def result = it as 	PlotCreation
					
					if(result.getStatus() == PlotCreationStatus.SUCCESS){
						def plot = result.getPlot()
						player.sendMessage(result.getStatus().getMessage())
						
						def member = man.getPlotMember(player)
						member.setAccess(AccessLevel.OWNER, plot)
						
						plot.reset(man.getSettings()) {
							player.sendMessage("&aPlot has been generated!")
						}
						
					} else {
						player.sendMessage(result.getStatus().getMessage())
					}
					
				}
			}
			
		} else {
			player.sendMessage(Messages.NO_PERMISSION)
		}
		
		
		
	}

}
