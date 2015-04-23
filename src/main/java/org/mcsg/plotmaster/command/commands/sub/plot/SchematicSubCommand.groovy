package org.mcsg.plotmaster.command.commands.sub.plot

import groovy.transform.CompileStatic;

import java.util.List;

import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.command.PlayerSubCommand
import org.mcsg.plotmaster.schematic.Schematic
import org.mcsg.plotmaster.utils.Selection


@CompileStatic
class SchematicSubCommand implements PlayerSubCommand{

	@Override
	public String help() {
		return "Creates a schematic";
	}

	@Override
	public boolean onCommand(PMPlayer player, List<String> args) {
		if(args.size() < 1){
			return false
		}
		
		Selection sel = Selection.getSelection(player.getUUID())
		
		if(!sel){
			player.sendMessage("&cMust create a selection!")
			return true
		}
		
		Schematic schem = sel.toSchematic()
		schem.setName(args[0])
		
		schem.save()
		
		player.sendMessage("&aSchematic created.")
		
		true
	}

	public String getCommand() {
		return "schematic <name>";
	}
	
	public String getPermission() {
		return "plot.schematic";
	}

}
