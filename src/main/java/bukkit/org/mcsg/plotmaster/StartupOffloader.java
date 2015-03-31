package bukkit.org.mcsg.plotmaster;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.bridge.PMCommandSender;
import org.mcsg.plotmaster.command.CommandHandler;
import org.mcsg.plotmaster.utils.PlatformAdapter;
import org.mcsg.plotmaster.utils.PlatformAdapter.PlatformType;

import com.sk89q.worldedit.WorldEdit;

import de.greenrobot.event.EventBus;
import bukkit.org.mcsg.plotmaster.bridge.BukkitCommand;
import bukkit.org.mcsg.plotmaster.bridge.BukkitConsole;
import bukkit.org.mcsg.plotmaster.bridge.BukkitPlayer;
import bukkit.org.mcsg.plotmaster.listeners.BlockListener;
import bukkit.org.mcsg.plotmaster.listeners.EntitySpawnListener;
import bukkit.org.mcsg.plotmaster.listeners.PlayerListener;
import bukkit.org.mcsg.plotmaster.listeners.PlotMasterListener;
import bukkit.org.mcsg.plotmaster.listeners.SelectionListener;
import bukkit.org.mcsg.plotmaster.listeners.WorldEditListener;

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
		Bukkit.getPluginManager().registerEvents(new BlockListener(), PlotMasterPlugin.getPlugin());
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), PlotMasterPlugin.getPlugin());
		
		plotMaster.registerEvent(new PlotMasterListener());

		try {
			if(Integer.parseInt(Bukkit.getPluginManager().getPlugin("WorldEdit").getDescription().getVersion().substring(0, 1)) < 5) {
				plotMaster.getConsole().warn("Your WorldEdit version is outdated. Please update to version 6.");
				plotMaster.getConsole().warn("PlotMaster will not be able to restrict WorldEdit to players plots!!!");
			} else {
				WorldEdit.getInstance().getEventBus().register(new WorldEditListener());
			}
		} catch (Exception e){
		}

		plotMaster.onEnable();
	}


	public static void onDisable() {
		plotMaster.onDisable();
	}

	public static boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		BukkitCommand command = new BukkitCommand(cmd);
		PMCommandSender send = null;

		if(sender instanceof Player){
			send = new BukkitPlayer((Player) sender);
		} else if (sender instanceof ConsoleCommandSender){
			send = new BukkitConsole(sender);
		} else {
			throw new RuntimeException("Invalid sender");
		}
		CommandHandler.sendCommand(send, command, args);

		return true;
	}

}
