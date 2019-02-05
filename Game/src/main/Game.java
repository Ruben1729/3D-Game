package main;

import static java.lang.Math.toRadians;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;

import java.io.File;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL11;

import GUI.Button;
import animation.AnimatedModel;
import animation.Animation;
import camera.Camera;
import camera.CameraTPS;
import engine.Loader;
import engine.Mesh;
import engine.Texture;
import engine.TexturedMesh;
import entities.Entity;
import entities.Player;
import lights.Light;
import lights.LightController;
import parsers.ColladaGeometry;
import parsers.ColladaParser;
import parsers.ObjParser;
import render.AnimatedModelRenderer;
import render.EntityRenderer;
import textMeshCreator.FontType;
import textMeshCreator.GUIText;
import textRendering.TextMaster;
import util.Logger;


public class Game extends Thread{
	
	/**
	 * Logger used to log game activity.
	 */
	public static final Logger LOGGER = new Logger("./logs/", null);
	
	/**
	 * Static boolean determining if the game is running or not.
	 */
	private static volatile boolean running = false;
	
	/**
	 * The main thread the game runs on.
	 */
	private static Thread mainThread;
	
	/**
	 * Number of game ticks per second.
	 */
	public static final int TICKS_PER_SEC = 60;
	
	/**
	 * Handle to the window.
	 */
	public long window;
	
	/**
	 * Player of the game.
	 */
	private static Player player;
	
	/**
	 * Camera of the game.
	 */
	private static Camera cam;
	
	/**
	 * Static main method to start up the game.
	 * @param args The command-line arguments.
	 * @return [void]
	 */
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	/**
	 * Main game loop.
	 * @returns [void]
	 */
	public void run() {
		
		// Initialize the game.
		init();
		
		// Create the display.
		Display.createDisplay(1280, 720, "THIS IS A TEST");
		
		// Clear to dark blue.
		Display.clearColor(0, 0.4f, 1.0f, 1.0f);
		
		window = Display.getWindow();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		// Version.
		LOGGER.println(GL11.glGetString(GL11.GL_VERSION));
		
		// TEMPORARY TEST
		Loader l = new Loader();
		FontType font = new FontType(l.loadTexture("tahoma.png", false), new File("res/tahoma.fnt"));
		Button button = new Button(l, "main menu", new float[] {1, 1, 0}, new Vector2f(0, 0.1f), true, font, 1);
		
		// LOAD 2 CUBES
		Mesh cube = ObjParser.loadObjModel("dragon", l);
		Entity cubeEnt = new Entity(new TexturedMesh(cube, null));
		Entity cubeEnt2 = new Entity(new TexturedMesh(cube, null));
		cubeEnt.setPosition(new Vector3f(-4, 0.5f, -20));
		cubeEnt.setRotation(new Vector3f(0, 90, 0));
		cubeEnt.setScale(0.3f);
		cubeEnt2.setPosition(new Vector3f(4, 0.5f, -20));
		cubeEnt2.setRotation(new Vector3f(0, 90, 0));
		cubeEnt2.setScale(0.3f);
		
		ColladaGeometry geom = ColladaParser.parse("./../char.dae");
		Mesh character = l.load(geom.getIndices(), geom.getVertices(), geom.getNormals(), geom.getTexCoords(), geom.getJointIds(), geom.getWeights());
		Texture charTex = l.loadTexture("Character Texture.png", true);
		
		AnimatedModel aChar = new AnimatedModel(character, charTex, geom.getSkin().getRootJoint(), geom.getSkin().getJointNames().length);
		Animation anim = geom.getAnimation();
		aChar.doAnimation(anim);
		aChar.update(0.6f);
		
		Matrix4f persp = new Matrix4f().perspective((float)toRadians(59.0f), (16.0f / 9.0f), 0.1f, 1000.0f);
		EntityRenderer er = new EntityRenderer(persp);
		AnimatedModelRenderer ar = new AnimatedModelRenderer(persp);
		
		player = new Player(new TexturedMesh(character, charTex));
		player.setPosition(new Vector3f(0, 0, -20.0f));
		player.setRotation(new Vector3f(-90, 0, 0));
		
		cam = new CameraTPS(player);
		cam.setPosition(new Vector3f(20, 0, 0));
		cam.lookAt(player.getPosition());
		
		
		
		LightController lm = new LightController();
		Light sun = new Light(new Vector3f(0, 0, -30), new Vector3f(1.0f, 1.0f, 1.0f));
		lm.addLight(sun);
		
		// Register all key bindings used for the game.
		registerKeybinds();
		
		// Main loop.
		long lastFrame = System.nanoTime();
		long lastTime = System.nanoTime();
		long lastTick = System.nanoTime();
		int fps = 0;
		int ticks = 0;
		while(!Display.isCloseRequested()) {
			
			// INFO
			long currentTime = System.nanoTime();
			if(currentTime - lastTime >= 1000000000) {
				LOGGER.println("FPS: " + fps + ", TICKS: " + ticks);
				fps = 0;
				ticks = 0;
				lastTime = currentTime;
			}
			
			// TICK
			long currentTick = System.nanoTime();
			if(currentTick - lastTick >= 1000000000 / TICKS_PER_SEC) {
				tick();
				
				// TEST
				float pos = 15 * (float)Math.sin((double)System.currentTimeMillis() / 100.0f);
				sun.setPosition(new Vector3f(pos, 0, 0));
				
				ticks++;
				lastTick = currentTick;
			}
			
			long currentFrame = System.nanoTime();
			float deltaTime = ((float)(currentFrame - lastFrame) / 1000000000.0f);
			lastFrame = currentFrame;
			
			// RENDER
			glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			er.render(cubeEnt, cam, lm.getLights());
			er.render(cubeEnt2, cam, lm.getLights());
			er.render(player, cam, lm.getLights());
			
			aChar.update(deltaTime);
			ar.render(aChar, cam, new Vector3f(1, 1, 1).normalize());
		
			button.render();
			
			Display.updateDisplay();
			
			fps++;
		}
		
		// Close the display.
		Display.closeDisplay();
		
		button.cleanUp();
		
		// Cleanup any resources we've loaded.
		l.cleanUp();
		
		// Close the game.
		close();
	}
	
