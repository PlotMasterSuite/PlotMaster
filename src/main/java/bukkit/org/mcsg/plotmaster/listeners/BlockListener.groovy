package bukkit.org.mcsg.plotmaster.listeners

import bukkit.org.mcsg.plotmaster.bridge.BukkitBlock
import bukkit.org.mcsg.plotmaster.bridge.BukkitLocation
import bukkit.org.mcsg.plotmaster.bridge.BukkitPlayer
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent;
import org.mcsg.plotmaster.listeners.BlockListeners;

class BlockListener implements Listener{

	
	@EventHandler
	public void blockPlace(BlockPlaceEvent e){
		
		e.setCancelled(BlockListeners.blockPlace(new BukkitPlayer(e.getPlayer()), 
			new BukkitLocation(e.getBlock().getLocation()), new BukkitBlock(e.getBlock())))
		
	}
	
	
	@EventHandler
	public void blockBreak(BlockBreakEvent e){
		
		e.setCancelled(BlockListeners.blockBreak(new BukkitPlayer(e.getPlayer()),
			new BukkitLocation(e.getBlock().getLocation()), new BukkitBlock(e.getBlock())))
		
	}
	
}
