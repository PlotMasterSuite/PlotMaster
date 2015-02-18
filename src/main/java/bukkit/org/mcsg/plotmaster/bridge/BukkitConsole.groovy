package bukkit.org.mcsg.plotmaster.bridge

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender;
import org.mcsg.plotmaster.bridge.PMCommandSender
import org.mcsg.plotmaster.bridge.PMConsole
import org.mcsg.plotmaster.bridge.PMLocation;
import org.mcsg.plotmaster.bridge.PMPlayer

class BukkitConsole implements PMConsole {

	CommandSender sender


	def BukkitConsole(CommandSender sender){
		this.sender = sender
	}

	@Override
	public void sendMessage(String message) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9[PlotMaster] ${message}"))
	}

	@Override
	public boolean isConsole() {
		return true
	}

	@Override
	public String getName() {
		return "Console"
	}

	@Override
	public boolean hasPermission(String permission) {
		return true
	}

	@Override
	public void error(String msg) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[PlotMaster] ${msg}"))
	}

	@Override
	public void warn(String msg) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4[PlotMaster] ${msg}"))
	}



}
