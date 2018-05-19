package game;

import org.lwjgl.glfw.GLFW;
import io.Window;

public class Main {

	public Main() {

		Window.setCallbacks();

		if ( !GLFW.glfwInit()) {
			System.out.println("Unable to initialize GLFW");
			System.exit(-1);
		}

		Game game = new Game3D();
		game.loop();
		game.cleanUp();
	}

	public static void main(String[] args) {
		new Main();
	}
}
