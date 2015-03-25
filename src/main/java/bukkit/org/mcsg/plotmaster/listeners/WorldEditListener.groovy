package bukkit.org.mcsg.plotmaster.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.utils.NullMask
import org.mcsg.plotmaster.utils.PlatformAdapter;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.function.mask.AbstractMask
import com.sk89q.worldedit.function.mask.Mask
import com.sk89q.worldedit.function.mask.Mask2D;
import com.sk89q.worldedit.function.mask.RegionMask
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.session.SessionOwner
import com.sk89q.worldedit.util.eventbus.Subscribe;

public class WorldEditListener implements Listener{

	@Subscribe
	void edit(EditSessionEvent e){

		def location = PlatformAdapter.getPlayer(e.getActor().getName()).getLocation()
		def session = WorldEdit.getInstance().getSessionManager().get(e.getActor())
		def manager = PlotMaster.getInstance().getManager(e.getWorld().getName())


		Plot plot = manager.getPlotAt(location.getX(), location.getZ(), null)

		if(plot){
			def loc1 = plot.getMin()
			def vec1 = new Vector(loc1.getX(), loc1.getY(), loc1.getZ())
			
			def loc2 = plot.getMax()
			def vec2 = new Vector(loc2.getX(), loc2.getY(), loc2.getZ())
			
			session.setMask(new RegionMask(new CuboidRegion(vec1, vec2)))
		} else {
			session.setMask(new NullMask())
		}
	}


}
