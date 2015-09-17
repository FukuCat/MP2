package game;

import utils.ObjectLoader;

public class LoadPanel{

	private static ObjectLoader loadThread;
	public static boolean loaded;
	
	public LoadPanel(int currState, String DIR) {
		LoadPanel.loaded = false;
		loadThread = new ObjectLoader(DIR, 0);
	}

	public void start(){
		if(!loaded)
			loadThread.start();
		else
			render();
	}
	
	public void render() {
		
	}
}
