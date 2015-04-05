package org.mcsg.plotmaster.command

import groovy.transform.CompileStatic;

import org.mcsg.plotmaster.bridge.PMCommand
import org.mcsg.plotmaster.bridge.PMCommandSender
import org.mcsg.plotmaster.bridge.PMPlayer
import org.mcsg.plotmaster.command.commands.PlotCommand
import org.mcsg.plotmaster.command.commands.TestCommand;

@CompileStatic
class CommandHandler {


	static Map<String, RootCommand> commands = [:]

	
	static load(){
		registerCommand("plot", new PlotCommand())
	}
	

	static registerCommand(String label, RootCommand command) {
		commands.put(label, command)
	}


	static boolean sendCommand(PMCommandSender sender, PMCommand cmd, String ... args){
		RootCommand command = commands.get(cmd.getLabel())

		if(command){
			if(command instanceof ConsoleCommand){
				return command.call(sender, Arrays.asList(args))
			}
			else {
				if(sender instanceof PMPlayer){
					PMPlayer player = sender as PMPlayer
					return (command as PlayerCommand).call(player, Arrays.asList(args))
				} else {
					sender.sendMessage("&aThis command cannot be called from console!")
				}
			}
		}
		return true
	}
}
