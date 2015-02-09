package org.mcsg.plotmaster.utils

import groovy.transform.CompileStatic;

import org.bukkit.Bukkit;
import org.mcsg.plotmaster.PlotMaster;

@CompileStatic
class TaskUtils {

	static delayed(int delay, Closure run){ 
		Bukkit.getScheduler().scheduleSyncDelayedTask(PlotMaster.getPlugin(), run as Runnable ,delay)
	} 

	static repeating(int delay, int repeat, Closure run){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(PlotMaster.getPlugin(), run as Runnable, delay, repeat)
	}

	static asyncDelay(int delay, Runnable run, Closure callback){
		Bukkit.getScheduler().runTaskLaterAsynchronously(PlotMaster.getPlugin(), {
			run.run()
			if(callback)
				callback.call()
		} as Runnable, delay)
	}

	static asyncRepeating(int delay, int repeat,  Closure run) {
		Bukkit.getScheduler().runTaskTimerAsynchronously(PlotMaster.getPlugin(), run as Runnable, delay, repeat)
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
