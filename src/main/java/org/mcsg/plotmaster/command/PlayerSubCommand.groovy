package org.mcsg.plotmaster.command

import groovy.transform.CompileStatic;

import java.util.List;


import org.mcsg.plotmaster.bridge.PMCommandSender;
import org.mcsg.plotmaster.bridge.PMPlayer
@CompileStatic

interface PlayerSubCommand extends SubCommand{

	boolean onCommand(PMPlayer player, List<String> args);
	
	
}
