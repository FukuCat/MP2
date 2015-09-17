package game;

import utils.ObjectLoader;

public class GamePanel {

	private static ObjectLoader loadThread;
	public static boolean loaded, initLoad;
	
	public GamePanel(int currState, String DIR) {
		GamePanel.loaded = false;
		GamePanel.initLoad = false;
		loadThread = new ObjectLoader(DIR, 2);
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
