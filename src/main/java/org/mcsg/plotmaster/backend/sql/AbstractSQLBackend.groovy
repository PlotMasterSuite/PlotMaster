package org.mcsg.plotmaster.backend.sql

import groovy.lang.Closure;
import groovy.sql.BatchingPreparedStatementWrapper
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
			CREATE TABLE IF NOT EXISTS `${plots}` (
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
			CREATE TABLE IF NOT EXISTS`${access_list}` (
				 `id` int(11) NOT NULL AUTO_INCREMENT,
				 `uuid` varchar(36) NOT NULL,
				 `name` varchar(16) NOT NULL,
				 `type` enum('MEMBER','ALLOW','DENY') NOT NULL,
				 `plot` int(11) NOT NULL,
				 `region` int(11) NOT NULL,
				 PRIMARY KEY (`id`)
			) ENGINE=InnoDB DEFAULT CHARSET=latin1
		""")

		sql.close()
	}



	public Region getRegion(int id) {
		Sql sql = getSql()

		def res = sql.firstRow "SELECT * FROM ${regions} WHERE id=${id}"
		def reg = regionFromQuery(res)
		
		loadPlotsForRegion(sql, reg)
		closeReturn(sql, res)
	}


	public Region getRegionByPlotId(int id){
		Sql sql = getSql()
		def res = sql.firstRow "SELECT region FROM ${plots} WHERE id=${id}"

		getRegion(res.region);
		closeReturn(sql, res)
	}


	public Region getRegionByLocation(int x, int z) {
		Sql sql = getSql()

		def res = sql.firstRow "SELECT * FROM ${regions} WHERE x=${x} AND z=${z}"
		def reg = regionFromQuery(res)
		
		loadPlotsForRegion(sql, reg)
		closeReturn(sql, reg)
	}

	public void saveRegion(Region region) {
		Sql sql = getSql()

		def reg = [region.name, region.x, region.z, region.h, region.w, region.id]
		sql.execute "UPDATE {$regions} SET name=?, x=?, z=?, h=?, w=? WHERE id=${region.id}", reg

		region.getPlots().values().each {
			def plot = [it.ownerName, it.OwnerUUID, it.plotName, it.x, it.z, it.h, it.w]
			sql.execute "UPDATE ${plots} SET owner=?, uuid=?, name=?, x=?, z=?, h=?, w=? WHERE id={$it.id}", plot
		}
		sql.close()
	}

	public Plot getPlot(int id) {
		Sql sql = getSql()

		def row = sql.firstRow "SELECT * FROM ${plots} WHERE id=${id}"
		def plot = plotFromQuery(row)

		closeReturn(sql, plot)
	}

	public Region createRegion(Region region) {
		assert region, "Cannot create a null region!"
		assert region.world == world, "Attempting to create a region with an invalid world! ${region.world} != ${world}"
		Sql sql = getSql()

		def res = sql.firstRow ("""INSERT INTO ${regions} (world, x, z, h, w) VALUES(?,?,?,?,?); 
			SELECT LAST_INSERT_ID() as id FROM ${regions}""", [region.world, region.x, region.y, region.h, region.w])

		def time = System.currentTimeMillis()
		region.setId(res.id)
		region.setCreatedAt(time)
		
		closeReturn(sql, reg)
	}

	public Plot createPlot(Region region, Plot plot) {
		Sql sql = getSql()

		def res = sql.firstRow( """INSERT INTO ${plots} (world, region, x, z, h, w, type) VALUES(?,?,?,?,?,?,?);
			SELECT LAST_INSERT_ID() as id FROM ${plots}""", [region.world, region.id, plot.x, plot.z, plot.h, plot.w, plot.type.name])

		def time = System.currentTimeMillis()
		plot.setId(res.id)
		plot.setCreatedAt(time)
		
		closeReturn(sql, plot)
	}

	PlotMember getMember(String uuid){
		assert uuid, "UUID cannot be null!"
		Sql sql = getSql()

		PlotMember member = new PlotMember(uuid: uuid, plots: new HashMap<>())

		sql.eachRow("SELECT * FROM ${access_list} WHERE uuid=?", [uuid]) { row ->
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
		
		sql.execute("DELETE FROM ${access_list} WHERE uuid=?", [member.uuid])

		//uuid, name, access, plot, region
		sql.withBatch("INSERT INTO ${access_list} VALUES(NULL, ?, ?, ?, ?, ?)") { BatchingPreparedStatementWrapper ps ->
			member.plots.each { type, map ->
				ps.addBatch([member.uuid, member.name, type, map.plot, map.region])
			}
		}
		sql.close()
	}


	private Region regionFromQuery(row){
		new Region(id: row.id, name: row.name, world: row.world, x: row.x, z: row.z,
		h: row.h, w: row.w, createdAt: row.createdAt)
	}

	private Plot plotFromQuery(row){
		new Plot(id: row.id, region: reg, plotName: row.name, ownerName: row.owner,
		ownerUUID: row.uuid, x: row.x, z: row.z, h: row.h, w: row.w,
		createdAt: row.createdAt)
	}


	private Region loadPlotsForRegion(Sql sql, Region reg){
		sql.eachRow("SELECT * FROM ${world}_plots WHERE region=${id}") { row ->
			def plot = plotFromQuery(row)
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
