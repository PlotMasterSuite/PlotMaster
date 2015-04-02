package bukkit.org.mcsg.plotmaster.listeners

import bukkit.org.mcsg.plotmaster.bridge.BukkitBlock
import bukkit.org.mcsg.plotmaster.bridge.BukkitLocation
import bukkit.org.mcsg.plotmaster.bridge.BukkitPlayer

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.eclipse.jetty.io.ArrayByteBufferPool.Bucket;
import org.mcsg.plotmaster.listener.BlockListeners;

class BlockListener implements Listener{
	
	
	@EventHandler
	public void BlockPlace(BlockPlaceEvent e){
		
		e.setCancelled(BlockListeners.blockPlace(new BukkitPlayer(e.getPlayer()),
				new BukkitLocation(e.getBlock().getLocation()), new BukkitBlock(e.getBlock())))
		
	}
	
	
	@EventHandler
	public void BlockBreak(BlockBreakEvent e){
		
		e.setCancelled(BlockListeners.blockBreak(new BukkitPlayer(e.getPlayer()),
				new BukkitLocation(e.getBlock().getLocation()), new BukkitBlock(e.getBlock())))
		
	}
	
	@EventHandler
	public void Interact(PlayerInteractEvent e) {
		
		def player = new BukkitPlayer(e.getPlayer());
		def loc = new BukkitLocation(e.getClickedBlock()?.getLocation())
		def block = new BukkitBlock(e.getClickedBlock())
		
		if(block && loc)
			e.setCancelled(BlockListeners.blockBreak(player, loc, block) || BlockListeners.blockPlace(player,  loc, block))
		
		
	}
	
	
	
	
	
}
