package org.mcsg.plotmaster.backend.sql.sqlite

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

import groovy.lang.Closure;

import org.mcsg.plotmaster.Plot;
import org.mcsg.plotmaster.Region;
import org.mcsg.plotmaster.Settings;
import org.mcsg.plotmaster.backend.Backend
import org.mcsg.plotmaster.backend.sql.AbstractSQLBackend;

class SQLiteBackend extends AbstractSQLBackend {

	public void load(String world) {
		def conf = Settings.config.backends.sqlite;
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:sqlite:$conf.location");
		config.setUsername(conf.username);
		config.setPassword(conf.password);
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.addDataSourceProperty("useServerPrepStmts", "true");
		
		this.ds = new HikariDataSource(config);
	}






}
