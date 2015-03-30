package org.mcsg.plotmaster.events

import org.mcsg.plotmaster.Plot

class PlotLoadEvent  implements Cancellable{

	boolean cancelled
	
	Plot plot
	
	
	public boolean isCancelled() {
		return false;
	}


}
