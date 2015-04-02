package bukkit.org.mcsg.plotmaster.listeners

import bukkit.org.mcsg.plotmaster.bridge.BukkitLocation
import bukkit.org.mcsg.plotmaster.bridge.BukkitPlayer

import org.bukkit.World
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent;
import org.mcsg.plotmaster.Plot.AccessMode;
import org.mcsg.plotmaster.AccessLevel;
import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.managers.PlotManager

class PlayerListener implements Listener{
	
	private PlotManager getManager(World world){
		return PlotMaster.getInstance().getManager(world.getName())
	}
	
	
	@EventHandler
	void PlayerMove(PlayerMoveEvent e){
		def manager = getManager(e.getFrom().getWorld())
		def player = new BukkitPlayer(e.getPlayer())
		def can = manager.canEnterLocation(player, new BukkitLocation(e.getTo()))
		
		println can
		
		if(!can) {
			e.setTo(e.getFrom())
			player.sendMessage("&cYou are not allowed to enter this plot!")
		}
	}
}
