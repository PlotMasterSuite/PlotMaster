package org.mcsg.plotmaster.command;

import groovy.transform.CompileStatic;

import java.util.List;

import org.mcsg.plotmaster.PlotMaster
import org.mcsg.plotmaster.bridge.PMCommandSender
import org.mcsg.plotmaster.bridge.PMConsole;
import org.mcsg.plotmaster.bridge.PMPlayer;

/*
 *  Based on the CommandHandler framework by Double0negative
 * 
 *  https://bitbucket.org/mcsg/commandhandler
 * 
 */

@CompileStatic
public abstract class ConsoleCommand extends RootCommand{
	
	public boolean call(PMCommandSender player, List<String> args){
		if(args.size() > 0){
			def sub = subs.get(args[0].toLowerCase());
			if(sub){
				if(sub instanceof ConsoleSubCommand){
					return (sub as ConsoleSubCommand).onCommand(player, (args.size() < 2  ? new ArrayList<String>() : args[1..-1]))
				} else if(sub instanceof PlotSubCommand) {
					def p = player as PMPlayer
					def manager = PlotMaster.getInstance().getManager(p.getLocation().getWorld())
					if(!manager) {
						player.sendMessage("&cThis command must be executed from a PlotMaster enabled world!")
						return false
					}
					def subcom = ((PlotSubCommand)sub);
					if(!subcom.getPermission() || player.hasPermission(subcom.getPermission()))
						return subcom.onCommand(p, manager, (args.size() < 2 ? new ArrayList<String>() : args[1..-1]))
					else
						player.sendMessage("&cYou do not have permission for this command!")
				}
				else {
					return (sub as PlayerSubCommand).onCommand((PMPlayer)player, (args.size()  < 2 ? new ArrayList<String>() : args[1..-1]))
				}
				
			} else {
				return onCommand(player, args);
			}
		} else {
			return onCommand(player, args);
		}
	}
	
	public abstract boolean onCommand(PMCommandSender player, List<String> args);
	
	
}