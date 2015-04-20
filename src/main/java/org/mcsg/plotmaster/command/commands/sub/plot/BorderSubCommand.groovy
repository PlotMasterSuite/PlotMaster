package org.mcsg.plotmaster.command.commands.sub.plot

import groovy.transform.CompileStatic;

import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.command.PlayerSubCommand;
import org.mcsg.plotmaster.schematic.Border
import org.mcsg.plotmaster.schematic.SFace;
import org.mcsg.plotmaster.schematic.SchematicBlock
import org.mcsg.plotmaster.utils.PlatformAdapter;
import org.mcsg.plotmaster.utils.Selection;

@CompileStatic
class BorderSubCommand implements PlayerSubCommand{
	//NP: Rob Gasser - Taking Over (ft. Miyoki) NP: The Brig - Hurricane (VIP Mix)
	Map<String, Border> borders = [:]
	
	@Override
	public String help() {
		return null;
	}
	
	@Override
	public boolean onCommand(PMPlayer player, List<String> args) {
		if(args.size() > 2){
			Selection sel = Selection.getSelection(player.getUUID())
			
			if(!sel){
				player.sendMessage("&cMust create a selection!")
				return true
			}
			
			def name = args[0]
			def face = SFace.fromString(args[1])
			def border = borders.get(name) ?: new Border(name: name)
			def schem = sel.toSchematic()
			
			schem.setWidth(args[2].toInteger())
			border.setBorder(face, schem)
			player.sendMessage("&aSuccessfully set ${face} of ${name}")
			
			borders.put(name, border)
			
			return true
		} else if(args.size() > 1) {
			if(args[0] == "load") {
				def border = Border.load(args[1])
				def loc = player.getLocation()
				def mx = loc.getX(), my = loc.getY(), mz = loc.getZ()
				if(border) {
					for(SFace face : SFace.values()) {
						def s = border.borders.get(face)
						
						SchematicBlock[][][] blocks = s.getBlocks()
						
						def lx
						
						PlatformAdapter.createSign(loc.getWorld(), mx + blocks.length / 2, my + 1, mz - 2, "", face.toString())
						
						for(int x = 0; x < blocks.length; x++) {
							for(int z = 0; z < blocks[x].length; z++) {
								for(int y = 0; y < blocks[x][z].length; y++) {
									def block = blocks[x][z][y]
									PlatformAdapter.createBlockUpdate(loc.getWorld(),mx + x ,my + y , z + mz, block.getMaterial(), block.getData()).update() 
								}
							}
							lx = x
						}
						mx = lx + mx + 5
					}
				}
			}
		}
	}
	
	public String getCommand() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getPermission() {
		return "plotm";
	}
	
}
