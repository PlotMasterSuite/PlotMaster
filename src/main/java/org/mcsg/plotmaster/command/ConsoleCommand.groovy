package org.mcsg.plotmaster.command;

import org.mcsg.plotmaster.bridge.PMCommandSender
import org.mcsg.plotmaster.bridge.PMPlayer;

/*
 *  Based on the CommandHandler framework by Double0negative
 * 
 *  https://bitbucket.org/mcsg/commandhandler
 * 
 */


public abstract class ConsoleCommand extends RootCommand{

	public boolean call(PMCommandSender player, String[] args){
		if(args.length > 0){
			def sub = subs.get(args[0].toLowerCase());
			if(subs){
				if(subs instanceof ConsoleSubCommand){
					return ((ConsoleSubCommand)subs).call(player, args[1..-1])
				} else {
					return ((PlayerSubCommand)subs).call(player, args[1..-1])
				}
			} else {
				return onCommand(player, player, args);
			}
		} else {
			return onCommand(player, args);
		}
	}

	public abstract boolean onCommand(PMCommandSender player, String[] args);
	

}