
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import input.Input;
import input.Joystick;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.naming.ConfigurationException;

import org.lwjgl.Sys;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.system.MemoryUtil;


import utils.DebugConfig;
import utils.WinConfig;

public class Driver implements Runnable{
	
	// Directories
		private static String configDIR = "config.properties";
		private static String debugDIR = "debug.properties";
	
	// Win constraint
		public static int HEIGHT;	
		public static int WIDTH;	
		public static int SCALE;	
		public static int FPS;
		
	// LWJGL variables
		private static GLFWErrorCallback errorCallback;
		private static long windowID;
		private boolean isRunning;
		
	// Game Thread
		private Thread thread;
		
	// For timer
		private long variableYieldTime, lastTime;
		
	public void start(){
    	thread = new Thread(this, "Main Thread");
    	System.out.println("Started main thread!");
    	// start runs method run
    	thread.start();
    	}
		
	public void run(){
		try {
            init();

            // Enter the update loop: keep refreshing the window as long as the window isn't closed
            while (isRunning) {

				// Get input
        		update();
				// Process data and logic
        		process();
				// Render output
        		render();
        		// FPS capping
                sync(FPS);
        		// Check if window has been closed
                if(glfwWindowShouldClose(windowID) == GL_TRUE)
                	isRunning = false;
        	}

            // It's important to release the resources when the program has finished to prevent dreadful memory leaks
            glfwDestroyWindow(windowID);
            
        } finally {
            // Destroys all remaining windows and cursors (LWJGL JavaDoc)
            glfwTerminate();
            errorCallback.release();
        }
		System.out.println("Ended main thead!");
	}
		
	public void update(){
        // Polls the user input. This is very important, because it prevents your application from becoming unresponsive
    	glfwPollEvents();
    	   	
    	// keyboard polling
		if(Input.keyMap[GLFW_KEY_ESCAPE])
    		isRunning = false;
    	
    	if(Input.keyMap[GLFW_KEY_SPACE])
    		System.out.println("Space bar pressed!");
    	
    	if(Input.keyMap[GLFW_KEY_C])
            Input.initializeJoysticks();
    	
    	// Joy stick polling
    	for(int i = 0; i < 4; i++)
			if (Input.joySlots[i] == GL_TRUE)
				Input.joystick[i].pollInput();
    	}
	
	public void process(){
		// check if joy stick is attached
		Input.checkJoystickConnection();
		
		// Joy stick buttons
		for(int j = 0; j < Input.joyTotal; j++)
		for(int i = 0; i < Joystick.MAXBUTTONS; i++)
	    	if(Input.joystick[j].buttonMap[i] == true){
	    		System.out.println(i + " is pressed.");
	    		if(i == Joystick.DI_X)
	    	        glClearColor(0.8f, 0.898f, 1.0f, 1.0f);
	    		if(i == Joystick.DI_A)
	    	        glClearColor(0.467f, 0.867f, 0.467f, 1.0f);
	    		if(i == Joystick.DI_Y)
	    	        glClearColor(0.992f, 0.992f, 0.588f, 1.0f);
	    		if(i == Joystick.DI_B)
	    	        glClearColor(1.0f, 0.412f, 0.38f, 1.0f);
	    		}
		
		// joystick sticks
		for(int j = 0; j < Input.joyTotal; j++)
		for(int i = 0; i < Joystick.MAXAXIS; i++)
	    if (Input.joystick[j].axisMap[i] < -0.95f || Input.joystick[0].axisMap[i] > 0.95f) {
	        System.out.println("Axis " + i + " is at full-range!");
	    } else if (Input.joystick[j].axisMap[i] < -0.5f || Input.joystick[0].axisMap[i] > 0.5f) {
	        System.out.println("Axis " + i + " is at mid-range!");
	    }
		
		//State manager
		//StateManager.runState();
	}
	
	public void render(){
        // Clear the contents of the window (try disabling this and resizing the window – fun guaranteed)
    	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // render color
    	int errno = glGetError();
    	if(errno != GL_NO_ERROR)
    		System.err.println("Game error no.: " + errno);
        // Swaps the front and back framebuffers, this is a very technical process which you don't necessarily
        // need to understand. You can simply see this method as updating the window contents.
    	glfwSwapBuffers(windowID);
    	}
	
	public static void main(String[] args) {
		new Driver().start();
    }
	
	public void init(){
		isRunning = true;
		
		// loads windows constraints and debug info
		new DebugConfig(debugDIR);
		new WinConfig(configDIR);
		
		loadConfig();
		
		System.out.println("Initializing renderer...");
		
		// Set the error handling code: all GLFW errors will be printed to the system error stream (just like println)
        errorCallback = Callbacks.errorCallbackPrint(System.err);
        glfwSetErrorCallback(errorCallback);

        // Initialize GLFW:
        int glfwInitializationResult = glfwInit(); // initialize GLFW and store the result (pass or fail)
        if (glfwInitializationResult == GL_FALSE)
            throw new IllegalStateException("GLFW initialization failed");

        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        if(WinConfig.RESIZE){ glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); }// the window will be re-sizable
        else glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
        
