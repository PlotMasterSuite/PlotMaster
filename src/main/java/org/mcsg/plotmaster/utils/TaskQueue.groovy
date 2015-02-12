package org.mcsg.plotmaster.utils

import bukkit.org.mcsg.plotmaster.util.BukkitSchedulerUtils;
import groovy.transform.CompileStatic;

@CompileStatic
class TaskQueue {

	static PriorityQueue<Task> queue
	
	static Task current
	
	static start(){
		SchedulerAdapter.repeating(1, 1) {
			
			if(current) {
				def ret = current.step()
				if(ret) {
					current.onComplete()
					current = null
				}
			} else {
				current = queue.poll()
			}
			
		}
	}
	
	
	static addTask(Task task) {
		queue.offer(task)
	}
	
	
	
}
