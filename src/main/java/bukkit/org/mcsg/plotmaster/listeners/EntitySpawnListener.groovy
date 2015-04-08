package bukkit.org.mcsg.plotmaster.listeners;

import groovy.transform.CompileStatic;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.mcsg.plotmaster.PlotMaster

@CompileStatic
public class EntitySpawnListener implements Listener{
	
	@EventHandler
	public void onSpawn(CreatureSpawnEvent e){
		def manager = PlotMaster.getInstance().getManager(e.getLocation().getWorld().getName())
		
		if(manager) {
			def loc = e.getLocation()
			
			if(manager.isPlotLoaded(loc.getBlockX(), loc.getBlockZ())) {
				def plot = manager.getPlotAt(loc.getBlockX(), loc.getBlockZ(), null)
				
				if(!(manager.getSettings().get("spawn-mobs-offline-plots") ?: false) &&  !plot.isOnline()
				|| !plot.getSetting("spawn-mobs", false)) {
					e.setCancelled(true)
				}
			} else {
				if(!(manager.getSettings().get("spawn-mobs-offline-plots") ?: false)) {
					e.setCancelled(true)
				}
			}
			
		}
	}
}
