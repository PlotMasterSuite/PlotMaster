package org.mcsg.plotmaster.command;

import org.mcsg.plotmaster.bridge.PMPlayer;


/*
 *  Based on the CommandHandler framework by Double0negative
 * 
 *  https://bitbucket.org/mcsg/commandhandler
 * 
 */
public abstract class PlayerCommand extends RootCommand{

	public boolean call(PMPlayer player, String[] args){
		if(args.length > 0){
			if(subs.containsKey(args[0].toLowerCase())){
				return subs.get(args[0].toLowerCase()).onCommand(player, args[1..-1]);
			} else {
				return onCommand(player, player, args);
			}
		} else {
			return onCommand(player, args);
		}
	}

	public abstract boolean onCommand(PMPlayer player, String[] args);
	

}