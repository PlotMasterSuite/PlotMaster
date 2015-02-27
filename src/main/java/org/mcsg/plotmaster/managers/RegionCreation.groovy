package org.mcsg.plotmaster.managers

import org.mcsg.plotmaster.Region

class RegionCreation {

	enum RegionCreationStatus {
		SUCCESS("Region created successfully."),
		REGION_EXISTS("A region already exists in this location!"),
		OTHER("Failed to create region.")

		String message;

		public RegionCreationStatus(String message){
			this.message = message;
		}


	}
	
	
	RegionCreationStatus status
	Region region
	
}
