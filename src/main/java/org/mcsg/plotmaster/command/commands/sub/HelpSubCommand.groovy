package org.mcsg.plotmaster.command.commands.sub

import java.util.List;
import java.util.Map.Entry

import org.mcsg.plotmaster.bridge.PMCommandSender;
import org.mcsg.plotmaster.command.ConsoleSubCommand;
import org.mcsg.plotmaster.command.RootCommand
import org.mcsg.plotmaster.command.SubCommand

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
		def coms = new HashSet(subs.values())
		
		
		
		coms.each { SubCommand com ->
			player.sendMessage("&6/${root} ${com.getCommand()}: &7 ${com.help()} ")
		}
		return false;
	}



	public String getCommand() {
		return "help";
	}



	public String getPermission() {
		return null;
	}
	
	
}
