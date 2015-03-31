package bukkit.org.mcsg.plotmaster.listeners

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
		def manager = getManager(e.getFrom().getWorld());
		def loc = e.getTo()
		
		//TODO: Dont load the plot
		
		def plot = manager.getPlotAt(loc.getBlockX(), loc.getBlockZ(), null)

		if(plot) {
			def mode = plot.getAccessMode()
			def player = new BukkitPlayer(e.getPlayer())
			def member = manager.getPlotMember(player)
			def level = member.getAccessLevel(plot)

			if(mode == AccessMode.ALLOW) {
				if(level == AccessLevel.DENY) {
					e.setTo(e.getFrom())
					player.sendMessage("&cYou are not allowed to enter this plot!")
				}
			} else {
				if(level.getLevel() < AccessLevel.ALLOW.getLevel()) {
					e.setTo(e.getFrom())
					player.sendMessage("&cYou are not allowed to enter this plot!")
				}
			}
		}
	}
}
