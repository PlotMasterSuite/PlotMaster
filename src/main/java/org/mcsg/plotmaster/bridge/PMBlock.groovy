package org.mcsg.plotmaster.bridge

import org.mcsg.plotmaster.utils.Material

interface PMBlock {

	PMLocation getLocation()
	
	Material getMaterial()
	
	byte getData()
	
}
