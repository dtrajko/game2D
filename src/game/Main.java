package game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import assets.Assets;
import assets.Cube;
import io.Window;
import render.Camera2D;
import shaders.Shader;
import world.TileRenderer;

public class Main {

	public Main() {

		Window.setCallbacks();

		if ( !GLFW.glfwInit()) {
			System.out.println("Unable to initialize GLFW");
			System.exit(-1);
		}

		Game game = new Game();
		game.loop();
		game.cleanUp();
	}

	public static void main(String[] args) {
		new Main();
	}
}
