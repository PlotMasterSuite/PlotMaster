package org.mcsg.plotmaster.utils

import org.bukkit.Bukkit;

import groovy.lang.Closure;
import groovy.transform.CompileStatic;

@CompileStatic
class AsyncUtils {

	static asyncWrap(Callback callback, Closure code){
		if (callback){
			Thread.start {
				callback( code() )
			}
			return null;
		}else {
			return code()
		}
	}


}
