package game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import io.Timer;
import io.Window;

public abstract class Game {

	protected static int FPS = 0;
	protected static Window window;

	public Game() {
		window = new Window();
		GL.createCapabilities();
		GL11.glClearColor(0.2f, 0.3f, 0.8f, 1.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void onWindowResize() {
		GL11.glViewport(0, 0, Window.getWidth(), Window.getHeight());
	}

	public void loop() {

		double frame_cap = 1.0 / 120.0;
		double frame_time = 0;
		double time = Timer.getTime();
		double time_2;
		double unprocessed = 0;
		boolean can_render;
		double passed;

		while (!window.shouldClose()) {

 			can_render = false;
			time_2 = Timer.getTime();
			passed = time_2 - time;
			unprocessed += passed;
			frame_time += passed;
			time = time_2;

			while (unprocessed >= frame_cap) {
				if (window.hasResized()) {
					onWindowResize();
				}
				unprocessed -= frame_cap;
				can_render = true;
				Window.getInput().handle(Window.getWindow());
				window.update();

				if (frame_time >= 1.0) {
					frame_time = 0;
					window.setTitle(Window.TITLE + " | FPS: " + FPS);
					FPS = 0;
				}
			}

			if (can_render) {
				GL11.glEnable(GL11.GL_DEPTH);
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
				this.render();
				window.swapBuffers();
				Game.FPS++;
			}
		}
	}
	
	public abstract void render();

	public void cleanUp() {
		GLFW.glfwTerminate();
	}
}
