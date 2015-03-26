package org.mcsg.plotmaster

enum AccessLevel {

	OWNER(10), ADMIN(5), MEMBER(2), ALLOW(1), NONE(0), DENY(-1)
	 
	int level
	
	AccessLevel(int level){
		this.level = level
	}
	
	int getLevel(){
		return this.level
	}
}
