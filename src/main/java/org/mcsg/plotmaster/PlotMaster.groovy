
package org.mcsg.plotmaster

import java.util.Map;

import org.mcsg.plotmaster.bridge.PMConsole;
import org.mcsg.plotmaster.backend.Backend;
import org.mcsg.plotmaster.backend.flatfile.FlatFileBackend;
import org.mcsg.plotmaster.backend.sql.mysql.MysqlBackend;
import org.mcsg.plotmaster.backend.sql.sqlite.SQLiteBackend;
import org.mcsg.plotmaster.bridge.PMCommandSender
import org.mcsg.plotmaster.command.CommandHandler;
import org.mcsg.plotmaster.managers.PlotManager
import org.mcsg.plotmaster.managers.grid.GridManager;
import org.mcsg.plotmaster.managers.unbound.UnboundManager;



class PlotMaster{

	private static Map<String, Class<? extends PlotManager>> managers_registry = [:]
	private static Map<String, Class<? extends Backend>> backends_registry = [:]
	private static Map<String, Map<String, PlotType>> plottypes = [:]
	private Map<String, PlotManager> managers = [:]
	private boolean setup = true
	private PMConsole console;
	static PlotMaster instance;



	void onLoad(PMConsole console){
		instance = this;
		console.sendMessage ("&9PlotMaster Suite is loading")

		Settings.load()

		setup = Settings.config.setup

		if(!setup) {
			registerBackend("flatfile", FlatFileBackend.class)
			registerBackend("mysql", MysqlBackend.class)
			registerBackend("sqlite", SQLiteBackend.class)

			registerManager("grid", GridManager.class)
			registerManager("unbound", UnboundManager.class)
		} else {
			console.warn("&6Plugin is in setup mode! Skipping load of backends & managers")
		}
	}

	void onEnable(){

		CommandHandler.load()
		
		if(setup){
			console.sendMessage("&bPlot Master has loaded in SETUP mode!")
			console.sendMessage("&bSETUP mode allows you to setup the plugin (ie create borders/schematics) without loading the plot managers")
			console.sendMessage("&bOnce you have setup the plugin, change 'setup' to false in the config!")

			for(a in 10..0){
				Thread.sleep(1000)
				console.sendMessage("&aContinuing startup in $a seconds")
			}
			return;
		} else {
		
			
		
		
		}


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

	void registerPlotType(String world, PlotType type){

	}

	PlotType getPlotType(String world, String name){

	}

	PlotManager getManager(String world){
		return managers.get(world)
	}




	List<Map> getConfigurationSelectionPerManagerType(String type){
		def list = []
		def sel = Settings.getConfig().configurations

		list = sel.findAll() {
			(it as Map).type == type
		}

		return list

	}




	//////////////////////////////////////////////////////////////////////////

	static String getVersion(){
		"0.0.1"
	}





}
