package bukkit.org.mcsg.plotmaster.bridge

import groovy.transform.CompileStatic;
import org.bukkit.util.Vector;
import org.mcsg.plotmaster.bridge.PMVector

@CompileStatic
class BukkitVector implements PMVector{

	Vector vec
	
	public BukkitVector(Vector vec) {
		this.vec = vec;
	}
	public double getX() {
		vec.getX()
	}
	public double getY() {
		vec.getY()
	}
	public double getZ() {
		vec.getZ()
	}
	
	

	
}
