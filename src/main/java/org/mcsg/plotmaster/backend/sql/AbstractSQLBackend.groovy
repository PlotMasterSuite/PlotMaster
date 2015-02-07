package org.mcsg.plotmaster.backend.sql

import groovy.lang.Closure;
import groovy.sql.Sql;
import groovy.transform.CompileStatic;

import javax.sql.DataSource

import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.PlotType;
import org.mcsg.plotmaster.Region;
import org.mcsg.plotmaster.backend.Backend;

@CompileStatic
abstract class AbstractSQLBackend implements Backend{

	DataSource ds;
	String world;

	public void load(String world){
		this.world = world;
		def sql = new Sql(ds);

		sql.execute("""
			CREATE TABLE IF NOT EXISTS `${world}_regions` (
				 `id` int(11) NOT NULL AUTO_INCREMENT,
				 `name` varchar(128) NOT NULL,
				 `world` int(11) NOT NULL,
				 `x` int(11) NOT NULL,
				 `z` int(11) NOT NULL,
				 `h` int(11) NOT NULL,
				 `w` int(11) NOT NULL,
				 `createdAt` int(11) NOT NULL,
				 PRIMARY KEY (`id`)
			) ENGINE=InnoDB DEFAULT CHARSET=latin1 ;
		""")
		
		sql.execute("""
			CREATE TABLE IF NOT EXISTS `${world}_plots` (
				 `id` int(11) NOT NULL AUTO_INCREMENT,
				 `region` int(11) NOT NULL,
				 `name` varchar(64) NOT NULL,
				 `owner` varchar(16) NOT NULL,
				 `uuid` varchar(36) NOT NULL,
				 `x` int(11) NOT NULL,
				 `z` int(11) NOT NULL,
				 `h` int(11) NOT NULL,
				 `w` int(11) NOT NULL,
				 `createdAt` int(11) NOT NULL,
				 `type` int(11) NOT NULL,
				  PRIMARY KEY (`id`)
			) ENGINE=InnoDB DEFAULT CHARSET=latin1 ;
		""")
		
		sql.execute("""
			CREATE TABLE `${world}_access_list` (
				 `id` int(11) NOT NULL AUTO_INCREMENT,
				 `name` varchar(16) NOT NULL,
				 `uuid` varchar(36) NOT NULL,
				 `mode` int(2) NOT NULL,
				 PRIMARY KEY (`id`)
			) ENGINE=InnoDB DEFAULT CHARSET=latin1
		""")
		
		
		
		sql.close()
	}



	public Region getRegion(int id) {
		// TODO Auto-generated method stub

	}

	public Region getRegionByLocation(int x, int z) {
		// TODO Auto-generated method stub

	}

	public void saveRegion(Region region) {
		// TODO Auto-generated method stub

	}

	public Plot getPlot(int id) {
		// TODO Auto-generated method stub

	}

	public Region createRegion(int x, int y) {
		// TODO Auto-generated method stub

	}

	public Plot createPlot(Region region, int x, int y, PlotType type) {
		// TODO Auto-generated method stub

	}

}
