package org.mcsg.plotmaster.command.commands.sub.plot

import groovy.swing.factory.ColumnFactory;

import java.util.List;

import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.bridge.PMPlayer;
import org.mcsg.plotmaster.command.PlayerSubCommand
import org.mcsg.plotmaster.command.PlotSubCommand
import org.mcsg.plotmaster.managers.PlotManager;
import org.mcsg.plotmaster.managers.grid.GridManager;
import org.mcsg.plotmaster.schematic.Border;
import org.mcsg.plotmaster.schematic.SchematicBlock

class InfoSubCommand implements PlotSubCommand{

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCommand(PMPlayer player, PlotManager man,List<String> args) {
		def loc = player.getLocation()
		man.getPlotAt(loc.getX(), loc.getZ()) { Plot plot ->

			if(plot) {

				player.sendMessage "&6--[ X: ${plot.getX()}, Z: ${plot.getZ()} ]--"
				player.sendMessage "&aOwner: ${plot.getOwnerName()}"
				player.sendMessage "&aRegion: ${plot.getRegion().getX()}, ${plot.getRegion().getZ()}"
			} else {
				player.sendMessage "&cNo plot found"
			}
			def manager = man as GridManager
			def border = Border.load(manager.getSettings().grid.border)
			player.sendMessage("\nDEBUG:\n&eBorder: ${border.getName()}")
			player.sendMessage("    &eWidth: ${border.getWidth()}")

			def posx = Math.abs(loc.getX() % (manager.cellWidth))
			def posz = Math.abs(loc.getZ() % (manager.cellHeight))

			if(loc.getX() < 0){
				posx = manager.getCellWidth() - posx
			}
			
			if(loc.getZ() < 0){
				posz = manager.getCellHeight() - posz
			}
			
			def face = border.getFace(posx, posz, manager.cellWidth, manager.cellHeight)


			SchematicBlock[] blocks = border.getColumnAt(loc.getX(), loc.getZ(), manager.getSettings().grid.width.toInteger(),manager.getSettings().grid.height.toInteger())
			player.sendMessage("    &eface: ${face.toString()}, posx: ${posx}, posz: ${posz}")
			player.sendMessage("    &eColumn: ${getMaterialString(blocks)}")


		}
	}

	def getMaterialString(SchematicBlock[] blocks){
		StringBuilder sb = new StringBuilder()
		blocks.each {
			if(it)
				sb.append(it.material+"  ")
		}
		return sb.toString()
	}

	public String getCommand() {
		// TODO Auto-generated method stub
		return null;
	}


}
