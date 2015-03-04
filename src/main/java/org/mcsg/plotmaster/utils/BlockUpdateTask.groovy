package org.mcsg.plotmaster.utils

import groovy.transform.CompileStatic;

@CompileStatic
class BlockUpdateTask implements Task{

	Callback callback
	
	List<BlockUpdate> updates
	
	def BlockUpdateTask(List<BlockUpdate> updates, Callback callback){
		this.updates = updates
		this.callback = callback
	}
	
	@Override
	public boolean step() {
		def time = System.currentTimeMillis()
		
		while (time + 100 > System.currentTimeMillis()) {
			int size = updates.size() - 1
			if(size < 0)
				return true
			
			updates.remove(size).update()
		}
		return false;
	}

	@Override
	public void onComplete() {
		if(callback)
			callback.call(null)
	}

	
	
	
	
}
