package org.mcsg.plotmaster.utils

import groovy.lang.Closure;

class AsyncUtils {

	static asyncWrap(Closure callback, Closure code){
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
