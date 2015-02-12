package org.mcsg.plotmaster.managers

import org.mcsg.plotmaster.Plot


class PlotCreation {


	enum PlotCreationStatus {
		SUCCESS("Plot created successfully."),
		PLOT_EXISTS("A plot already exists in this location!"),
		NO_PARENT_REGION("Cannot create a plot without a parent region!"),
		OTHER("Failed to create plot.")

		String message;

		public CreationStatus(String message){
			this.message = message;
		}
	}

	
	
	PlotCreationStatus stuatus
	Plot plot
}
	