        // Configure the GLFW window
        windowID = glfwCreateWindow(
                WIDTH * SCALE, HEIGHT * SCALE,   // Width and height of the drawing canvas in pixels
                WinConfig.TITLE,     // Title of the window
                MemoryUtil.NULL, // Monitor ID to use for full screen mode, or NULL to use windowed mode (LWJGL JavaDoc)
                MemoryUtil.NULL); // Window to share resources with, or NULL to not share resources (LWJGL JavaDoc)

        if (windowID == MemoryUtil.NULL)
            throw new IllegalStateException("GLFW window creation failed");

        glfwMakeContextCurrent(windowID); // Links the OpenGL context of the window to the current thread (GLFW_NO_CURRENT_CONTEXT error)
        
        if(FPS == 0)
        	glfwSwapInterval(1);// Enable VSync, which effective caps the frame-rate of the application to 60 frames-per-second
        else glfwSwapInterval(0);
        
        // Get the resolution of the primary monitor
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
            windowID,
            (GLFWvidmode.width(vidmode) - WIDTH * SCALE) / 2,
            (GLFWvidmode.height(vidmode) - HEIGHT * SCALE) / 2
        );
        
        // Input handling
        glfwSetKeyCallback(windowID, new Input());
        
        glfwShowWindow(windowID);

        // If you don't add this line, you'll get the following exception:
        //  java.lang.IllegalStateException: There is no OpenGL context current in the current thread.
        GLContext.createFromCurrent(); // Links LWJGL to the OpenGL context
        
    	// Set renderer color (optional)
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        System.out.println("LWJGL version: " + Sys.getVersion());
        System.out.println("OpenGL version: " + glGetString(GL_VERSION));
        //enableShaders();

		// new state manager
		//new StateManager(DebugConfig.INITSTATE, DebugConfig.ISGAME);
        
    	System.out.println("Completed initialization!\n");
        
        
	}
	
	public void enableShaders(){/*
		glActiveTexture(GL_TEXTURE1);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
		Shader.loadAll();
		
		Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
		StateManager.BG.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.BG.setUniform1i("tex", 1);
		
		Shader.BIRD.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.BIRD.setUniform1i("tex", 1);
		
		Shader.PIPE.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.PIPE.setUniform1i("tex", 1);*/
	}

	public void sync(int FPS) {
		//TODO: redo timer
		if (FPS <= 0) return;
	          
		long sleepTime = 1000000000 / FPS; // nanoseconds to sleep this frame
		// yieldTime + remainder micro & nano seconds if smaller than sleepTime
		long yieldTime = Math.min(sleepTime, variableYieldTime + sleepTime % (1000 * 1000));
		long overSleep = 0; // time the sync goes over by
	          
		try {
	    	while (true) {
	        	long t = System.nanoTime() - lastTime;
	                  
	        	if (t < sleepTime - yieldTime) {
	                    Thread.sleep(1);
	        	}else if (t < sleepTime) {
	                    // burn the last few CPU cycles to ensure accuracy
	                    Thread.yield();
	        	}else {
	            	overSleep = t - sleepTime;
	            	break; // exit while loop
	         	}
	    	}
		} catch (InterruptedException e) {
	    	 e.printStackTrace();
		}finally{
	    	lastTime = System.nanoTime() - Math.min(overSleep, sleepTime);
	             
	    	// auto tune the time sync should yield
	    	if (overSleep > variableYieldTime) {
	    	// increase by 200 microseconds (1/5 a ms)
	        	variableYieldTime = Math.min(variableYieldTime + 200*1000, sleepTime);
	        } else if (overSleep < variableYieldTime - 200*1000) {
	        // decrease by 2 microseconds
	        	variableYieldTime = Math.max(variableYieldTime - 2*1000, 0);
	        }
	    }
	}
	
	public void loadConfig(){
		System.out.println("Loading config file.");

		try {
		WinConfig.loadConfig();
		HEIGHT = WinConfig.HEIGHT;
		WIDTH  = WinConfig.WIDTH;
		SCALE  = WinConfig.SCALE;
		FPS    = WinConfig.FPS;
		System.out.println("LOADED!\n");
		System.out.println("Window variables:");
		System.out.println("Title:   " + WinConfig.TITLE);
		System.out.println("WIDTH:   " + WinConfig.WIDTH);
		System.out.println("HEIGHT:  " + WinConfig.HEIGHT);
		System.out.println("SCALE:   " + WinConfig.SCALE);
		System.out.println("RESIZE:  " + WinConfig.RESIZE);
		System.out.println("FPS CAP: " + WinConfig.FPS);
		if(SCALE != 1) System.out.println("True dimentions: "+ (WIDTH * SCALE) + " x " + (HEIGHT * SCALE));
		if(FPS == 0) System.out.println("Vsync enabled!");
		System.out.println("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveConfig(){
		try {
			WinConfig.saveConfig(HEIGHT, WIDTH, SCALE, FPS);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
