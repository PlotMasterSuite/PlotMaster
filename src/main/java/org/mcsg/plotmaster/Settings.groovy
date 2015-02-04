package org.mcsg.plotmaster

class Settings {

	/**
	 * Default Configuration
	 */
	
	Map config = [




		configurations: [
			[
				type: "grid",
				world: "world",

				grid : [
					regions : [
						width: 99,
						height: 99,
						border: "road",
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


		borders : [
			road : [
				[
					height: 1,
					block: "Stone"
				],
				[
					height: 0,
					block: "wood"
				],
				[
					height: 1,
					block: "wood"
				],
				[
					height: 1,
					block: "wood"
				],
				[
					height: 1,
					block: "Stone"
				]
			],
			small : [
				[
					height: 1,
					block: "Stone"
				]
			]
		],



		backend : [
			type: "flatfile",

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
