package game;

import utils.ObjectLoader;

public class MenuPanel {

	private static ObjectLoader loadThread;
	public static boolean loaded, initLoad;
	
	public MenuPanel(int currState, String DIR) {
		MenuPanel.loaded = false;
		MenuPanel.initLoad = false;
		loadThread = new ObjectLoader(DIR, 1);
	}

	public void start(){
		if(!loaded)
			load();
		else
			render();
	}
	
	public void render() {
		if(loaded){
			
		}
	}

	public void load() {
		if(!initLoad){
			initLoad = true;
			loadThread.start();
		}
	}

	
}
