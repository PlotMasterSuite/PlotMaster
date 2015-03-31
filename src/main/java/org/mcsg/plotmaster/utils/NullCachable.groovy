package org.mcsg.plotmaster.utils

import org.mcsg.plotmaster.cache.Cacheable;

class NullCachable implements Cacheable {

	public boolean isStale() {
		return true;
	}
	
}
