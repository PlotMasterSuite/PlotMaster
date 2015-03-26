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

	public void load(String world, Map conf) {
		Class.forName("org.sqlite.JDBC")
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:sqlite:plugins/PlotMaster/${conf.location}");
		config.setUsername(conf.username);
		config.setPassword(conf.password);
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.addDataSourceProperty("useServerPrepStmts", "true");
		config.setConnectionTestQuery("SELECT 1")
		this.ds = new HikariDataSource(config);

		super.load(world, conf)

		def sql = super.getSql();

		sql.execute("""
			CREATE TABLE IF NOT EXISTS `${regions}` (
				 `id` INTEGER PRIMARY KEY,
				 `name` TEXT NOT NULL DEFAULT '',
				 `world` TEXT NOT NULL,
				 `x` INTEGER NOT NULL,
				 `z` INTEGER NOT NULL,
				 `h` INTEGER NOT NULL,
				 `w` INTEGER NOT NULL,
				 `createdAt` INTEGER NOT NULL
			);
		""".toString())

		sql.execute("""
			CREATE TABLE IF NOT EXISTS `${plots}` (
				 `id` INTEGER PRIMARY KEY,
				 `region` INTEGER NOT NULL,
				 `world` TEXT NOT NULL DEFAULT '',
				 `name` TEXT NOT NULL DEFAULT '',
				 `owner` TEXT NOT NULL DEFAULT '',
				 `uuid` TEXT NOT NULL DEFAULT '',
				 `x` INTEGER NOT NULL,
				 `z` INTEGER NOT NULL,
				 `h` INTEGER NOT NULL,
				 `w` INTEGER NOT NULL,
				 `createdAt` bigint(32) NOT NULL,
				 `type` text NOT NULL
				 `accessmode` TEXT NOT NULL DEFAULT 'ALLOW',
				 `settings` TEXT,
			);
		""".toString())

		sql.execute("""
			CREATE TABLE IF NOT EXISTS`${access_list}` (
				 `id` INTEGER PRIMARY KEY,
				 `uuid` TEXT,
				 `name` TEXT,
				 `type` TEXT,
				 `plot` INTEGER
			);
		""".toString())

		sql.close()
	}






}
