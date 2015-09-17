package input;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class Joystick {

	public static final int MAXBUTTONS = 16;
	public static final int MAXAXIS = 4;
	
	public boolean[] buttonMap = new boolean[MAXBUTTONS];
	// x y axis
	// up right down left
	public float[] axisMap = new float[MAXAXIS];
	public int joystickID;
	public boolean detached;

	// "Microsoft PC-joystick driver" Direct Input
	// Recommended
	public static final int DI_X = 0;
	public static final int DI_A = 1;
	public static final int DI_B = 2;
	public static final int DI_Y = 3;
	public static final int DI_L1 = 4;
	public static final int DI_R1 = 5;
	public static final int DI_L2 = 6;
	public static final int DI_R2 = 7;
	public static final int DI_Back = 8;
	public static final int DI_Start = 9;
	public static final int DI_L3 = 10;
	public static final int DI_R3 = 11;
	public static final int DI_Up = 12;
	public static final int DI_Down = 14;
	public static final int DI_Left = 15;
	public static final int DI_Right = 13;

	/* "Microsoft PC-joystick driver" X Input
	 * crap - never use!
	 * 0 = A
	 * 1 = B
	 * 2 = X
	 * 3 = Y
	 * 4 = L1
	 * 5 = R1
	 * 6 = Back
	 * 7 = Start
	 * XX= L2
	 * XX= R2
	 * 10= D Up
	 * 11= D Right
	 * 12= D Down
	 * 13= D Left
	 * 8 = L3
	 * 9 = R3
	 */
	
	public Joystick(int joystickID){
		this.joystickID = joystickID;
		detached = false;
	}
	
	public void pollInput(){
		pollButtons();
		pollAxis();
	}
	
	public void pollButtons(){
		if(glfwJoystickPresent(joystickID) == GL_TRUE){
			detached = false;
			ByteBuffer buttonArray = glfwGetJoystickButtons(joystickID);

			for(int buttonID = 0; buttonArray.hasRemaining() && MAXBUTTONS > buttonID; buttonID++){
				
					int temp = buttonArray.get();
					if (temp == GLFW_RELEASE)
						buttonMap[buttonID] = false;
					if (temp == GLFW_PRESS) {
						buttonMap[buttonID] = true;
					}
				
			}
		} else detached = true;
	}
	
	public void pollAxis(){
		if(glfwJoystickPresent(joystickID) == GL_TRUE){
			detached = false;
			FloatBuffer axesArray = glfwGetJoystickAxes(joystickID);

			for(int axisID = 0; axesArray.hasRemaining() && MAXAXIS > axisID; axisID++) 
			    	axisMap[axisID] = axesArray.get();
			    
		} else detached = true;
	}
	
}
