package org.mcsg.plotmaster.command;

import groovy.transform.CompileStatic;

import java.util.List;

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
		player.sendMessage(args.toString())
		if(args.size() > 0){
			def sub = subs.get(args[0].toLowerCase());
			if(sub){
				if(sub instanceof ConsoleSubCommand){
					return (sub as ConsoleSubCommand).onCommand(player, (args.size() ? new ArrayList<String>() : args[1..-1]))
				} else {
					return (sub as PlayerSubCommand).onCommand((PMPlayer)player, (args.size() ? new ArrayList<String>() : args[1..-1]))
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