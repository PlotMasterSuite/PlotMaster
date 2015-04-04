package org.mcsg.plotmaster.command.commands;

import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.bridge.PMCommandSender;
import org.mcsg.plotmaster.command.ConsoleCommand;
import org.mcsg.plotmaster.command.commands.sub.plot.BorderSubCommand;
import org.mcsg.plotmaster.command.commands.sub.plot.ClaimSubCommand;
import org.mcsg.plotmaster.command.commands.sub.plot.ClearSubCommand;
import org.mcsg.plotmaster.command.commands.sub.plot.DeleteSubCommand;
import org.mcsg.plotmaster.command.commands.sub.plot.HomeSubCommand;
import org.mcsg.plotmaster.command.commands.sub.plot.InfoSubCommand;
import org.mcsg.plotmaster.command.commands.sub.plot.ResetSubCommand;
import org.mcsg.plotmaster.command.commands.sub.plot.SchematicSubCommand;
import org.mcsg.plotmaster.command.commands.sub.plot.TeleportSubCommand;

public class PlotCommand extends ConsoleCommand{

	@Override
	public boolean onCommand(PMCommandSender player, List<String> args) {
		player.sendMessage("&aPlot Master Suite, Version ${PlotMaster.getVersion()}. By Double0negative")
		return true;
	}

	@Override
	public void registerCommands() {
		registerCommand("border", new BorderSubCommand())
		registerCommand("claim", new ClaimSubCommand())
		registerCommand("clear", new ClearSubCommand())
		registerCommand("reset", new ResetSubCommand())
		registerCommand("schematic", new SchematicSubCommand())
		registerCommand("info", new InfoSubCommand())
		registerCommand("delete", new DeleteSubCommand())
		def teleport = new TeleportSubCommand()
		registerCommand("teleport", teleport)
		registerCommand("tp", teleport)
		registerCommand("home", new HomeSubCommand())
	}

	
	
	
}
