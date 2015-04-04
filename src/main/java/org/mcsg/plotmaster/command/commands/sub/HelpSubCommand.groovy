package org.mcsg.plotmaster.command.commands.sub

import java.util.List;
import java.util.Map.Entry

import org.mcsg.plotmaster.bridge.PMCommandSender;
import org.mcsg.plotmaster.command.ConsoleSubCommand;
import org.mcsg.plotmaster.command.RootCommand

class HelpSubCommand implements ConsoleSubCommand{

	RootCommand command;
	String root;
	
	public HelpSubCommand(String root, RootCommand command) {
		this.command = command
		this.root = root;
	}
	
	
	
	public String help() {
		return "Prints command help";
	}

	public boolean onCommand(PMCommandSender player, List<String> args) {
		def subs = command.getSubs()
		
		
		
		subs.entrySet().each { Entry entry ->
			player.sendMessage("&6/${root} ${entry.getKey()} 
		}
		
		
		
		
		
		
		return false;
	}
	
}
