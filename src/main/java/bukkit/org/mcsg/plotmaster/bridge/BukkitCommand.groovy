package bukkit.org.mcsg.plotmaster.bridge

import groovy.transform.CompileStatic;

import org.bukkit.command.Command
import org.mcsg.plotmaster.bridge.PMCommand
@CompileStatic
class BukkitCommand implements PMCommand{

	Command cmd
	
	def BukkitCommand(Command cmd){
		this.cmd = cmd
	}
	
	@Override
	public String getCommand() {
		return cmd.getName()
	}

	@Override
	public String getLabel() {
		return cmd.getLabel()
	}

	@Override
	public String getDescription() {
		return cmd.getDescription()
	}
	
}
