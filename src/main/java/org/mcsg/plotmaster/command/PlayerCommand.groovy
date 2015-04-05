package org.mcsg.plotmaster.command;

import groovy.transform.CompileStatic;

import java.util.List;

import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.bridge.PMPlayer;


/*
 *  Based on the CommandHandler framework by Double0negative
 * 
 *  https://bitbucket.org/mcsg/commandhandler
 * 
 */

@CompileStatic
public abstract class PlayerCommand extends RootCommand{
	
	public boolean call(PMPlayer player, List<String> args){
		if(args.size() > 0){
			def sub = subs.get(args[0].toLowerCase());
			if(sub){
				assert !(sub instanceof ConsoleSubCommand), "Cannot invoke a ConsoleSubCommand from a PlayerCommand!"
				
				if(sub instanceof PlayerSubCommand) {
					def subcom = ((PlayerSubCommand)sub);
					if(!subcom.getPermission() || player.hasPermission(subcom.getPermission()))
						return subcom.onCommand(player, (args.size() < 2 ? new ArrayList<String>() : args[1..-1]))
					else
						player.sendMessage("&cYou do not have permission for this command!")
				} else if(sub instanceof PlotSubCommand) {
					def manager = PlotMaster.getInstance().getManager(player.getLocation().getWorld())
					if(!manager) {
						player.sendMessage("&cThis command must be executed from a PlotMaster enabled world!")
						return false
					}
					def subcom = ((PlotSubCommand)sub);
					if(!subcom.getPermission() || player.hasPermission(subcom.getPermission()))
						return subcom.onCommand(player, manager, (args.size() < 2 ? new ArrayList<String>() : args[1..-1]))
					else
						player.sendMessage("&cYou do not have permission for this command!")
				}
			}
			else {
				return onCommand(player, args);
			}
		} else {
			return onCommand(player, args);
		}
		
	}
	
	public abstract boolean onCommand(PMPlayer player, List<String> args);
	
	
}