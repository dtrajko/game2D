package game2D;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Main {
	
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	
	public Main() {
		if ( !GLFW.glfwInit()) {
			System.out.println("Unable to initialize GLFW");
			System.exit(-1);
		}
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		long window = GLFW.glfwCreateWindow(WIDTH, HEIGHT, "The 2D game based on Java and LWJGL3", 0, 0);
		if (window == 0) {
			throw new IllegalStateException("Failed to create window!");
		}
		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(window, (videoMode.width() - WIDTH) / 2, (videoMode.height() - HEIGHT) / 2);
		GLFW.glfwShowWindow(window);
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();
		GL11.glClearColor(0.2f, 0.3f, 0.8f, 1.0f);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		Texture texture = new Texture("./res/grass.png");

		float x = 0.0f;
		float y = 0.0f;
		
		while (!GLFW.glfwWindowShouldClose(window)) {
			
			if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GL11.GL_TRUE) {
				x -= 0.0005f;
			}
			if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GL11.GL_TRUE) {
				x += 0.0005f;
			}
			if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GL11.GL_TRUE) {
				y += 0.0005f;
			}
			if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GL11.GL_TRUE) {
				y -= 0.0005f;
			}
			if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GL11.GL_TRUE) {
				GLFW.glfwSetWindowShouldClose(window, true);
			}

			if (GLFW.glfwGetMouseButton(window, 0) == GL11.GL_TRUE) {
				System.out.println("Left mouse button clicked.");
			}
			if (GLFW.glfwGetMouseButton(window, 1) == GL11.GL_TRUE) {
				System.out.println("Right mouse button clicked.");
			}
			if (GLFW.glfwGetMouseButton(window, 2) == GL11.GL_TRUE) {
				System.out.println("Middle mouse button clicked.");
			}

			GLFW.glfwPollEvents();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			texture.bind();
			GL11.glBegin(GL11.GL_QUADS);
			// GL11.glColor4f(1, 0, 0, 1);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(-0.1f + x,  0.15f + y);
			// GL11.glColor4f(0, 1, 0, 1);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex2f( 0.1f + x,  0.15f + y);
			// GL11.glColor4f(0, 0, 1, 1);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex2f( 0.1f + x, -0.15f + y);
			// GL11.glColor4f(1, 1, 1, 1);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex2f(-0.1f + x, -0.15f + y);
			GL11.glEnd();

			GLFW.glfwSwapBuffers(window);
		}
		
		GLFW.glfwTerminate();
	}

	public static void main(String[] args) {
		new Main();
	}
}