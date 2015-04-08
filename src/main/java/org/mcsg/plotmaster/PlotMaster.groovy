
package org.mcsg.plotmaster

import groovy.transform.CompileStatic;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.commons.io.FileUtils;
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
import org.mcsg.plotmaster.rest.RestServer
import org.mcsg.plotmaster.utils.PlatformAdapter;
import org.mcsg.plotmaster.utils.TaskQueue;

import bukkit.org.mcsg.plotmaster.listeners.WorldEditListener;

import com.google.common.eventbus.EventBus;
import com.sk89q.worldedit.WorldEdit;



class PlotMaster{

	private static Map<String, Class<? extends PlotManager>> managers_registry = [:]
	private static Map<String, Class<? extends Backend>> backends_registry = [:]
	private static Map<String, Map<String, PlotType>> plottypes = [:]
	private Map<String, PlotManager> managers = [:]
	private boolean setup = true
	private PMConsole console
	private EventBus eventBus
	
	static PlotMaster instance;



	void onLoad(PMConsole console){
		instance = this;
		this.console = console

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


		copyFile("borders/", "beacon.border")
		copyFile("borders/", "small.border")
		copyFile("borders/", "road.border")
		copyFile("borders/", "dir.border")

		eventBus = new EventBus()
		
	}

	@CompileStatic
	void onEnable(){

		CommandHandler.load()

		if(setup){
			console.sendMessage("&bPlot Master has loaded in SETUP mode!")
			console.sendMessage("&bSETUP mode allows you to setup the plugin (ie create borders/schematics) without loading the plot managers")
			console.sendMessage("&bOnce you have setup the plugin, change 'setup' to false in the config!")

			for(a in 2..0){
				Thread.sleep(1000)
				console.sendMessage("&aContinuing startup in $a seconds")
			}
			return;
		} else {

			loadConfigurations()
			TaskQueue.start()

			
		}


	}

	void onDisable(){

	}

	private copyFile(String dir, String file){
		File folder = new File(PlatformAdapter.getDataFolder(), dir)
		File f = new File(folder, file)
		if(!f.exists()){
			folder.mkdirs()
			f.createNewFile()

			def input = this.getClass().getResource("/files/"+dir+file)

			FileUtils.copyURLToFile(input, f)
		}
	}


	private loadConfigurations(){
		for(Map conf in Settings.config.worlds) {

			conf.plotTypes.each { key, value ->
				def plot = new PlotType(name: value.name, w: value.width, h: value.height,
				schematic: value.schematic, border: value.border)

				registerPlotType(conf.world, plot)
			}
			loadManager(conf, conf.backend, conf.type, conf.world )
		}
	}


	Backend loadBackend(String name, String world){
		def backend = backends_registry.get(name).newInstance()
		backend.load(world, getBackendConfiguration(name))
		return backend;
	}

	PlotManager loadManager(Map conf, String backend, String type, String world){
		def backendInstance = loadBackend(backend, world);
		def manager = managers_registry.get(type).newInstance(backendInstance, world)

		manager.load(conf)
		managers.put(world,  manager)

		return manager
	}

	void sendConsoleMessage(String msg){
		console.sendMessage(msg)
	}

	PMConsole getConsole(){
		return this.console;
	}

	//////////////////////////////// API //////////////////////////////////
	void registerBackend(String name, Class<? extends Backend> backend){
		backends_registry.put(name, backend)
	}

	void registerManager(String name, Class<? extends PlotManager> manager){
		managers_registry.put(name, manager)
	}

	void registerPlotType(String world, PlotType type){
		console.sendMessage("&aLoaded PlotType \"${type.name}\" for ${world}")

		def worldmap = plottypes.get(world) ?: [:]

		worldmap.put(type.name, type)
		plottypes.put(world, worldmap)
	}

	PlotType getPlotType(String world, String name){
		return plottypes.get(world).get(name)
	}

	PlotManager getManager(String world){
		return managers.get(world)
	}




	List<Map> getConfigurationSelectionPerManagerType(String type){
		def list = []
		def sel = Settings.getConfig().worlds

		list = sel.findAll() {
			(it as Map).type == type
		}

		return list
	}

	Map getBackendConfiguration(String backend){
		return Settings.config.backends.get(backend)
	}

	
	void registerEvent(Object o){
		eventBus.register(o)
	}

	
	void fireEvent(Object o) {
		eventBus.post(o)
	}


	//////////////////////////////////////////////////////////////////////////

	static String getVersion(){
		"0.0.1"
	}





}
