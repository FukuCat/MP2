package input;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.glfw.GLFWKeyCallback;

public class Input extends GLFWKeyCallback{

	public static final int MAXJOYSTICKS = 4;
	
	public static boolean[] keyMap = new boolean[65536];
	
	public static Joystick[] joystick = new Joystick[MAXJOYSTICKS];
	public static int[] joySlots = new int[MAXJOYSTICKS];
	public static String[] joyStickName = new String[MAXJOYSTICKS];
	public static int joyTotal = 0;
	private static boolean joyNotified= false;
	
	// up to 4 joysticks
	public static void initializeJoysticks(){
		System.out.println("Searching and connecting with joysticks...");
		joyTotal = 0;
		if((joySlots[joyTotal] = glfwJoystickPresent(GLFW_JOYSTICK_1)) == GL_TRUE){
			joyStickName[joyTotal] = glfwGetJoystickName(GLFW_JOYSTICK_1);
			joystick[joyTotal] = new Joystick(GLFW_JOYSTICK_1);
			joyTotal++;
		}
		if((joySlots[joyTotal] = glfwJoystickPresent(GLFW_JOYSTICK_2)) == GL_TRUE){
			joyStickName[joyTotal] = glfwGetJoystickName(GLFW_JOYSTICK_2);
			joystick[joyTotal] = new Joystick(GLFW_JOYSTICK_2);
			joyTotal++;
		}
		if((joySlots[joyTotal] = glfwJoystickPresent(GLFW_JOYSTICK_3)) == GL_TRUE){
			joyStickName[joyTotal] = glfwGetJoystickName(GLFW_JOYSTICK_3);
			joystick[joyTotal] = new Joystick(GLFW_JOYSTICK_3);
			joyTotal++;
		}
		if((joySlots[joyTotal] = glfwJoystickPresent(GLFW_JOYSTICK_4)) == GL_TRUE){
			joyStickName[joyTotal] = glfwGetJoystickName(GLFW_JOYSTICK_4);
			joystick[joyTotal] = new Joystick(GLFW_JOYSTICK_4);
			joyTotal++;
		}
		for(int i = 0; i < joyTotal; i++)
			if(joySlots[i] == GL_TRUE)
				System.out.println("Controller "+ Input.joyStickName[i] + " at slot " + (i + 1) + " is connected!\n");
		if(joyTotal == 0)
			System.out.println("No joysticks found.");
	}
	
	public static void checkJoystickConnection(){
		if(!joyNotified)
			for(int i = 0; i < joyTotal; i++)
				if(joystick[i].detached){
					System.out.println("Controller "+ Input.joyStickName[i] + " at slot " + (i + 1) + " is disconnected!");
					joyNotified = true;
				}

		if(joyNotified)
			for(int i = 0; i < joyTotal; i++)
				if(!joystick[i].detached){
					System.out.println("Controller "+ Input.joyStickName[i] + " at slot " + (i + 1) + " is connected!");
					joyNotified = false;
				}
	}
	
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if(action == GLFW_PRESS)
			keyMap[key] = true;
		if(action == GLFW_RELEASE){
			keyMap[key] = false;
			}
	}
	
}
