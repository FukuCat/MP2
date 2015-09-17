package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.naming.ConfigurationException;

public class DebugConfig {
	static InputStream inpStream;
	String propDir;
	public static boolean ISGAME;
	public static int     INITSTATE;

	
	public DebugConfig(String propDir){
		this.propDir = propDir;
		inpStream = getClass().getClassLoader().getResourceAsStream(propDir);
		
	}
	
	public void loadConfig() throws IOException {
		try{
		Properties config = new Properties();
		
		// Check if exist
		if(inpStream != null)
			config.load(inpStream);
		else
			throw new FileNotFoundException("Property file " + propDir + " not found!");
		
		// Field to fill
		INITSTATE = Integer.parseInt(config.getProperty("INITSTATE", "0").trim());
		int tIsgame = Integer.parseInt(config.getProperty("ISGAME", "0").trim()); // 0 is menu
		
		// Fix input
		if(INITSTATE < 0)
			INITSTATE = 0;
		if(tIsgame < 1){ ISGAME = false;}
		else ISGAME = true;
		// Check for problems
		} catch (Exception e) {
			System.err.println("LoadProperties");
			e.printStackTrace();
		// Close stream
		} finally {
			inpStream.close();
		}
	}
	
	public void saveConfig(int HEIGHT, 
			int WIDTH, int SCALE, int FPS) throws ConfigurationException{
		try{
		Properties config = new Properties();
		// Set fields
		config.setProperty("INITSTATE", Integer.toString(SCALE));
		if(ISGAME)
			config.setProperty("ISGAME", "1");
		else
			config.setProperty("ISGAME", "0");
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
