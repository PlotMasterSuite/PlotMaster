package org.mcsg.plotmaster

class Settings {

	/**
	 * Default Configuration
	 */
	
	static Map config = [




		configurations: [
			[
				type: "grid",
				world: "world",
				backend: "flatfile",
				
				grid : [
					regions : [
						width: 99,
						height: 99,
						border: "road",
						allow_expand: false
					],
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



}
