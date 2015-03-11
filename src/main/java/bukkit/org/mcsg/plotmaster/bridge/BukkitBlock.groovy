package bukkit.org.mcsg.plotmaster.bridge

import org.bukkit.block.Block
import org.mcsg.plotmaster.bridge.PMBlock
import org.mcsg.plotmaster.bridge.PMLocation;
import org.mcsg.plotmaster.utils.Material;

class BukkitBlock implements PMBlock{

	Block block
	
	BukkitBlock(Block block){
		this.block = block
	}
	
	
	@Override
	public PMLocation getLocation() {
		return new BukkitLocation(block.getLocation())
	}

	@Override
	public Material getMaterial() {
		return Material.valueOf(block.getType().toString())
	}

	@Override
	public byte getData() {
		return block.getData()
	}

}
