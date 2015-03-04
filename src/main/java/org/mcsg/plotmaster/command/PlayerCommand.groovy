package org.mcsg.plotmaster.command;

import groovy.transform.CompileStatic;

import java.util.List;

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
		player.sendMessage(args.toString())
		if(args.size() > 0){
			def sub = subs.get(args[0].toLowerCase());
			if(sub){
				assert sub instanceof PlayerSubCommand, "Cannot invoke a ConsoleSubCommand from a ConsoleCommand!"
				return ((PlayerSubCommand)sub).onCommand(player, (args.size() ? new ArrayList<String>() : args[1..-1]))
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