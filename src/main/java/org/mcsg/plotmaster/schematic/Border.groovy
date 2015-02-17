package org.mcsg.plotmaster.schematic

import groovy.transform.CompileStatic;

import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

import org.mcsg.plotmaster.Settings;
import org.mcsg.plotmaster.schematic.Schematic.SchematicBlock
import org.mcsg.plotmaster.utils.PlatformAdapter;

@CompileStatic
class Border {

	String name;
	int width
	Map<SFace, Schematic> borders
	
	
	SchematicBlock getBlockAt(int x, int y, int z , int w, int h, int bottom){
		def posx = x % h
		def posz = z % w
		
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
	
	
	SchematicBlock[] getColumnAt(int x, int z , int w, int h, int bottom){
		def posx = x % h
		def posz = z % w
		
		def face = getFace(posx, posz, h, w)
		
		if(face) {
			Schematic schematic = borders.get(face)
			
			int xloc = posx + (face.getXmod() * w)
			int zloc = posz + (face.getZmod() * h)
			
			return schematic.getColumn(xloc, zloc)
		}
		return null
	}
	
	
	private SFace getFace(int posx, int posz, int h, int w){
		
		boolean north = posx > h - width
		boolean south = posx < h + width 
		
		boolean west = posz > w - width
		boolean east = posz < w + width
		
		
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
		def file = new File(folder, "${name}.border")
		
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
		File file = new File(folder, "${name}.border")
		
		if(!file.exists())
			return null
	
		def reader = new BufferedReader(new InputStreamReader(
			new GZIPInputStream(new FileInputStream(file))))
			
		def json = reader.getText()
		
		Settings.getGson().fromJson(json, Border.class)
		reader.close()
	}
	
	
	
	
}
