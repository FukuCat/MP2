package utils;

import game.GamePanel;
import game.LoadPanel;
import game.MenuPanel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import utils.ShaderUtils;

public class ObjectLoader extends Thread{
	private Thread thread;
	private static InputStream inpStream;
	private String propDir;
	public String VERT;
	public String FRAG;
	public boolean done;
	
	public int panelType;
	public static final int LOADPANEL = 0;
	public static final int MENUPANEL = 1;
	public static final int GAMEPANEL = 2;
	
	public ObjectLoader(String propDir, int panelType){
		this.propDir = propDir;
		this.panelType = panelType;
		inpStream = getClass().getClassLoader().getResourceAsStream(propDir);
		done = false;
	}
	
	public void start(){
		if(thread == null){
	        thread = new Thread (this, "ObjectLoader");
			thread.run();
		}
	}
	
	public void run(){
		//TODO: loading method
		done = true;
		
		if(done)
		switch(panelType){
			case 0:
				LoadPanel.loaded = true;
				break;
			case 1:
				MenuPanel.loaded = true;
				break;
			case 2:
				GamePanel.loaded = true;
				break;
		}
	}
	
	public void loadFile() throws IOException {
		try{
		Properties config = new Properties();
		
		// Check if exist
		if(inpStream != null)
			config.load(inpStream);
		else
			throw new FileNotFoundException("Property file " + propDir + " not found!");
		
		// Field to fill
		VERT = config.getProperty("VERT", null);
		FRAG = config.getProperty("FRAG", null);

		// Check for problems
		} catch (Exception e) {
			System.err.println("LoadProperties");
			e.printStackTrace();
		// Close stream
		} finally {
			inpStream.close();
		}
	}
	
	public void loadShader(String vertPath, String fragPath){
		//ID = ShaderUtils.load(vertPath, fragPath);
	}
}
