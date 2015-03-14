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
	}




}
