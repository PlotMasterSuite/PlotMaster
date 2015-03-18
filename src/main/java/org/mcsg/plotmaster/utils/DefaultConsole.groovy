package org.mcsg.plotmaster.utils

import org.mcsg.plotmaster.bridge.PMConsole

class DefaultConsole implements PMConsole{

	@Override
	public boolean isConsole() {
		return false;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasPermission(String permission) {
		return false;
	}

	@Override
	public void sendMessage(String message) {
		println message
		
	}

	@Override
	public void error(String msg) {
		println msg
		
	}

	@Override
	public void warn(String msg) {
		println msg
	}

}
