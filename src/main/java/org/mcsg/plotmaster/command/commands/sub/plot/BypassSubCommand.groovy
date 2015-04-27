package org.mcsg.plotmaster.command.commands.sub.plot

import java.util.List;

import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.command.PlayerSubCommand;
import org.mcsg.plotmaster.listener.BlockListeners;

class BypassSubCommand implements PlayerSubCommand{
	
	public String help() {
		return "Allows you to place, break and worldedit blocks anywhere";
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
			player.sendMessage("&aYou can now edit anywhere!")
		} else {
			BlockListeners.bypass.remove(player.getUUID())
			player.sendMessage("&6Bypass disabled")
		}
	}
	
}
