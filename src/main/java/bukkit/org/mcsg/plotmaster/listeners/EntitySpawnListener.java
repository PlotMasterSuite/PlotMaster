package bukkit.org.mcsg.plotmaster.listeners;

import groovy.transform.CompileStatic;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

@CompileStatic
public class EntitySpawnListener implements Listener{

	@EventHandler
	public void onSpawn(CreatureSpawnEvent e){
		e.setCancelled(true);		
	}
	
	
	
}
