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
import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.PlotMember;
import org.mcsg.plotmaster.PlotType;
import org.mcsg.plotmaster.Region;
import org.mcsg.plotmaster.backend.Backend;
import org.mcsg.plotmaster.backend.sql.sqlite.SQLiteBackend
import org.mcsg.plotmaster.managers.PlotManager;

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

		region.plots.values().each {
			def plot = [it.ownerName, it.ownerUUID, it.plotName, it.x, it.z, it.h, it.w, it.settingsToJson()]
			sql.execute "UPDATE ${plots} SET owner=?, uuid=?, name=?, x=?, z=?, h=?, w=?, settings=? WHERE id=${it.id}".toString(), plot
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

		def res = (this instanceof SQLiteBackend)
				?sql.firstRow("SELECT last_insert_rowid() as id FROM ${regions}".toString())
				: sql.firstRow("SELECT last_insert_id() as id FROM ${regions}".toString())

		def time = System.currentTimeMillis()
		region.setId(res.id)

		closeReturn(sql, region)
	}

	public Plot createPlot(Region region, Plot plot) {
		Sql sql = getSql()

		sql.execute( "INSERT INTO ${plots} (world, region, owner, uuid, x, z, h, w, type, accessmode, createdAt, settings, metadata) VALUES(?,?,?,?,?,?,?,?,?,?,?,?m ?);".toString()
				, [region.world, region.id, plot.ownerName, plot.ownerUUID, plot.x, plot.z, 
					plot.h, plot.w, plot.type.name, plot.accessMode,  plot.createdAt, plot.settingsToJson(), plot.metadataToJson()])
		
		def res = (this instanceof SQLiteBackend)
				? sql.firstRow("SELECT last_insert_rowid() as id FROM ${plots}".toString())
				: sql.firstRow("SELECT last_insert_id() as id FROM ${plots}".toString())

		def time = System.currentTimeMillis()
		plot.setId(res.id)
		plot.setCreatedAt(time)

		region.plots.put(plot.id, plot)

		closeReturn(sql, plot)
	}

	PlotMember getMember(String uuid){
		assert uuid, "UUID cannot be null!"
		Sql sql = getSql()

		PlotMember member = new PlotMember(uuid: uuid, plots: new HashMap<>())

		sql.eachRow("SELECT * FROM ${access_list} WHERE uuid=?".toString(), [uuid]) { row ->
			def type = AccessLevel.valueOf(row.type);
			member.getPlotAccessMap().put(row.plot, type)
		}

		closeReturn(sql, member)
	}

	void saveMember(PlotMember member){
		assert member, "Cannot save null member!"
		assert member.uuid, "Cannot save a member with no UUID!"
		Sql sql = getSql()

		sql.execute("DELETE FROM ${access_list} WHERE uuid=?".toString(), [member.uuid])

		//id, uuid, name, type, plot, reg
		sql.withBatch("INSERT INTO ${access_list} VALUES(NULL, ?, ?, ?, ?)".toString()) { BatchingPreparedStatementWrapper ps ->
			member.getPlotAccessMap().each { id, level ->
					ps.addBatch([member.uuid, member.name, level.toString(), id])
			}

		}
		sql.close()
	}


	private Region regionFromQuery(row){
		if(row) {
			return new Region(id: row.id, name: row.name, world: row.world, x: row.x, z: row.z,
			h: row.h, w: row.w, createdAt: row.createdAt)
		} else {
			return null
		}
	}

	protected Plot plotFromQuery(row, reg){
		if(row){
			Plot plot = new Plot(id: row.id, region: reg, plotName: row.name, ownerName: row.owner,
			ownerUUID: row.uuid, x: row.x, z: row.z, h: row.h, w: row.w, world: world, accessMode: row.accessmode,
			createdAt: row.createdAt, type: PlotMaster.getInstance().getPlotType(world, row.type))
			plot.settingsFromJson(row.settings)
			plot.metadataFromJson(row.metadata)
			return plot
		}
		return null
	}


	private Region loadPlotsForRegion(Sql sql, Region reg){
		if(reg)
			sql.eachRow("SELECT * FROM ${plots} WHERE region=${reg.id}".toString()) { row ->
				def plot = plotFromQuery(row, reg)
				if(plot) {
					reg.plots.put(plot.id, plot)
				}
			}
		return reg
	}

	@Override
	public void deleteRegion(Region region) {
		Sql sql = getSql()

		sql.execute "DELETE FROM ${plots} WHERE region=${region.getId()}".toString()
		sql.execute "DELETE FROM ${regions} WHERE id=${region.getId()}".toString()

		sql.close()
	}

	@Override
	public void deletePlot(Plot plot) {
		Sql sql = getSql()
		sql.execute "DELETE FROM ${plots} WHERE id=${plot.getId()}".toString()
		sql.close()
	}


	Sql getSql(){
		new Sql(ds)
	}

	private closeReturn(sql, ret){
		sql.close()
		ret
	}



}
