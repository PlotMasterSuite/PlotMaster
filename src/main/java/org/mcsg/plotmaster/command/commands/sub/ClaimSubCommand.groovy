package org.mcsg.plotmaster.command.commands.sub

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
			println "WORLD ${loc.getWorld()}"
			def type = PlotMaster.getInstance().getPlotType(loc.getWorld(), tname)
			
			if(!type){
				player.sendMessage("&cRequested PlotType does not exist!")
			} else {
				def man = PlotMaster.getInstance().getManager(loc.getWorld())
				
				
				man.createPlot(loc.getX(), loc.getZ(), type){
					def result = it as 	PlotCreation
					
					if(result.getStatus() == PlotCreationStatus.SUCCESS){
						def plot = result.getPlot()
						player.sendMessage(result.getStatus().getMessage())
						
						plot.reset(man.getSettings()) {
							player.sendMessage("Plot has been created")
						}
						
					}
					
				}
			}
			
		} else {
			player.sendMessage(Messages.NO_PERMISSION)
		}
		
		
		
	}

}
