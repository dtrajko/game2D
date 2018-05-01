package game2D;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Main {
	
	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;
	
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

		while (!GLFW.glfwWindowShouldClose(window)) {
			GLFW.glfwPollEvents();			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			GL11.glBegin(GL11.GL_QUADS);
			
			GL11.glColor4f(1, 0, 0, 1);
			GL11.glVertex2f(-0.5f,  0.5f);
			
			GL11.glColor4f(0, 1, 0, 1);
			GL11.glVertex2f( 0.5f,  0.5f);
			
			GL11.glColor4f(0, 0, 1, 1);
			GL11.glVertex2f( 0.5f, -0.5f);
			
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glVertex2f(-0.5f, -0.5f);
			
			GL11.glEnd();

			GLFW.glfwSwapBuffers(window);
		}
		
		GLFW.glfwTerminate();
	}

	public static void main(String[] args) {
		new Main();
	}
}
