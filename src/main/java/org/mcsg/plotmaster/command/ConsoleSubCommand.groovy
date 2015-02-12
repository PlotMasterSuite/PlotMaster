package org.mcsg.plotmaster.command

import org.mcsg.plotmaster.bridge.PMCommandSender;

interface ConsoleSubCommand extends SubCommand {
	
	boolean onCommand(PMCommandSender player, String[] args);
	
}
