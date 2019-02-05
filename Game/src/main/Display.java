package main;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import input.KeyboardInput;
import input.MouseInput;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Util class for frame related functions.
 */
public class Display {
	
	/**
	 * Handle to the window.
	 */
	private static long hWnd;
	
	/**
	 * Size of the display.
	 */
	private static int displayWidth, displayHeight;
	
	/**
	 * Keyboard and mouse input for the display.
	 */
	public static KeyboardInput keyIn = new KeyboardInput();
	public static MouseInput mouseIn = new MouseInput();
	
	/**
	 * Returns the main screen size.
	 * @return [{@link Dimension}] The size of the primary screen.
	 */
	public static Dimension getSize() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}
	
	/**
	 * Creates the display.
	 * @param width The width of the display in pixels.
	 * @param height The height of the display in pixels.
	 * @param title The title of the display.
	 * @return [<b>void</b>]
	 */
	public static void createDisplay(int width, int height, String title) {
		
		// Set the display size.
		setDisplayWidth(width);
		setDisplayHeight(height);
		
		// Create the window.
		hWnd = glfwCreateWindow(width, height, title, NULL, NULL);
		
		// Set the callback function for key events.
		glfwSetKeyCallback(hWnd, keyIn);
		
		// Set the callback function for mouse movement.
		glfwSetCursorPosCallback(hWnd, mouseIn);
		
		if(hWnd == NULL)
			throw new IllegalStateException("Failed to create window.");

		// Get the thread stack and push a new frame.
		try (MemoryStack stack = stackPush()) {
			
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			// Get the window size passed to glfwCreateWindow.
			glfwGetWindowSize(hWnd, pWidth, pHeight);

			// Get the resolution of the primary monitor.
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window.
			glfwSetWindowPos( 	hWnd,
								(vidmode.width() - pWidth.get(0)) / 2,
								(vidmode.height() - pHeight.get(0)) / 2);
			
		} 

		// Make the OpenGL context current.
		glfwMakeContextCurrent(hWnd);
		
		// Disable v-sync.
		glfwSwapInterval(0);

		// Make the window visible.
		glfwShowWindow(hWnd);
		
		// Grab the cursor.
		glfwSetInputMode(hWnd, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
		
		// Create OpenGL capabilities.
		GL.createCapabilities();
		
	}
	
	/**
	 * Creates the display.
	 * @param dimension The dimension of the display.
	 * @param title The title of the display.
	 * @return [<b>void</b>]
	 */
	public static void createDisplay(Dimension dimension, String title) {
		createDisplay(dimension.width, dimension.height, title);
	}
	
	/**
	 * Updates the display.
	 * @return [<b>void</b>]
	 */
	public static void updateDisplay() {
		
		// Check if display was created.
		if(hWnd == NULL)
			return;
		
		// Swap the frame buffer (show the next frame).
		glfwSwapBuffers(hWnd);

		// Poll keyboard and mouse events.
		glfwPollEvents();
	
	}
	
	/**
	 * Closes the display.
	 * @return [<b>void</b>]
	 */
	public static void closeDisplay() {
		
		// Check if display was created.
		if(hWnd == NULL)
			return;
		
		// Free the window callbacks and destroy the window.
		glfwFreeCallbacks(hWnd);
		glfwDestroyWindow(hWnd);
		
	}
	
	/**
	 * Returns whether or not the display should be closed.
	 * @return [<b>boolean</b>] True if display needs to be closed, false otherwise.
	 */
	public static boolean isCloseRequested() {
		// Check if display was created.
		if(hWnd == NULL)
			return false;
		
		return glfwWindowShouldClose(hWnd);
	}
	
	/**
	 * Sets the clear color of the display.
	 * @param r The red component of the color to use.
	 * @param g The green component of the color to use.
	 * @param b The blue component of the color to use.
	 * @param a The alpha component of the color to use.
	 * @return [<b>void</b>]
	 */
	public static void clearColor(float r, float g, float b, float a) {
		glClearColor(r, g, b, a);
	}
	
	/**
	 * Returns the window handle for the current display.
	 * @return [<b>long</b>] A handle to the window.
	 */
	public static long getWindow() {
		return hWnd;
	}

	/**
	 * Returns the display's width in pixels.
	 * @return [<b>int</b>] The width of the frame in pixels.
	 */
	public static int getWidth() {
		return displayWidth;
	}
	
	/**
	 * Returns the display's height in pixels.
	 * @return [<b>int</b>] The height of the frame in pixels.
	 */
	public static int getHeight() {
		return displayHeight;
	}
	
	/**
	 * Sets the display's width in pixels.
	 * @param displayWidth The new width of the frame in pixels.
	 * @return [<b>void</b>]
	 */
	public static void setDisplayWidth(int displayWidth) {
		Display.displayWidth = displayWidth;
	}
	
	/**
	 * Sets the display's height in pixels.
	 * @param displayHeight The new height of the frame in pixels.
	 * @return [<b>void</b>]
	 */
	public static void setDisplayHeight(int displayHeight) {
		Display.displayHeight = displayHeight;
	}

}
