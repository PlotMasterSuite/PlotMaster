package org.mcsg.plotmaster.rest;

import static spark.Spark.*;

import org.mcsg.plotmaster.PlotMaster;
import org.mcsg.plotmaster.bridge.PMConsole;
import org.mcsg.plotmaster.managers.PlotManager
import org.mcsg.plotmaster.utils.DefaultConsole
import org.mcsg.plotmaster.utils.PlatformAdapter;
import org.mcsg.plotmaster.utils.PlatformAdapter.PlatformType

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import groovy.transform.CompileStatic;
import spark.Request
import spark.Response
import spark.Route

@CompileStatic
class RestServer {

	
	
	public static void main(args){		
		pm = new PlotMaster()
		
		pm.onLoad(new DefaultConsole())
		
		pm.onEnable()
		
		man = PlotMaster.getInstance().getManager("world")
		

		new RestServer().start()
	}
	
	static PlotMaster pm
	static PlotManager man
	Gson gson = new GsonBuilder().setPrettyPrinting().create()
	
	void start(){
		before { Request req, Response res ->
			res.type("application/json")
		}
		
		
		get("/regions") { Request req, Response res ->
			def regions = []
			int start = ((req.queryParams("start")?.toInteger()) ?: 1)
			int amount = ((req.queryParams("amount")?.toInteger()) ?: 25)
			amount = (amount < 25) ? amount : 25
			
			for(index in start..start+amount-1){
				def reg = man.getRegion(index, null)
				if(reg){
					regions.add([href: getUrl(req, "/region/$reg.id"), id: reg.id])
				} 
			}
			return toJson(regions)
		}
		
		get("/region/:id") { Request req, Response res ->
			def id = req.params("id")
			if(id.contains(",")) {
				def s = id.split(",")
				def x = s[0].toInteger()
				def z = s[1].toInteger()
				
				println "Getting at $x, $z"
				
				return toJson(man.getRegionAt(x, z, null))
			}
			return toJson(man.getRegion(id.toInteger(), null))
		}
		
		get("/plots") { Request req, Response res ->
			
		}
		
		get("/plot/:id") { Request req, Response res ->
			def id = req.params("id")
			if(id.contains(",")) {
				def s = id.split(",")
				def x = s[0].toInteger()
				def z = s[1].toInteger()
				
				println "Getting at $x, $z"
				
				return toJson(man.getPlotAt(x, z, null))
			}
			return toJson(man.getPlot(id.toInteger(), null))		
			}
	}

	String getUrl(Request req, String url){
		return req.url().replace(req.uri(), "") + url
	}
	
	String toJson(Object o){
		gson.toJson(o)
	}

}
