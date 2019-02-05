package util;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glEnable;

public class GLUtils {
	
	public static void useBlending(boolean state) {
		glEnable(GL_BLEND);
	}
	
}
