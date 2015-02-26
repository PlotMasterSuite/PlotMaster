package org.mcsg.plotmaster.schematic

import java.io.File;
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

import org.mcsg.plotmaster.Settings;
import org.mcsg.plotmaster.utils.PlatformAdapter;

class Schematic {

	SchematicBlock[][][] blocks;
	String name

	
	def Schematic(int sizex, int sizey, int sizez){
		blocks = new SchematicBlock[sizex][ sizez][ sizey]
	}
	
	void setBlockAt(int x, int y, int z, SchematicBlock block){
		blocks[x][z][y] = block
	}

	SchematicBlock getBlockAt(int x,int y,int z){
		try{
			return blocks[x][z][y]
		} catch (Exception e){
			return null
		}
	}
	
	
	SchematicBlock[] getColumn(int x, int z){
		return blocks[x % blocks.size()][z % blocks[0].size()]
	}
	
	
	
	
	void save(){
		checkFolder()
		
		def json = Settings.getGson().toJson(this)
		def file = new File(folder, "${name}.schematic")
		
		def writer = new OutputStreamWriter(new GZIPOutputStream(
			new FileOutputStream(file)))
		
		writer.write(json)
		writer.close()
	}
	
	
	private static checkFolder(){
		if(!folder){
			folder = new File(PlatformAdapter.getDataFolder(), "schematics/")
			folder.mkdirs()
		}
	}
	
	static File folder
	
	static Schematic load(String name){

		File file = new File(folder, "${name}.sch")
		
		if(!file.exists())
			return null
	
		def reader = new BufferedReader(new InputStreamReader(
			new GZIPInputStream(new FileInputStream(file))))
			
		def json = reader.getText()
		
		Settings.getGson().fromJson(json, Schematic.class)
	}


}
