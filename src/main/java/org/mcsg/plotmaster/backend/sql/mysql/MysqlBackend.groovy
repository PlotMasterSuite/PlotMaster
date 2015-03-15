package org.mcsg.plotmaster.backend.sql.mysql

import org.mcsg.plotmaster.Settings;

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

import groovy.lang.Closure;

import org.mcsg.plotmaster.Plot;
import org.mcsg.plotmaster.PlotType;
import org.mcsg.plotmaster.Region;
import org.mcsg.plotmaster.backend.Backend
import org.mcsg.plotmaster.backend.sql.AbstractSQLBackend;

class MysqlBackend extends AbstractSQLBackend {

	public void load(String world, Map conf) {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://$conf.host:${conf.port as int}/$conf.database");
		config.setUsername(conf.user);
		config.setPassword(conf.pass);
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.addDataSourceProperty("useServerPrepStmts", "true");

		this.ds = new HikariDataSource(config);
		super.load(world, conf)

		def sql = getSql();

		sql.execute("""
			CREATE TABLE IF NOT EXISTS `${regions}` (
				 `id` int(11) NOT NULL AUTO_INCREMENT,
				 `name` varchar(128) NOT NULL DEFAULT '',
				 `world` varchar(64) NOT NULL,
				 `x` int(11) NOT NULL,
				 `z` int(11) NOT NULL,
				 `h` int(11) NOT NULL,
				 `w` int(11) NOT NULL,
				 `createdAt` bigint(32) NOT NULL,
				 PRIMARY KEY (`id`)
			) ENGINE=InnoDB DEFAULT CHARSET=latin1 ;
		""".toString())

		sql.execute("""
			CREATE TABLE IF NOT EXISTS `${plots}` (
				 `id` int(11) NOT NULL AUTO_INCREMENT,
				 `region` int(11) NOT NULL,
				 `world` varchar(64) NOT NULL DEFAULT '',
				 `name` varchar(64) NOT NULL DEFAULT '',
				 `owner` varchar(16) NOT NULL DEFAULT '',
				 `uuid` varchar(36) NOT NULL DEFAULT '',
				 `x` int(11) NOT NULL,
				 `z` int(11) NOT NULL,
				 `h` int(11) NOT NULL,
				 `w` int(11) NOT NULL,
				 `createdAt` bigint(32) NOT NULL,
				 `type` varchar(32) NOT NULL,
				  PRIMARY KEY (`id`)
			) ENGINE=InnoDB DEFAULT CHARSET=latin1 ;
		""".toString())

		sql.execute("""
			CREATE TABLE IF NOT EXISTS`${access_list}` (
				 `id` int(11) NOT NULL AUTO_INCREMENT,
				 `uuid` varchar(36) NOT NULL,
				 `name` varchar(16) ,
				 `type` enum('OWNER', 'ADMIN', 'MEMBER', 'ALLOW', 'DENY') NOT NULL,
				 `plot` int(11) NOT NULL,
				 PRIMARY KEY (`id`)
			) ENGINE=InnoDB DEFAULT CHARSET=latin1
		""".toString())

		sql.close()
	}




}
