package org.mcsg.plotmaster.command.commands.sub

import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.command.PlayerSubCommand;
import org.mcsg.plotmaster.schematic.Border
import org.mcsg.plotmaster.schematic.SFace;
import org.mcsg.plotmaster.utils.Selection;

class BorderSubCommand implements PlayerSubCommand{
	//NP: Rob Gasser - Taking Over (ft. Miyoki) NP: The Brig - Hurricane (VIP Mix)
	Map<String, Border> borders = [:]

	@Override
	public String help() {
		return null;
	}

	@Override
	public boolean onCommand(PMPlayer player, List<String> args) {
		if(args.size() < 2){
			return false
		}
		
		Selection sel = Selection.getSelection(player.getUUID())
		
		if(!sel){
			player.sendMessage("&cMust create a selection!")
			return true
		}
		
		def name = args[0]
		def face = SFace.fromString(args[1])
		def border = borders.get(name) ?: new Border(name: name)
		
		border.setBorder(face, sel.toSchematic())
		player.sendMessage("&aSuccessfully set ${face} of ${name}")
		
		borders.put(name, border)
				
		return true
	}

}
