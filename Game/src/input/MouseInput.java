package input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

import camera.CameraTPS;
import main.Display;
import main.Game;

public class MouseInput extends GLFWCursorPosCallback{

	private double xpos, ypos;
	private int dx = 0, dy = 0;
	private CameraTPS cam;
	
	public MouseInput() {
		
		xpos = 0;
		ypos = 0;
		
	}
	
	/**
	 * Method being called whenever it listens to a change in position of the cursor
	 */
	@Override
	public void invoke(long window, double xpos, double ypos) {
		// TODO Auto-generated method stub
		
		setXpos(xpos);
		setYpos(ypos);
		
		dx = (int) ((Display.getWidth() / 2) - xpos);
		dy = (int) ((Display.getHeight() / 2) - ypos);
		
		cam = (CameraTPS)Game.getCamera();
		
	}
	
	public void tick() {
		
		if(cam != null)
			cam.move(dx, dy);
		
	}
	
	public double getXpos() {
		return xpos;
	}

	public void setXpos(double xpos) {
		this.xpos = xpos;
	}

	public double getYpos() {
		return ypos;
	}

	public void setYpos(double ypos) {
		this.ypos = ypos;
	}
	
}
