package input;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import java.util.HashMap;

import org.lwjgl.glfw.GLFWKeyCallback;

/*
 * Class that extends GLFWKeyCallback to get the key being used
 */
public class KeyboardInput extends GLFWKeyCallback{

	/**
	 * Array holding booleans of each key to keep track of which one is being used
	 */
	private static boolean keys[] = new boolean [65536];
	
	/**
	 * Hash maps containing bindings for key presses and releases.
	 */
	private HashMap<Integer, Runnable> keyPressed = new HashMap<Integer, Runnable>();
	private HashMap<Integer, Runnable> keyReleased = new HashMap<Integer, Runnable>();

	/**
	 * Callback method called when a key event is processed.
	 * @return [void]
	 */
	public void invoke(long window, int key, int scancode, int action, int mods) {
		
		keys[key] = action != GLFW_RELEASE;
		
		if(action == GLFW_RELEASE){
			try{
				keyReleased.get(key).run();
			}catch(Exception e) {}
		}else {
			try{
				keyPressed.get(key).run();
			}catch(Exception e) {}
		}
		
	}
	
	/**
	 * Method to check if the key is being pressed down or being released
	 * @param key the keycode corresponding to the action
	 * @return [boolean] True if key is being pressed, false otherwise.
	 */
	public static boolean isKeyDown(int key){
		return keys[key];
	}
	
	/**
	 * Registers a key released action for the specified key.
	 * @param key The key code of the wanted key.
	 * @param action The action to be executed upon the key's release.
	 * @return [void]
	 */
	public void registerKeyUp(int key, Runnable action) {
		keyReleased.put(key, action);
	}
	
	/**
	 * Registers a key pressed action for the specified key.
	 * @param key The key code of the wanted key.
	 * @param action The action to be executed upon the key's press.
	 * @return [void]
	 */
	public void registerKeyDown(int key, Runnable action) {
		keyPressed.put(key, action);
	}
	
}

