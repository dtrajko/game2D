package game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import assets.Assets;
import io.Window;
import render.Camera;
import render.Shader;
import world.TileRenderer;

public class Main {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final String TITLE = "Java / LWJGL3 Game";
	private static final boolean FULLSCREEN = false;

	public Main() {
		
		Window.setCallbacks();

		if ( !GLFW.glfwInit()) {
			System.out.println("Unable to initialize GLFW");
			System.exit(-1);
		}

		Window window = new Window(WIDTH, HEIGHT, TITLE, FULLSCREEN);

		GL.createCapabilities();
		GL11.glClearColor(0.2f, 0.3f, 0.8f, 1.0f);
		
		TileRenderer renderer = new TileRenderer();
		Assets.initAsset();

		Shader shader = new Shader("shader");
		Camera camera = new Camera(window.getWidth(), window.getHeight());
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		Game game = new Game(TITLE, window, camera, shader, renderer);
		game.run();

		Assets.deleteAsset();
		GLFW.glfwTerminate();
	}

	public static void main(String[] args) {
		new Main();
	}
}
