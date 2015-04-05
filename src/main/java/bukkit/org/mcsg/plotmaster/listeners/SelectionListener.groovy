package bukkit.org.mcsg.plotmaster.listeners

import groovy.transform.CompileStatic;
import bukkit.org.mcsg.plotmaster.bridge.BukkitPlayer

import org.bukkit.Location
import org.bukkit.Material;
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent;
import org.mcsg.plotmaster.utils.Selection;

@CompileStatic
class SelectionListener implements Listener {

	@EventHandler
	void select(PlayerInteractEvent e){
		if(e.getPlayer().getItemInHand().getType() == Material.STICK){
			Player player = e.getPlayer()
			Location loc = e.getClickedBlock().getLocation()
			Action a = e.getAction()
			if(a == Action.LEFT_CLICK_BLOCK || a == Action.RIGHT_CLICK_BLOCK){
				Selection.select(player.getUniqueId().toString(), a == Action.LEFT_CLICK_BLOCK , 
					loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())
				
				new BukkitPlayer(player).sendMessage("&aSelection set at { ${loc.getX()}, ${loc.getY()}, ${loc.getZ()}}")
				e.setCancelled(true)
			}
		}
	}
	
	
}
