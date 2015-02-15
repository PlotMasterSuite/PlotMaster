package org.mcsg.plotmaster

import org.mcsg.plotmaster.utils.PlatformAdapter;

import com.google.gson.Gson

class Settings {

	/**
	 * Default Configuration
	 */

	static Gson gson = new Gson()
	static File file
	
	static load(){
		file = new File(PlatformAdapter.getDataFolder(), "config.json")
		file.createNewFile()
		
		def json = file.getText()
		
		if(json) {
			config = gson.fromJson(json, Map.class)
		} else {
			save()
		}
	}

	static save(){
		def json = gson.toJson(config)
		file.setText(json)
	}



	static Map config = [
		configurations: [
			[
				type: "grid",
				world: "world",
				backend: "flatfile",
				grid : [
					width: 99,
					height: 99,
					border: "road",
					allow_expand: false,
					plotTypes :[
						default: [
							name: "default",
							width: 50,
							height: 50,
							schematic: "",
							border: "small"
						],
						donator: [
							name: "donator",
							width: 99,
							height: 99,
							schematic: "",
							border: null
						]
					]
				]
			]
		],
		backend : [
			flatfile: [
				location : "./plots"
			],
			mysql: [
				host: "localhost",
				port: 3306,
				database: "plots",
				user: "root",
				pass: ""
			],
			sqlite: [
				location: "./plots.db"
			]
		]
	]
	
	/* Java
	 *
	 *
	 * class Configuration {
	 String type
	 String world
	 String backend
	 }
	 class ManagerConfiguration {
	 String border
	 Map<String, PlotType> types
	 }*/


}
