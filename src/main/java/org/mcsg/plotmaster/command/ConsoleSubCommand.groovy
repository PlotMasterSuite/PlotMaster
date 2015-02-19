package org.mcsg.plotmaster.command

import groovy.transform.CompileStatic;

import org.mcsg.plotmaster.bridge.PMCommandSender;

@CompileStatic
interface ConsoleSubCommand extends SubCommand {
	
	boolean onCommand(PMCommandSender player, List<String> args);
	
}
