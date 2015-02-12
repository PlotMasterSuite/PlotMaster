package org.mcsg.plotmaster

import org.mcsg.plotmaster.backend.BackendManager;
import org.mcsg.plotmaster.backend.flatfile.FlatFileBackend;
import org.mcsg.plotmaster.backend.sql.mysql.MysqlBackend;
import org.mcsg.plotmaster.backend.sql.sqlite.SQLiteBackend;
import org.mcsg.plotmaster.bridge.PMCommandSender



class PlotMaster{

	void onLoad(PMCommandSender console){
		console.sendMessage ("&9PlotMaster Suite is loading")
		
		BackendManager.registerBackend("flatfile", FlatFileBackend.class)
		BackendManager.registerBackend("mysql", MysqlBackend.class)
		BackendManager.registerBackend("sqlite", SQLiteBackend.class)
		
		
		
		
	}
	
	void onEnable(){
		
	}

	void onDisable(){

	}
	


}
