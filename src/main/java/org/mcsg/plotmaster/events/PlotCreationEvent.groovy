package org.mcsg.plotmaster.events

import org.mcsg.plotmaster.Plot;

class PlotCreationEvent  implements Cancellable{

	boolean cancelled
	
	Plot plot
	
	String message
	
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

}
