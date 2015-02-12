package org.mcsg.plotmaster.command

import org.mcsg.plotmaster.bridge.PMCommandSender
import org.mcsg.plotmaster.bridge.PMPlayer

class CommandHandler {

	
	static Map<String, RootCommand> commands = [:]
	
	
	static registerCommand(String label, RootCommand command) {
		commands.put(label, command)
	}
	
	
	static sendCommand(PMCommandSender sender, String label, String ... args){
		
		
		
	}
	

}
