package org.mcsg.plotmaster.backend.sql

import groovy.lang.Closure;
import groovy.sql.Sql;
import groovy.transform.CompileStatic;

import javax.sql.DataSource

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
				`world` int(11) NOT NULL,
				`x` int(11) NOT NULL,
				`z` int(11) NOT NULL,
				`h` int(11) NOT NULL,
				`w` int(11) NOT NULL,
				`createdAt` int(11) NOT NULL,
				PRIMARY KEY (`id`)
			) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;
		""")
		
		sql.execute("""
			CREATE TABLE IF NOT EXISTS `${world}_plots` (
			  `id` int(11) NOT NULL AUTO_INCREMENT,
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
			) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;
		""")
		

	}



	public void getRegion(int id, Closure c) {
		// TODO Auto-generated method stub

	}

	public void getRegionByLocation(int x, int z, Closure c) {
		// TODO Auto-generated method stub

	}

	public void saveRegion(Region region, Closure c) {
		// TODO Auto-generated method stub

	}

	public void getPlot(int id, Closure c) {
		// TODO Auto-generated method stub

	}

	public void createRegion(int x, int y, Closure c) {
		// TODO Auto-generated method stub

	}

	public void createPlot(Region region, int x, int y, PlotType type, Closure c) {
		// TODO Auto-generated method stub

	}

}
