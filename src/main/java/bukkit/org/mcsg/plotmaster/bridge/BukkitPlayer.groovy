package bukkit.org.mcsg.plotmaster.bridge

import org.bukkit.ChatColor;
import org.bukkit.entity.Player
import org.mcsg.plotmaster.bridge.PMLocation;
import org.mcsg.plotmaster.bridge.PMPlayer

class BukkitPlayer implements PMPlayer{

	Player player
	
	def BukkitPlayer(Player player){
		this.player = player
	}
	
	
	@Override
	public String getName() {
		return player.getName()
	}

	@Override
	public String getUUID() {
		return player.getUniqueId().toString()
	}

	@Override
	public boolean hasPermission(String perm) {
		return player.hasPermission(perm)
	}

	@Override
	public void sendMessage(String message) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&' as char, "${message}"))
	}


	@Override
	public PMLocation getLocation() {
		return new BukkitLocation(player.getLocation());
	}


	@Override
	public boolean isConsole() {
		return false;
	}

	
}
