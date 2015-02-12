package org.mcsg.plotmaster.command

import org.mcsg.plotmaster.bridge.PMCommandSender;
import org.mcsg.plotmaster.bridge.PMPlayer

interface PlayerSubCommand extends SubCommand{

	boolean onCommand(PMPlayer player, String[] args);
	
	
}
