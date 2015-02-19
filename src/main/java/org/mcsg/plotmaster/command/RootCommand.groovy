package org.mcsg.plotmaster.command;

import groovy.transform.CompileStatic;

import java.util.HashMap;

import org.mcsg.plotmaster.bridge.PMCommandSender
import org.mcsg.plotmaster.bridge.PMPlayer;


/*
 *  Based on the CommandHandler framework by Double0negative
 * 
 *  https://bitbucket.org/mcsg/commandhandler
 * 
 */
@CompileStatic

public abstract class RootCommand {

	HashMap<String, SubCommand> subs = new HashMap<String, SubCommand>();

	public RootCommand(){
		registerCommands();
	}
	
	public void registerCommand(String name, SubCommand command){
		subs.put(name, command);
	}
	
	
	public void listSubCommands(PMCommandSender sender){
		
	}
	
	
	public abstract void registerCommands();
	
}