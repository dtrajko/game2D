package game;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import io.Timer;
import io.Window;
import render.Camera;
import render.Model;
import render.Shader;
import render.Texture;
import world.Tile;
import world.TileRenderer;
import world.World;

public class Main {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final String TITLE = "Java / LWJGL3 Game";
	private static final boolean FULLSCREEN = false;

	public Main() {
		if ( !GLFW.glfwInit()) {
			System.out.println("Unable to initialize GLFW");
			System.exit(-1);
		}

		Window window = new Window(WIDTH, HEIGHT, TITLE, FULLSCREEN);
		
		GL.createCapabilities();
		GL11.glClearColor(0.2f, 0.3f, 0.8f, 1.0f);
		
		TileRenderer tileRenderer = new TileRenderer();
		Camera camera = new Camera(window.getWidth(), window.getHeight());
		Shader shader = new Shader("shader");

		World world = new World(32, 24, 26);
		world.setTile(Tile.wall_tile, 0, 0);
		world.setTile(Tile.wall_tile, 0, 23);
		world.setTile(Tile.wall_tile, 31, 0);
		world.setTile(Tile.wall_tile, 31, 23);

		GL11.glEnable(GL11.GL_TEXTURE_2D);

		double frame_cap = 1.0 / 120.0;
		double frame_time = 0;
		int frames = 0;
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
				unprocessed -= frame_cap;
				can_render = true;

				if (window.getInput().isKeyDown(GLFW.GLFW_KEY_A)) {
					camera.getPosition().sub(new Vector3f(-2f, 0, 0));
				}
				if (window.getInput().isKeyDown(GLFW.GLFW_KEY_D)) {
					camera.getPosition().sub(new Vector3f(2f, 0, 0));
				}
				if (window.getInput().isKeyDown(GLFW.GLFW_KEY_W)) {
					camera.getPosition().sub(new Vector3f(0, 2f, 0));
				}
				if (window.getInput().isKeyDown(GLFW.GLFW_KEY_S)) {
					camera.getPosition().sub(new Vector3f(0, -2f, 0));
				}

				window.getInput().handle(window.getWindow());
				world.correctCamera(camera, window);
				window.update();
				if (frame_time >= 1.0) {
					frame_time = 0;
					// System.out.println("FPS: " + frames);
					frames = 0;
				}
			}
			
			if (can_render) {

				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

				world.render(tileRenderer, shader, camera, window);

				window.swapBuffers();
				frames++;
			}
		}
		
		GLFW.glfwTerminate();
	}

	public static void main(String[] args) {
		new Main();
	}
}
