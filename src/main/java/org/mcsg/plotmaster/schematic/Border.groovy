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
	 * Z = W
	 * -Z = NORTH
	 * +Z = SOUTH
	 * 
	 * X = H
	 * +X = EAST
	 * -X = WEST
	 * 
	 * 
	 */


	SchematicBlock getBlockAt(int x, int y, int z , int w, int h, int bottom){
		//Fix x = 0 & z = 0 line
		if(x > 0)
			x -= 1
		if(z > 0)
			z -=1

		def posx = Math.abs(x % (w + 1))
		def posz = Math.abs(z % (h + 1))

		//Fix negative values
		if(x < 0)
			posx = (w - 1) - posx

		if(z < 0)
			posz = (h - 1) - posz

		def face = getFace(posx, posz, h, w)

		if(face) {

			Schematic schematic = borders.get(face)

			int yloc = y - bottom

			return schematic.getBlockAt(posx, yloc, posz)
		}
		return null
	}


	SchematicBlock[] getColumnAt(int x, int z , int w, int h){
		//Fix x = 0 & z = 0 line
		if(x > 0)
			x -= 1
		if(z > 0)
			z -=1

		def posx = Math.abs(x % (w))
		def posz = Math.abs(z % (h))

		//Fix negative values
		if(x < 0)
			posx = (w - 1) - posx

		if(z < 0)
			posz = (h - 1) - posz


		def face = getFace(posx, posz, h, w)
		if(face) {

			if(face.toString().contains("SOUTH")){
				posz = posz - (h % width)
			}
			if(face.toString().contains("EAST")) {
				posx = posx - (w % width)
			}

			Schematic schematic = borders.get(face)
			return schematic.getColumn(posx , posz)
		}
		return null
	}

	void setBorder(SFace face, Schematic schematic){
		borders.put(face,  schematic)
		save()
	}

	private SFace getFace(int posx, int posz, int h, int w){

		boolean south = posz >= h  - width
		boolean north = posz <  width

		boolean east = posx >= w - width
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

		Border border = Settings.getGson().fromJson(json, Border.class)
		reader.close()

		return border
	}




}
