package game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import assets.Assets;
import assets.Cube;
import io.Window;
import render.Camera;
import render.Shader;
import world.TileRenderer;

public class Main {

	public Main() {

		Window.setCallbacks();

		if ( !GLFW.glfwInit()) {
			System.out.println("Unable to initialize GLFW");
			System.exit(-1);
		}

		Window window = new Window();

		GL.createCapabilities();
		GL11.glClearColor(0.2f, 0.3f, 0.8f, 1.0f);
		
		TileRenderer renderer = new TileRenderer();
		Assets.initAsset();

		Shader shader = new Shader("shader");
		Camera camera = new Camera(window.getWidth(), window.getHeight());
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		Game game = new Game(window, camera, shader, renderer);
		game.loop();

		Assets.deleteAsset();
		GLFW.glfwTerminate();
	}

	public static void main(String[] args) {
		new Main();
	}
}
