package org.mcsg.plotmaster

import java.util.Map;

import org.mcsg.plotmaster.backend.Backend;
import org.mcsg.plotmaster.backend.flatfile.FlatFileBackend;
import org.mcsg.plotmaster.backend.sql.mysql.MysqlBackend;
import org.mcsg.plotmaster.backend.sql.sqlite.SQLiteBackend;
import org.mcsg.plotmaster.bridge.PMCommandSender
import org.mcsg.plotmaster.managers.PlotManager
import org.mcsg.plotmaster.managers.grid.GridManager;
import org.mcsg.plotmaster.managers.unbound.UnboundManager;



class PlotMaster{

	private static Map<String, Class<? extends PlotManager>> managers_registry = [:]
	private static Map<String, Class<? extends Backend>> backends_registry = [:]
	
	private Map<String, PlotManager> managers = [:]
	
	
	static instance;
	
	void onLoad(PMCommandSender console){
		instance = this;
		console.sendMessage ("&9PlotMaster Suite is loading")
		
		registerBackend("flatfile", FlatFileBackend.class)
		registerBackend("mysql", MysqlBackend.class)
		registerBackend("sqlite", SQLiteBackend.class)
		
		registerManager("grid", GridManager.class)
		registerManager("unbound", UnboundManager.class)
	}
	
	void onEnable(){
		
	}

	void onDisable(){

	}
	
	
	
	Backend loadBackend(String name, String world){
		def backend = backends_registry.get(name).newInstance()
		backend.load(world)
		return backend;
	}
	
	PlotManager loadManager(String name, String backend, String world){
		def backendInstance = loadBackend(backend, world);
		def manager = managers_registry.get(world).newInstance(backendInstance, world)
		
		manager.load()
		return manager
	}
	
	
	
	//////////////////////////////// API //////////////////////////////////
	void registerBackend(String name, Class<? extends Backend> backend){
		backends_registry.put(name, backend)
	}
	
	void registerManager(String name, Class<? extends PlotManager> manager){
		managers_registry.put(name, manager)
	}
	
	PlotManager getManager(String world){
		return managers.get(world)
	}
	
	
	
	
	
	
	


}
