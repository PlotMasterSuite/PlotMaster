package org.mcsg.plotmaster.utils

import org.mcsg.plotmaster.utils.PlatformAdapter.PlatformType;

import bukkit.org.mcsg.plotmaster.util.BukkitSchedulerUtils;
import groovy.lang.Closure;
import groovy.transform.CompileStatic;
@CompileStatic
class SchedulerAdapter {

	static delayed(int delay, Closure run){
		if(PlatformAdapter.getPlatform() == PlatformType.BUKKIT){
			BukkitSchedulerUtils.delayed(delay, run) 
		}
	}

	static repeating(int delay, int repeat, Closure run){
		if(PlatformAdapter.getPlatform() == PlatformType.BUKKIT){
			BukkitSchedulerUtils.repeating(delay, repeat, run)
		}
	}

	static asyncDelay(int delay, Runnable run, Closure callback){
		if(PlatformAdapter.getPlatform() == PlatformType.BUKKIT){
			BukkitSchedulerUtils.asyncDelay(delay, run, callback)
		}
	}

	static asyncRepeating(int delay, int repeat,  Closure run) {
		if(PlatformAdapter.getPlatform() == PlatformType.BUKKIT){
			BukkitSchedulerUtils.asyncRepeating(delay, repeat, run)
		}
	}

	static asyncRepeating(int delay, int repeat, int times, Runnable run, Closure callback) {
		if(PlatformAdapter.getPlatform() == PlatformType.BUKKIT){
			BukkitSchedulerUtils.asyncRepeating(delay, repeat, times, run, callback)
		}
	}

}
