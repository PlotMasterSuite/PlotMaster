package org.mcsg.plotmaster.bridge

interface PMCommandSender extends PMMessageable{

	boolean isConsole()
	
	String getName()

	boolean hasPermission(String permission)
		
}
