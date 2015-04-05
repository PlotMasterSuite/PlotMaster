package bukkit.org.mcsg.plotmaster.listeners

import groovy.transform.CompileStatic;
import bukkit.org.mcsg.plotmaster.bridge.BukkitLocation
import bukkit.org.mcsg.plotmaster.bridge.BukkitPlayer
import bukkit.org.mcsg.plotmaster.bridge.BukkitWorld

import org.bukkit.World
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mcsg.plotmaster.Plot.AccessMode;
import org.mcsg.plotmaster.AccessLevel;
import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.events.PlayerEnterPlotEvent;
import org.mcsg.plotmaster.managers.PlotManager


@CompileStatic
class PlayerListener implements Listener{
	
	private PlotManager getManager(World world){
		return PlotMaster.getInstance().getManager(world.getName())
	}
	
	Map<String, Boolean> isInPlot = [:]
	
	@EventHandler
	void PlayerMove(PlayerMoveEvent e){
		def manager = getManager(e.getFrom().getWorld())
		def player = new BukkitPlayer(e.getPlayer())
		def can = manager.canEnterLocation(player, new BukkitLocation(e.getTo()))
		
		if(!can) {
			e.setTo(e.getFrom())
			player.sendMessage("&cYou are not allowed to enter this plot!")
		}
		
		
		manager.isInPlotFast(player) { boolean is ->
			if(is && !isInPlot[player.getUUID()]) {
				manager.getPlotAt(player.getLocation()) { Plot plot ->
					PlayerEnterPlotEvent ev = new PlayerEnterPlotEvent(plot: plot, player: player)
					PlotMaster.getInstance().fireEvent(ev)
				}
			}
			isInPlot[player.getUUID()] = is
		}
	}
	
	@EventHandler
	void PlayerChangeWorld(PlayerChangedWorldEvent e){
		getManager(e.getFrom()).playerOffline(new BukkitPlayer(e.getPlayer()))
		getManager(e.getPlayer().getWorld()).playerOnline(new BukkitPlayer(e.getPlayer()))
	}
	
	@EventHandler
	void PlayerJoin(PlayerJoinEvent e) {
		getManager(e.getPlayer().getWorld()).playerOnline(new BukkitPlayer(e.getPlayer()))
	}
	
	@EventHandler
	void PlayerQuit(PlayerQuitEvent e) {
		getManager(e.getPlayer().getWorld()).playerOffline(new BukkitPlayer(e.getPlayer()))
	}
}
