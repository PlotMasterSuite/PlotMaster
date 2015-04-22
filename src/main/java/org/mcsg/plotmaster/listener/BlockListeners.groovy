package org.mcsg.plotmaster.listener

import groovy.transform.CompileStatic;

import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.bridge.PMBlock
import org.mcsg.plotmaster.bridge.PMLocation
import org.mcsg.plotmaster.bridge.PMPlayer

@CompileStatic
class BlockListeners {

	
	public static HashSet<String> bypass = new HashSet<>()
	

	static boolean blockPlace(PMPlayer player, PMLocation location, PMBlock block){
		def man = PlotMaster.getInstance().getManager(location.getWorld())
		def bol = man.canModifyLocation(player, location)
		
		if(!bol && !bypass.contains(player.getUUID())) {
			player.sendMessage("&aCannot modify this location!")
		}

		return !bol
	}

	static boolean blockBreak(PMPlayer player, PMLocation location, PMBlock block){
		def man = PlotMaster.getInstance().getManager(location.getWorld())
		def bol = man.canModifyLocation(player, location)

		if(!bol && !bypass.contains(player.getUUID())) {
			player.sendMessage("&aCannot modify this location!")
		}

		return !bol
	}


}
