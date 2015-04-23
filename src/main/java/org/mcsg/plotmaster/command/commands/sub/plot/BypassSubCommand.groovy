package org.mcsg.plotmaster.command.commands.sub.plot

import java.util.List;

import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.command.PlayerSubCommand;
import org.mcsg.plotmaster.listener.BlockListeners;

class BypassSubCommand implements PlayerSubCommand{
	
	public String help() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getCommand() {
		return "bypass";
	}
	
	public String getPermission() {
		return "plot.access.bypass"
	}
	
	public boolean onCommand(PMPlayer player, List<String> args) {
		if(!BlockListeners.bypass.contains(player.getUUID())) {
			BlockListeners.bypass.add(player.getUUID())
		} else {
			BlockListeners.bypass.remove(player.getUUID())
		}
	}
	
}
