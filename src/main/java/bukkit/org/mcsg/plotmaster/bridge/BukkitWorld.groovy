package bukkit.org.mcsg.plotmaster.bridge

import groovy.transform.CompileStatic;
import org.bukkit.World
import org.mcsg.plotmaster.bridge.PMLocation;
import org.mcsg.plotmaster.bridge.PMVector;


@CompileStatic
class BukkitWorld implements PMLocation{

	World world
	
	public BukkitWorld(World world) {
		this.world = world
	}
	
	
	public int getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getZ() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getWorld() {
		return world.getName()
	}


	public PMVector asVector() {
		return null;
	}
	
}
