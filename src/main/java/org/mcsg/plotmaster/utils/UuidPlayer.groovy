package org.mcsg.plotmaster.utils

import org.mcsg.plotmaster.bridge.PMLocation;
import org.mcsg.plotmaster.bridge.PMPlayer

class UuidPlayer implements PMPlayer{
	
	String uuid
	PMPlayer delegate
	
	public UuidPlayer(String uuid) {
		delegate = PlatformAdapter.getPlayerByUUID(uuid)
		this.UUID = uuid;
	}
	
	
	public boolean isConsole() {
		return false
	}
	
	public String getName() {
		return (delegate) ? "" : delegate.getName()
		
	}
	
	public boolean hasPermission(String permission) {
		return (delegate) ? false : delegate.hasPermission(permission)
	}
	
	public void sendMessage(String message) {
		// TODO Auto-generated method stub
		
	}
	
	public String getUUID() {
		return uuid;
	}
	
	public PMLocation getLocation() {
		return null;
	}
	
	public boolean isOnline() {
		return false;
	}
	
}
