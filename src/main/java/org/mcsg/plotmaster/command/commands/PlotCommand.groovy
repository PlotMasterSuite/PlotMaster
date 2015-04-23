package org.mcsg.plotmaster.command.commands;

import groovy.transform.CompileStatic;

import java.util.List;

import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.bridge.PMCommandSender;
import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.command.ConsoleCommand;
import org.mcsg.plotmaster.command.PlayerCommand
import org.mcsg.plotmaster.command.commands.sub.HelpSubCommand
import org.mcsg.plotmaster.command.commands.sub.plot.AdminSubCommand
import org.mcsg.plotmaster.command.commands.sub.plot.BorderSubCommand;
import org.mcsg.plotmaster.command.commands.sub.plot.BypassSubCommand
import org.mcsg.plotmaster.command.commands.sub.plot.ClaimSubCommand;
import org.mcsg.plotmaster.command.commands.sub.plot.ClearSubCommand;
import org.mcsg.plotmaster.command.commands.sub.plot.DeleteSubCommand;
import org.mcsg.plotmaster.command.commands.sub.plot.DenySubCommand
import org.mcsg.plotmaster.command.commands.sub.plot.HomeSubCommand;
import org.mcsg.plotmaster.command.commands.sub.plot.InfoSubCommand;
import org.mcsg.plotmaster.command.commands.sub.plot.MemberSubCommand
import org.mcsg.plotmaster.command.commands.sub.plot.ResetSubCommand;
import org.mcsg.plotmaster.command.commands.sub.plot.SchematicSubCommand;
import org.mcsg.plotmaster.command.commands.sub.plot.SettingsSubCommand
import org.mcsg.plotmaster.command.commands.sub.plot.TeleportSubCommand;

@CompileStatic
public class PlotCommand extends PlayerCommand{



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
		
		registerCommand("help", new HelpSubCommand("p", this))
		registerCommand("set", new SettingsSubCommand())
		
		registerCommand("deny", new DenySubCommand())
		registerCommand("admin", new AdminSubCommand())
		registerCommand("member", new MemberSubCommand())
		
		registerCommand("bypass", new BypassSubCommand())
	}

	@Override
	public boolean onCommand(PMPlayer player, List<String> args) {
		player.sendMessage("&aPlot Master Suite, Version ${PlotMaster.getVersion()}. By Double0negative")
		return true;
	}

	
	
	
}
