package bukkit.org.mcsg.plotmaster.util

import groovy.transform.CompileStatic;

import org.bukkit.Bukkit;
import bukkit.org.mcsg.plotmaster.PlotMasterPlugin;

@CompileStatic
class BukkitSchedulerUtils {

	static delayed(int delay, Closure run){ 		
		Bukkit.getScheduler().scheduleSyncDelayedTask(PlotMasterPlugin.getPlugin(), run as Runnable ,delay)
	} 
 
	static repeating(int delay, int repeat, Closure run){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(PlotMasterPlugin.getPlugin(), run as Runnable, delay, repeat)
	}

	static asyncDelay(int delay, Runnable run, Closure callback){
		Bukkit.getScheduler().runTaskLaterAsynchronously(PlotMasterPlugin.getPlugin(), {
			run.run()
			if(callback)
				callback.call()
		} as Runnable, delay)
	}
 
	static asyncRepeating(int delay, int repeat,  Closure run) {
		Bukkit.getScheduler().runTaskTimerAsynchronously(PlotMasterPlugin.getPlugin(), run as Runnable, delay, repeat)
	}

	static asyncRepeating(int delay, int repeat, int times, Runnable run, Closure callback) {
		asyncDelay(delay, run) {
			if(times > 0)
				asyncRepeating(delay, repeat, times--, run, callback)
			else if(callback)
				callback.call()
		}
	}
}
