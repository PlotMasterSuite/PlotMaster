package org.mcsg.plotmaster.utils

import static java.lang.Math.*

class LocationUtils {

	static boolean isInRegion(int posx, int posz, int x, int z, int x2, int z2){
		def bol = posx >= min(x, x2) && posx <= max(x, x2) && posz >= min(z, z2) && posz <= max(z, z2)
		
		println "posx:${posx}, posz:${posz}, x:${x}, z:${z}, x2: ${x2}, z2: ${z2}, bol: ${bol}"
		
		
		return bol
	}
	
		
}
