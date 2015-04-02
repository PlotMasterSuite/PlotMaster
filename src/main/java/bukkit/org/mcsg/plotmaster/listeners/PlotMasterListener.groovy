package bukkit.org.mcsg.plotmaster.listeners

import bukkit.org.mcsg.plotmaster.bridge.BukkitBlock;
import bukkit.org.mcsg.plotmaster.bridge.BukkitLocation
import bukkit.org.mcsg.plotmaster.util.EntityData

import org.bukkit.Bukkit;
import org.bukkit.Location
import org.bukkit.World
import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.events.PlayerEnterPlotEvent
import org.mcsg.plotmaster.events.PlotOfflineEvent;
import org.mcsg.plotmaster.events.PlotOnlineEvent;

import com.google.common.eventbus.Subscribe;

class PlotMasterListener {

	@Subscribe
	public void PlotLoad(PlotOnlineEvent e) {
		Plot plot = e.getPlot()
		
		if(plot.getSetting("save-entities") as boolean) {
			e.getPlot().getMetaData("entites")?.each { EntityData data ->
			
				Location loc = new Location(Bukkit.getWorld(plot.getWorld()), data.x, data.y, data.z)
				loc.getWorld().spawnEntity(loc, data.type)
					
			}
		}
	}
	
	@Subscribe
	public void PlotUnload(PlotOfflineEvent e){
		Plot plot = e.getPlot()
		World w = Bukkit.getWorld(plot.getWorld())
		
		def save = plot.getSetting("save-entities")
		def savelist = []
		
		if(plot.getSetting("clear-entities-unload")) {
			def ents = w.getEntities()
			
			ents.each {
				def loc = it.getLocation()
				if(plot.isPartOf(loc.getBlockX(), loc.getBlockZ())) {
					if(save) {
						savelist.add(new EntityData(type: it.getType(), x: loc.getBlockX(), y: loc.getBlockY(), z: loc.getBlockZ()))
					}
					it.remove()
				}
			}
			
			if(save){
				plot.setMetaData("entities", savelist)
			}
		}
	}
	
}
