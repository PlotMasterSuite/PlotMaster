package bukkit.org.mcsg.plotmaster.util

import groovy.transform.CompileStatic;

import org.bukkit.Bukkit;
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.mcsg.plotmaster.utils.BlockUpdate


@CompileStatic
class BukkitBlockUpdate implements BlockUpdate{

	Location loc

	Material type
	byte data



	def BukkitBlockUpdate(String world, int x, int y, int z, String type, byte data){
		this.loc = new Location(Bukkit.getWorld(world), x, y, z)

		Material material = Material.valueOf(type.toUpperCase())

		this.type = material
		this.data = data

	}

	def BukkitBlockUpdate(String world, int x, int y, int z) {
		this.loc = new Location(Bukkit.getWorld(world), x, y, z)
	}


	void update(){
		if(!(loc.getBlock().getType() == type)){
			loc.getBlock().setType(type)
			loc.getBlock().setData(data)
		}

	}

}
