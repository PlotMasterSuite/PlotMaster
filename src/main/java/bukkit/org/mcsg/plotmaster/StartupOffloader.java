package bukkit.org.mcsg.plotmaster;

import org.bukkit.Bukkit;
import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.utils.PlatformAdapter;
import org.mcsg.plotmaster.utils.PlatformAdapter.PlatformType;

import bukkit.org.mcsg.plotmaster.bridge.BukkitConsole;
import bukkit.org.mcsg.plotmaster.listeners.BlockListener;
import bukkit.org.mcsg.plotmaster.listeners.EntitySpawnListener;
import bukkit.org.mcsg.plotmaster.listeners.SelectionListener;

/**
 * 
 *  Offload the startup seqence to another class so we can check for Groovy from the plugin class
 *  and download it without triggering a NoClassDefFound exception at plugin load.
 * 
 * 
 * @author drew
 *
 */
public class StartupOffloader {

	static private PlotMaster plotMaster;
	
	public static void onLoad() {
		PlatformAdapter.setPlatform(PlatformType.BUKKIT);
		
		plotMaster =  new PlotMaster();

		plotMaster.onLoad(new BukkitConsole(Bukkit.getConsoleSender()));
	}
	
	
	public static void onEnable() {
		Bukkit.getPluginManager().registerEvents(new SelectionListener(), PlotMasterPlugin.getPlugin());
		Bukkit.getPluginManager().registerEvents(new EntitySpawnListener(), PlotMasterPlugin.getPlugin());
		Bukkit.getPluginManager().registerEvents(new BlockListener(), PlotMasterPlugin.getPlugin());
		
		plotMaster.onEnable();
	}
	
	
	public static void onDisable() {
		
	}
	
}
