package org.mcsg.plotmaster.bridge

interface PMPlayer extends PMCommandSender {

	
	String getUUID()
		
	PMLocation getLocation()
	
	boolean isOnline()
	
	PMVector getVelocity()
	
	boolean isFlying()
	
	void teleport(PMLocation loc)
}
