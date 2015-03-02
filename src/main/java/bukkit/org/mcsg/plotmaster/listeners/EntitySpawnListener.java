package bukkit.org.mcsg.plotmaster.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class EntitySpawnListener implements Listener{

	@EventHandler
	public void onSpawn(CreatureSpawnEvent e){
		e.setCancelled(true);		
	}
	
	
	
}
