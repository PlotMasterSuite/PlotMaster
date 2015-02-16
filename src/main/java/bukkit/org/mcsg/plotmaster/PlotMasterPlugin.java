package bukkit.org.mcsg.plotmaster;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcsg.plotmaster.PlotMaster;

public class PlotMasterPlugin extends JavaPlugin{

	private static PlotMasterPlugin plugin;
	
	public void onEnable(){
		PlotMaster plotMaster = null;
		try{
			plotMaster = new PlotMaster();
		} catch (Exception e){
			e.printStackTrace();
			
			sendConsoleMessage(ChatColor.RED+"**********************************************************");
			sendConsoleMessage("");
			sendConsoleMessage(ChatColor.GOLD+"  GroovyRuntimeSupport is required to run this plugin!");
			sendConsoleMessage(ChatColor.GOLD+"  Download: https://ci.drtshock.net/view/Double0negative/");
			sendConsoleMessage("");
			sendConsoleMessage(ChatColor.RED+"**********************************************************");
			
			return;
	
		}
		plotMaster.onEnable();
		
	}
	
	
	public void onDisable(){
		
	}
	
	public static PlotMasterPlugin getPlugin(){
		return plugin;
	}
	
	private void sendConsoleMessage(String message){
		Bukkit.getConsoleSender().sendMessage(message);
	}
	
	

	
	
}
