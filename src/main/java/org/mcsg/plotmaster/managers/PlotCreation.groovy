package org.mcsg.plotmaster.managers

import org.mcsg.plotmaster.Plot


class PlotCreation {


	enum PlotCreationStatus {
		SUCCESS("&aPlot created successfully."),
		PLOT_EXISTS("&cA plot already exists in this location!"),
		NO_PARENT_REGION("&cCannot create a plot without a parent region!"),
		REGION_FULL("&cCannot create plot, region has no space!"),
		OTHER("&cFailed to create plot.")

		String message;

		public PlotCreationStatus(String message){
			this.message = message;
		}
	}

	
	
	PlotCreationStatus status
	Plot plot
}
	
