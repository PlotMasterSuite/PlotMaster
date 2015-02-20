package org.mcsg.plotmaster.schematic

import groovy.transform.CompileStatic;

import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

import org.mcsg.plotmaster.Settings;
import org.mcsg.plotmaster.utils.PlatformAdapter;

@CompileStatic
class Border {

	String name;
	int width
	Map<SFace, Schematic> borders = [:]
	
	/*
	 * Z = H
	 * +Z = NORTH
	 * -Z = SOUTH
	 * 
	 * X = W
	 * +X = EAST
	 * -X = WEST
	 * 
	 * 
	 */
	
	
	SchematicBlock getBlockAt(int x, int y, int z , int w, int h, int bottom){
		def posx = x % w
		def posz = z % h
		
		def face = getFace(posx, posz, h, w)
		
		if(face) {
			
			Schematic schematic = borders.get(face)
			
			int xloc = posx + (face.getXmod() * w)
			int yloc = y - bottom
			int zloc = posz + (face.getZmod() * h)
			
			return schematic.getBlockAt(xloc, yloc, zloc)
		}
		return null
	}
	
	
	SchematicBlock[] getColumnAt(int x, int z , int w, int h){
		def posx = Math.abs(x % w)
		def posz = Math.abs(z % h)
		
		//println "${x}_$posx: ${z}_$posz"
		
		def face = getFace(posx, posz, h, w)
		if(face) {
			Schematic schematic = borders.get(face)
		
		/*	int xloc = posx + (face.getXmod() * width)
			int zloc = posz + (face.getZmod() * width)
			
			if(Math.abs(xloc) < width || Math.abs(zloc) < width)*/
			
			return schematic.getColumn(posx , posz)
		}
		return null
	}
	
	void setBorder(SFace face, Schematic schematic){
		borders.put(face,  schematic)
		save()
	}
	
	private SFace getFace(int posx, int posz, int h, int w){

		//print "${posx}.${posz}.${h}.${w}.${width}"
		
		boolean south = posz > h - width - 1
		boolean north = posz <  width 
		
		boolean east = posx > w - width - 1
		boolean west = posx <  width
		
		
		if(north && east)
			return SFace.NORTH_EAST
		if(north && west)
			return SFace.NORTH_WEST
		if(south && east)
			return SFace.SOUTH_EAST
		if(south && west)
			return SFace.SOUTH_WEST
		if(north)
			return SFace.NORTH
		if(south)
			return SFace.SOUTH
		if(west)
			return SFace.WEST
		if(east)
			return SFace.EAST
			
		return null
	}
	
	
	void save(){
		checkFolder()
		
		def json = Settings.getGson().toJson(this)
		def file = new File(folder, "${name}.border.gz")
		
		def writer = new OutputStreamWriter(new GZIPOutputStream(
			new FileOutputStream(file)))
		
		writer.write(json)
		writer.close()	
	}
	
	static File folder
	
	private static checkFolder(){
		if(!folder){
			folder = new File(PlatformAdapter.getDataFolder(), "borders/")
			folder.mkdirs()
		}
	}
	
	
	static Border load(String name){
		checkFolder()
		File file = new File(folder, "${name}.border.gz")
		
		if(!file.exists())
			return null
	
		def reader = new BufferedReader(new InputStreamReader(
			new GZIPInputStream(new FileInputStream(file))))
			
		def json = reader.getText()
		
		Border border = Settings.getGson().fromJson(json, Border.class)
		reader.close()
		
		return border
	}
	
	
	
	
}
