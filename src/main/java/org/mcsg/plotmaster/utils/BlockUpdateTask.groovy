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

		def amount = 0

		while (time + 20 > System.currentTimeMillis()) {
			int size = updates.size() - 1
			if(size < 0)
				return true

			amount++
			updates.remove(size).update()
		}
		println "UPDATES ${amount}"
		return false;
	}

	@Override
	public void onComplete() {
		if(callback)
			callback.call(null)
	}





}
