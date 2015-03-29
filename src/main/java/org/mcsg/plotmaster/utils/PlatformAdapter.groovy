package org.mcsg.plotmaster.utils

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location
import org.bukkit.block.Block;
import org.bukkit.event.Event
import org.mcsg.plotmaster.bridge.PMLocation
import org.mcsg.plotmaster.bridge.PMPlayer
import org.mcsg.plotmaster.events.Cancellable
import org.mcsg.plotmaster.events.IEvent
import org.mcsg.plotmaster.events.IPlotCreationEvent;
import org.mcsg.plotmaster.events.IPlotLoadEvent;
import org.mcsg.plotmaster.events.IPlotUnloadEvent
import org.mcsg.plotmaster.events.IRegionCreationEvent
import org.mcsg.plotmaster.events.IRegionLoadEvent;
import org.mcsg.plotmaster.events.IRegionUnloadEvent;
import org.mcsg.plotmaster.schematic.SchematicBlock;

import groovy.lang.Closure;
import groovy.transform.CompileStatic;
import bukkit.org.mcsg.plotmaster.bridge.BukkitLocation
import bukkit.org.mcsg.plotmaster.bridge.BukkitPlayer;
import bukkit.org.mcsg.plotmaster.event.PlotCreationEvent
import bukkit.org.mcsg.plotmaster.event.PlotLoadEvent;
import bukkit.org.mcsg.plotmaster.event.PlotUnloadEvent
import bukkit.org.mcsg.plotmaster.event.RegionCreationEvent;
import bukkit.org.mcsg.plotmaster.event.RegionLoadEvent;
import bukkit.org.mcsg.plotmaster.event.RegionUnloadEvent
import bukkit.org.mcsg.plotmaster.util.BukkitBlockUpdate;

@CompileStatic
class PlatformAdapter {

	enum PlatformType {
		NONE, BUKKIT
	}

	static PlatformType platform = PlatformType.NONE

	static BlockUpdate createBlockUpdate(String world, int x, int y, int z, String material, byte data){
		if(platform == PlatformType.BUKKIT){
			return new BukkitBlockUpdate(world, x, y, z, material, data)
		}
	}

	static SchematicBlock toSchematicBlock(String world, int x, int y, int z){
		if(platform == PlatformType.BUKKIT){
			Block b = new Location(Bukkit.getWorld(world), x, y, z).getBlock()

			return new SchematicBlock(material: b.getType().toString(), data: b.getData())
		}

	}

	static PMLocation toLocation(String world, int x, int y, int z){
		if(platform == PlatformType.BUKKIT){
			return new BukkitLocation(new Location(Bukkit.getWorld(world), x, y, z))
		}
	}

	static PMPlayer getPlayer(String name){
		if(platform == PlatformType.BUKKIT){
			return new BukkitPlayer(Bukkit.getPlayer(name))
		}
	}

	static PMPlayer getPlayerByUUID(String uuid){
		if(platform == PlatformType.BUKKIT){
			return new BukkitPlayer(Bukkit.getPlayer(UUID.fromString(uuid)))
		}
	}


	static File getDataFolder(){
		if(platform == PlatformType.BUKKIT) {
			def file  = new File("plugins/PlotMaster")
			file.mkdirs()
			return file
		} else {
			def file = new File("PlotMaster/")
			file.mkdirs()
			return file
		}
	}

	static Material toMaterial(org.bukkit.Material material){
		return Material.valueOf(material.toString())
	}

	static class EventMapper {
		def EventMapper(Class bukkit){
			this.bukkit = bukkit
		}

		Class bukkit

		Class toExternalClass() {
			if(platform == PlatformType.BUKKIT) {
				return bukkit
			}
		}
	}

	static {
		eventMap.put(IPlotCreationEvent.class, new EventMapper(PlotCreationEvent.class))
		eventMap.put(IPlotUnloadEvent.class, new EventMapper(PlotUnloadEvent.class))
		eventMap.put(IPlotLoadEvent.class, new EventMapper(PlotLoadEvent.class))
		
		eventMap.put(IRegionCreationEvent.class, new EventMapper(RegionCreationEvent.class))
		eventMap.put(IRegionLoadEvent.class, new EventMapper(RegionLoadEvent.class))
		eventMap.put(IRegionUnloadEvent.class, new EventMapper(RegionUnloadEvent.class))

	}

	static Map<Class, EventMapper> eventMap  = [:]




	//Oh groovy, you are amazing

	//Take an internal event and covert it to the external form
	static boolean fireEvent(IEvent internal){

		def external = eventMap.get(internal.getClass()).toExternalClass().newInstance()
		transformClass(internal, external)

		if(platform == PlatformType.BUKKIT) {
			Event e = external as Event
			Bukkit.getPluginManager().callEvent(e)
			if (e instanceof Cancellable) {
				return (e as Cancellable).isCancelled()
			} else return false
		}


	}


	private static transformClass(class1, class2){
		class1.getProperties().each { key, value ->
			if(!(value instanceof Class))
				class2.getMetaClass().setProperty(class2, key as String, value)
		}
	}
}
