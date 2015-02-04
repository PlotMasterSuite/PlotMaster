package org.mcsg.plotmaster.backend

class BackendManager {

	private static Map<String, Class<? extends Backend>> backends = [:]
	
	static Backend backend;
	
	static initBackend(String name){
		assert !backend, "A backend is already loaded!"
		
		backend = backends.get(name).newInstance();
		backend.load()
	}
	
	
	static registerBackend(String name, Class<? extends Backend> backend){
		backends.put(name, backend)
	}
	

	
}
