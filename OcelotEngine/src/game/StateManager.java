package game;

public class StateManager {

	public static int currState;
	public static MenuPanel menuPanel;
	public static GamePanel gamePanel;
	public static LoadPanel loadPanel;
	public static int totalMP;
	public static int totalGP;
	public static int totalLP;
	public static boolean isGame;
	public static boolean loaded;
	
	public StateManager(int state, boolean isGame){
		loadPanel = new LoadPanel(currState, "");
		gamePanel = new GamePanel(currState, "");
		menuPanel = new MenuPanel(currState, "");
		StateManager.currState = state;
		StateManager.isGame = isGame;
		// TODO: make a recursive object loader
		totalMP = totalGP = totalLP = 1;
	}
	
	public static void changeState(int currState, boolean isGame){
		loaded = false;
		StateManager.isGame = isGame;
		StateManager.currState = currState;
	}
	
	public static void runState(){
		if(!loaded) {
			loadPanel.start();
			if(isGame){
				gamePanel.load();
				if(GamePanel.loaded)
					loaded = true;
			} else {
				menuPanel.load();
				if(MenuPanel.loaded)
					loaded = true;
			}
		} else {
			if(isGame){
				if(GamePanel.loaded)
					gamePanel.render();
				else loaded = false;
			} else {
				if(MenuPanel.loaded)
					menuPanel.render();
				else loaded = false;
			}
			
		}
	}
}
