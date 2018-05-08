package io;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

public class Window {

	private long window;
	private int width, height;
	private boolean fullscreen = false;
	private String title;
	private Input input;
	
	public Window(int width, int height, String title, boolean fullscreen) {
		setSize(width, height); // default values
		this.title = title;
		setFullscreen(fullscreen);
		createWindow();
	}

	public void createWindow() {
		// GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		window = GLFW.glfwCreateWindow(width, height, this.title, 
			fullscreen ? GLFW.glfwGetPrimaryMonitor() : 0, 0);
		if (window == 0) {
			throw new IllegalStateException("Failed to create window!");
		}

		if (!fullscreen) {
			GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			GLFW.glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
			GLFW.glfwShowWindow(window);
		}

		GLFW.glfwMakeContextCurrent(window);
		
		this.input = new Input(this.window);
	}
	
	public void update() {
		input.update();
		GLFW.glfwPollEvents();
	}

	public static void setCallbacks() {
		GLFW.glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
	}

	public long getWindow() {
		return this.window;
	}
	
	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(window);
	}
	
	public void swapBuffers() {
		GLFW.glfwSwapBuffers(window);
	}

	public void setTitle(String title) {
		GLFW.glfwSetWindowTitle(window, title);
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}

	public int getWidth() { return this.width; }
	public int getHeight() { return this.height; }
	public boolean isFullscreen() { return this.fullscreen; }
	public Input getInput() { return this.input; }
}
