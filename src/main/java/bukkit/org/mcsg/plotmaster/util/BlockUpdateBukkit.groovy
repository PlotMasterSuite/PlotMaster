package bukkit.org.mcsg.plotmaster.util

import org.bukkit.Bukkit;
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.mcsg.plotmaster.utils.BlockUpdate

class BlockUpdateBukkit implements BlockUpdate{

	Location loc
	
	Material type
	byte data
	
	
	
	def BlockUpdateBukkit(String world, int x, int y, int z, String type, byte data){
		this.loc = new Location(Bukkit.getWorld(world), x, y, z)
		
		this.type = Material.valueOf(type.toUpperCase())
		this.data = data
	}
	
	def BlockUpdateBukkit(String world, int x, int y, int z) {
		this.loc = new Location(Bukkit.getWorld(world), x, y, z)
	}
	
	
	void update(){
		loc.getBlock().setType(type)
		loc.getBlock().setData(data)
	}
	
}
