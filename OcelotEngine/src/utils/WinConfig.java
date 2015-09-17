package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.naming.ConfigurationException;

public class WinConfig {
	static InputStream inpStream;
	static String propDir;
	public static String  TITLE;
	public static int     HEIGHT;
	public static int     WIDTH;
	public static int     SCALE;
	public static boolean RESIZE;
	public static int     FPS;

	
	public WinConfig(String propDir){
		WinConfig.propDir = propDir;
		inpStream = getClass().getClassLoader().getResourceAsStream(propDir);
		
	}
	
	public static void loadConfig() throws IOException {
		try{
		Properties config = new Properties();
		
		// Check if exist
		if(inpStream != null)
			config.load(inpStream);
		else
			throw new FileNotFoundException("Property file " + propDir + " not found!");
		
		// Field to fill
		TITLE = config.getProperty("WIN_TITLE", "OcelotEngine");
		HEIGHT = Integer.parseInt(config.getProperty("WIN_HEIGHT", "540").trim());
		WIDTH = Integer.parseInt(config.getProperty("WIN_WIDTH", "960").trim());
		SCALE = Integer.parseInt(config.getProperty("WIN_SCALE", "1").trim());
		int tResize = Integer.parseInt(config.getProperty("RESIZEABLE", "0").trim()); // 0 means vsync enabled
		FPS = Integer.parseInt(config.getProperty("FPS_CAP", "0").trim()); // 0 means vsync enabled
		
		// Fix input
		if(HEIGHT < 1)
			HEIGHT = 1;
		if(WIDTH < 1)
			WIDTH = 1;
		if(SCALE < 1)
			SCALE = 1;
		if(tResize < 1){ RESIZE = false;}
		else RESIZE = true;
		if(FPS < 0)
			FPS = 0;
		// Check for problems
		} catch (Exception e) {
			System.err.println("LoadProperties");
			e.printStackTrace();
		// Close stream
		} finally {
			inpStream.close();
		}
	}
	
	public static void saveConfig(int HEIGHT, 
			int WIDTH, int SCALE, int FPS) throws ConfigurationException{
		try{
		Properties config = new Properties();
		// Set fields
		config.setProperty("WIN_TITLE", TITLE);
		config.setProperty("WIN_HEIGHT", Integer.toString(HEIGHT));
		config.setProperty("WIN_WIDTH", Integer.toString(WIDTH));
		config.setProperty("WIN_SCALE", Integer.toString(SCALE));
		if(RESIZE)
			config.setProperty("RESIZEABLE", "1");
		else
			config.setProperty("RESIZEABLE", "0");
		config.setProperty("FPS_CAP", Integer.toString(FPS));
		// Save operation
		File fTemp = new File(propDir);
		OutputStream sTemp = new FileOutputStream(fTemp);
		config.store(sTemp, "");
		}catch(Exception e){
			System.err.println("SaveProperties");
			e.printStackTrace();
		}
	}
}
