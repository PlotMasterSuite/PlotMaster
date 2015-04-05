package bukkit.org.mcsg.plotmaster.bridge

import groovy.transform.CompileStatic;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location
import org.bukkit.entity.Player
import org.mcsg.plotmaster.bridge.PMLocation;
import org.mcsg.plotmaster.bridge.PMPlayer
import org.mcsg.plotmaster.bridge.PMVector

@CompileStatic
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
		player.sendMessage(ChatColor.translateAlternateColorCodes('&' as char, message))
	}


	@Override
	public PMLocation getLocation() {
		return new BukkitLocation(player.getLocation());
	}


	@Override
	public boolean isConsole() {
		return false;
	}


	@Override
	public boolean isOnline() {
		return player.isOnline()
	}


	public PMVector getVelocity() {
		return new BukkitVector(player.getVelocity())
	}


	public boolean isFlying() {
		player.isFlying()
	}


	public void teleport(PMLocation loc) {
		player.teleport(new Location(Bukkit.getWorld(loc.getWorld()), loc.getX(), loc.getY(), loc.getZ()))
	}

	
}
