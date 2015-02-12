package org.mcsg.plotmaster.utils

interface Task {

	def priority
	
	boolean step()
	
	void onComplete()
}
