package org.mcsg.plotmaster.rest;

import static spark.Spark.*;

import org.mcsg.plotmaster.Plot
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


	//Yes, PM is 100% standalone and the core can be started without a server
	public static void main(args){

		Test t1 = new Test(hi: "yes")
		Test2 t2 = new Test2()

		transformClass(t1, t2)

		println t2.hi

		System.exit(0)

		pm = new PlotMaster()

		pm.onLoad(new DefaultConsole())
		pm.onEnable()

		man = PlotMaster.getInstance().getManager("world")

		new RestServer().start()

	}

	private static transformClass(class1, class2){
		class1.getProperties().each { key, value ->
			if(!(value instanceof Class))
				class2.getMetaClass().setProperty(class2, key as String, value)
		}
	}


	static class Test {
		String hi
	}

	static class Test2 extends Test {
		String ok = "no"
	}

	static PlotMaster pm
	static PlotManager man
	Gson gson = new GsonBuilder().setPrettyPrinting().create()

	void start(){
		before { Request req, Response res ->
			pm.sendConsoleMessage("&7[WebServer] Request to ${req.url()}")

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
					def say = [href: getUrl(req, "/region/$reg.id"), id: reg.id] as HashMap<String, Object>
					def plots = []
					for(Plot plot : reg.plots.values()) {
						plots.add([	href: getUrl(req, "/plot/${plot.id}"), id: plot.id])
					}
					say.put("plots", plots)
					regions.add(say)
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
			def regions = []
			int start = ((req.queryParams("start")?.toInteger()) ?: 1)
			int amount = ((req.queryParams("amount")?.toInteger()) ?: 25)
			amount = (amount < 25) ? amount : 25

			for(index in start..start+amount-1){
				def plot = man.getPlot(index, null)
				if(plot){
					regions.add([
						_href: getUrl(req, "/plot/${plot.id}"),
						id: plot.id,
						region: [
							_href: getUrl(req, "/region/${plot.getRegion().id}"),
							id: plot.id
						] ])
				}
			}
			return toJson(regions)
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
