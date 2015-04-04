package org.mcsg.plotmaster.command

import groovy.transform.CompileStatic;

/*
 *  Based on the CommandHandler framework by Double0negative
 *
 *  https://bitbucket.org/mcsg/commandhandler
 *
 */
@CompileStatic

public interface SubCommand {

	public String help();
	
	public String getCommand();
}

