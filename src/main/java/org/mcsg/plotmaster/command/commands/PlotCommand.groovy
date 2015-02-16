package org.mcsg.plotmaster.command.commands;

import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.bridge.PMCommandSender;
import org.mcsg.plotmaster.command.ConsoleCommand;

public class PlotCommand extends ConsoleCommand{

	@Override
	public boolean onCommand(PMCommandSender player, String... args) {
		player.sendMessage("&aPlot Master Suite, Version ${PlotMaster.getVersion()}. By Double0negative")
		return true;
	}

	@Override
	public void registerCommands() {
		
		
	}

	
	
	
}