	/**
	 * Ticks the game logic.
	 * @return [void]
	 */
	private void tick() {
		
		glfwSetCursorPos(window, Display.getWidth()/2, Display.getHeight()/2);
		player.tick();
		Display.mouseIn.tick();
		//cam.tick();
		
	}
	
	/**
	 * Initializes the game.
	 * @return [void]
	 */
	private void init() {
		
		// Set the GLFW error callback to print to System.err.
		GLFWErrorCallback.createPrint(System.err).set();
		
		// Initialize GLFW.
		if(!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW.");
		
		// OpenGL version 4.0
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
		
		// Core profile.
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		
		// Forward compatibility.
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		
		//glfwWindowHint(GLFW_DOUBLEBUFFER, GL_FALSE);
		
	}
	
	/**
	 * Method used in case of a key being pressed
	 */
	private void registerKeybinds() {
		Display.keyIn.registerKeyDown(GLFW.GLFW_KEY_ESCAPE, ()->{
			glfwSetWindowShouldClose(Display.getWindow(), true);
		});
	}
	
	/**
	 * Cleans up and terminates the game.
	 * @return [void]
	 */
	private void terminate() {
		
		// Terminate GLFW.
		glfwTerminate();
		glfwSetErrorCallback(null).free();
		
	}
	
	/**
	 * Starts the game if not already running.
	 * @return [void]
	 */
	public synchronized void start() {
		
		if(running) 
			return;
			
		mainThread = new Thread(this);
		running = true;
		mainThread.start();
		
	}
	
	/**
	 * Stops the game if not already stopped.
	 * @return [void]
	 */
	public synchronized void close() {
		
		if(!running) 
			return;
		
		try {
			
			join();
			running = false;
			terminate();
			System.exit(0);
			
		} catch (InterruptedException e) { }
		
	}
	
	/**
	 * Returns whether or not the game is currently running.
	 * @return [boolean] True if running, false otherwise.
	 */
	public static boolean isRunning() {
		return running;
	}
	
	public static Player getPlayer(){return player;}
	
	public static Camera getCamera(){return cam;}
	
}
