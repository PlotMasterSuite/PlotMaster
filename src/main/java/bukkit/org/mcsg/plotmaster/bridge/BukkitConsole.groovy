package bukkit.org.mcsg.plotmaster.bridge

import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender;
import org.mcsg.plotmaster.bridge.PMCommandSender
import org.mcsg.plotmaster.bridge.PMLocation;
import org.mcsg.plotmaster.bridge.PMPlayer

class BukkitConsole implements PMCommandSender {

	CommandSender sender
	
	
	def BukkitConsole(CommandSender sender){
		this.sender = sender
	}

	@Override
	public void sendMessage(String message) {
		sender.sendMessage(message)
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
	
	

}
