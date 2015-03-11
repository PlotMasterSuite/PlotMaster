package org.mcsg.plotmaster

enum AccessLevel {

	OWNER(10), ADMIN(5), MEMBER(1), ALLOW(0), DENY(-1)
	 
	int level
	
	AccessLevel(int level){
		this.level = level
	}
	
	int getLevel(){
		return this.level
	}
}
