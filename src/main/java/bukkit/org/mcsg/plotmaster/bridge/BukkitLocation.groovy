package bukkit.org.mcsg.plotmaster.bridge

import groovy.transform.CompileStatic;

import org.bukkit.Location
import org.mcsg.plotmaster.bridge.PMLocation;
import org.mcsg.plotmaster.bridge.PMVector;

@CompileStatic
class BukkitLocation implements PMLocation{

	Location location
	
	def BukkitLocation(Location loc){
		this.location = loc
	}
	
	
	@Override
	public int getX() {
		location.getBlockX()
	}

	@Override
	public int getY() {
		location.getBlockY()
	}

	@Override
	public int getZ() {
		location.getBlockZ()
	}

	@Override
	public String getWorld() {
		location.getWorld().getName()
	}


	public PMVector asVector() {
		return new BukkitVector(location.toVector())
	}

}
