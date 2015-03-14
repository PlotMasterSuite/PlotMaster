package org.mcsg.plotmaster.backend.sql

import groovy.lang.Closure;
import groovy.sql.BatchingPreparedStatementWrapper
import groovy.sql.GroovyResultSet;
import groovy.sql.Sql;
import groovy.transform.CompileStatic;

import javax.sql.DataSource

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.mcsg.plotmaster.AccessLevel;
import org.mcsg.plotmaster.Plot
import org.mcsg.plotmaster.PlotMember;
import org.mcsg.plotmaster.PlotType;
import org.mcsg.plotmaster.Region;
import org.mcsg.plotmaster.backend.Backend;

abstract class AbstractSQLBackend implements Backend{

	DataSource ds;
	String world;

	def regions, plots, users, access_list

	public void load(String world, Map settings){
		this.world = world;

		regions = "${world}_regions"
		plots  = "${world}_plots"
		users  = "${world}_users"
		access_list = "${world}_access_list"

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
				 `plot` varchar(32) NOT NULL,
				 `reg` varchar(32) NOT NULL,
				 PRIMARY KEY (`id`)
			) ENGINE=InnoDB DEFAULT CHARSET=latin1
		""".toString())

		sql.close()
	}


	public Region getRegion(int id) {
		Sql sql = getSql()

		def res = sql.firstRow "SELECT * FROM ${regions} WHERE id=${id}".toString()
		def reg = regionFromQuery(res)

		loadPlotsForRegion(sql, reg)
		closeReturn(sql, res)
	}


	public Region getRegionByPlotId(int id){
		Sql sql = getSql()
		def res = sql.firstRow "SELECT region FROM ${plots} WHERE id=${id}".toString()

		getRegion(res.region);
		closeReturn(sql, res)
	}


	public Region getRegionByLocation(int x, int z) {
		Sql sql = getSql()

		def res = sql.firstRow "SELECT * FROM ${regions} WHERE x=${x} AND z=${z}".toString()
		def reg = regionFromQuery(res)

		loadPlotsForRegion(sql, reg)
		closeReturn(sql, reg)
	}

	public void saveRegion(Region region) {
		Sql sql = getSql()

		def reg = [region.name, region.x, region.z, region.h, region.w, region.id]
		sql.execute "UPDATE ${regions} SET name=?, x=?, z=?, h=?, w=? WHERE id=?".toString(), reg

		region.getPlots().values().each {
			def plot = [it.ownerName, it.OwnerUUID, it.plotName, it.x, it.z, it.h, it.w]
			sql.execute "UPDATE ${plots} SET owner=?, uuid=?, name=?, x=?, z=?, h=?, w=? WHERE id={$it.id}".toString(), plot
		}
		sql.close()
	}

	public Plot getPlot(int id) {
		Sql sql = getSql()

		def row = sql.firstRow "SELECT * FROM ${plots} WHERE id=${id}".toString()
		def reg = getRegion(row.region)
		def plot = plotFromQuery(row, reg)

		closeReturn(sql, plot)
	}

	public Region createRegion(Region region) {
		assert region, "Cannot create a null region!"
		assert region.world == world, "Attempting to create a region with an invalid world! ${region.world} != ${world}"
		Sql sql = getSql()

		sql.execute("INSERT INTO ${regions} (world, x, z, h, w, createdAt) VALUES(?,?,?,?,?, ?); ".toString()
				, [region.world, region.x, region.z, region.h, region.w, region.createdAt])
		def res = sql.firstRow("SELECT LAST_INSERT_ID() as id FROM ${regions}".toString())

		def time = System.currentTimeMillis()
		region.setId(res.id)
		region.setCreatedAt(time)

		closeReturn(sql, region)
	}

	public Plot createPlot(Region region, Plot plot) {
		Sql sql = getSql()

		sql.execute( "INSERT INTO ${plots} (world, region, x, z, h, w, type, createdAt) VALUES(?,?,?,?,?,?,?, ?);".toString()
				, [region.world, region.id, plot.x, plot.z, plot.h, plot.w, plot.type.name, plot.createdAt])
		def res = sql.firstRow("SELECT LAST_INSERT_ID() as id FROM ${plots}".toString())

		def time = System.currentTimeMillis()
		plot.setId(res.id)
		plot.setCreatedAt(time)

		closeReturn(sql, plot)
	}

	PlotMember getMember(String uuid){
		assert uuid, "UUID cannot be null!"
		Sql sql = getSql()

		PlotMember member = new PlotMember(uuid: uuid, plots: new HashMap<>())

		sql.eachRow("SELECT * FROM ${access_list} WHERE uuid=?".toString(), [uuid]) { row ->
			def type = row.type as AccessLevel;
			def access = (member.plots.get(type)) ?: new ArrayList<Map<String, Integer>>()

			access.add([plot: row.plot, region: row.region])
			member.plots.put(type, access)
		}

		closeReturn(sql, member)
	}

	void saveMember(PlotMember member){
		assert member, "Cannot save null member!"
		assert member.uuid, "Cannot save a member with no UUID!"
		Sql sql = getSql()

		sql.execute("DELETE FROM ${access_list} WHERE uuid=?".toString(), [member.uuid])

		//id, uuid, name, type, plot, reg
		sql.withBatch("INSERT INTO ${access_list} VALUES(NULL, ?, ?, ?, ?, ?)".toString()) { BatchingPreparedStatementWrapper ps ->
			member.getPlotAccessMap().each { level, list ->
				list?.each {
					ps.addBatch([member.uuid, member.name, level.toString(), it.id, it.reg])
				}
			}

		}
		sql.close()
	}


	private Region regionFromQuery(row){
		if(row) {
			new Region(id: row.id, name: row.name, world: row.world, x: row.x, z: row.z,
			h: row.h, w: row.w, createdAt: row.createdAt)
		} else {
			return null
		}
	}

	protected Plot plotFromQuery(row, reg){
		if(row){
			new Plot(id: row.id, region: reg, plotName: row.name, ownerName: row.owner,
			ownerUUID: row.uuid, x: row.x, z: row.z, h: row.h, w: row.w,
			createdAt: row.createdAt)
		}
		return null
	}


	private Region loadPlotsForRegion(Sql sql, Region reg){
		if(reg)
			sql.eachRow("SELECT * FROM ${world}_plots WHERE region=${reg.id}".toString()) { row ->
				def plot = plotFromQuery(row, reg)
				if(plot)
					reg.plots.put(plot.id, plot)
			}
		return reg
	}



	private Sql getSql(){
		new Sql(ds)
	}

	private closeReturn(sql, ret){
		sql.close()
		ret
	}

}
