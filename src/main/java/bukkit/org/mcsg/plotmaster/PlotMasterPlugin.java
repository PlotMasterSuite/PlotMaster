package bukkit.org.mcsg.plotmaster;


import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcsg.plotmaster.bridge.PMCommandSender;
import org.mcsg.plotmaster.command.CommandHandler;

import bukkit.org.mcsg.plotmaster.bridge.BukkitCommand;
import bukkit.org.mcsg.plotmaster.bridge.BukkitConsole;
import bukkit.org.mcsg.plotmaster.bridge.BukkitPlayer;

public class PlotMasterPlugin extends JavaPlugin{

	private static PlotMasterPlugin plugin;

	private boolean hasGroovy = false;

	public PlotMasterPlugin(){

		if(Bukkit.getPluginManager().getPlugin("GroovyRuntime") == null) {
			sendConsoleMessage(ChatColor.YELLOW+"GroovyRuntime is required to use PlotMaster. It will be automatically downloaded");
			sendConsoleMessage(ChatColor.YELLOW+"Alternatively, you can stop the server and go to");
			sendConsoleMessage(ChatColor.YELLOW+"http://ci.mc-sg.org/job/GroovyRuntime/lastBuild/org.mcsg.groovy$GroovyRuntime/");
			for(int a = 10; a > 0; a--){
				if(a == 10 || a < 6)
					sendConsoleMessage(ChatColor.GREEN+"Downloading in "+a+" seconds.");
				try{ Thread.sleep(1000); } catch (Exception e){}
			}
			sendConsoleMessage(ChatColor.GREEN+"Downloading...");
			try{
				URL url = new URL("http://ci.mc-sg.org/job/GroovyRuntime/lastBuild/org.mcsg.groovy$GroovyRuntime/artifact/org.mcsg.groovy/GroovyRuntime/1.0/GroovyRuntime-1.0.jar");
				File file = new File("plugins/", "GroovyRuntime.jar");
				FileUtils.copyURLToFile(url, file);

				Plugin groovy = Bukkit.getPluginManager().loadPlugin(file);
				Bukkit.getPluginManager().enablePlugin(groovy);

				hasGroovy = true;
			} catch (Exception e){
				sendConsoleMessage(ChatColor.RED+"Failed to download GroovyRuntime. Please manually download it and try again.");
			}
		} else {
			hasGroovy = true;
		}

	}

	public void onLoad(){
		plugin = this;



		if(hasGroovy){
			StartupOffloader.onLoad(this);
		}
	}

	public void onEnable(){
		if(hasGroovy){
			StartupOffloader.onEnable();
		}
	}


	public void onDisable(){
		StartupOffloader.onDisable();
	}



	private void sendConsoleMessage(String message){
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"[PlotMaster] "+message);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return StartupOffloader.onCommand(sender, cmd, label, args);
	}



	public static PlotMasterPlugin getPlugin(){
		return plugin;
	}


}
