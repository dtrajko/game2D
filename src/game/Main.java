package game;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import assets.Assets;
import entities.Entity;
import entities.Transform;
import gui.Gui;
import io.Timer;
import io.Window;
import render.Camera;
import render.Shader;
import render.TileSheet;
import world.TileRenderer;
import world.World;

public class Main {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final String TITLE = "Java / LWJGL3 Game";
	private static final boolean FULLSCREEN = false;
	private static int FPS = 0;

	public Main() {
		
		Window.setCallbacks();

		if ( !GLFW.glfwInit()) {
			System.out.println("Unable to initialize GLFW");
			System.exit(-1);
		}

		Window window = new Window(WIDTH, HEIGHT, TITLE, FULLSCREEN);
		
		GL.createCapabilities();
		GL11.glClearColor(0.2f, 0.3f, 0.8f, 1.0f);
		
		TileRenderer tileRenderer = new TileRenderer();
		Assets.initAsset();

		Shader shader = new Shader("shader");
		Camera camera = new Camera(window.getWidth(), window.getHeight());
		World world = new World("level_01", camera, 26);
		world.calculateView(window);
		
		TileSheet sheet = new TileSheet("lives", 3);
		Gui gui = new Gui(sheet, window);
		Transform heart_01_pos = new Transform(new Vector3f(-590, -320, 0), 30);
		Transform heart_02_pos = new Transform(new Vector3f(-530, -320, 0), 30);
		Transform heart_03_pos = new Transform(new Vector3f(-470, -320, 0), 30);

		GL11.glEnable(GL11.GL_TEXTURE_2D);

		double frame_cap = 1.0 / 120.0;
		double frame_time = 0;
		double time = Timer.getTime();
		double unprocessed = 0;
		
		while (!window.shouldClose()) {
			
			boolean can_render = false;
			double time_2 = Timer.getTime();
			double passed = time_2 - time;
			unprocessed += passed;
			frame_time += passed;
			time = time_2;
			
			while (unprocessed >= frame_cap) {
				if (window.hasResized()) {
					camera.setProjection(window.getWidth(), window.getHeight());
					// life.resizeCamera(window);
					world.calculateView(window);
					GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
				}
				unprocessed -= frame_cap;
				can_render = true;
				window.getInput().handle(window.getWindow());
				world.update((float)frame_cap * 10, window, camera);
				world.correctCamera(camera, window);
				window.update();

				if (frame_time >= 1.0) {
					frame_time = 0;
					// System.out.println("FPS: " + Main.FPS);
					window.setTitle(TITLE + " | FPS: " + Main.FPS);
					Main.FPS = 0;
				}
			}

			if (can_render) {

				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

				world.render(tileRenderer, shader, camera);
				gui.render(heart_01_pos, 0);
				gui.render(heart_02_pos, 0);
				gui.render(heart_03_pos, 0);

				window.swapBuffers();
				Main.FPS++;
			}
		}

		Assets.deleteAsset();
		GLFW.glfwTerminate();
	}

	public static void main(String[] args) {
		new Main();
	}
}
