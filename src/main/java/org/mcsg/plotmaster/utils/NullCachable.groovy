package org.mcsg.plotmaster.utils

import groovy.transform.CompileStatic;

import org.mcsg.plotmaster.cache.Cacheable;
@CompileStatic
class NullCachable implements Cacheable {

	public boolean isStale() {
		return true;
	}
	
}
