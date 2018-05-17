package io;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import game.Game;

public class Window {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final String TITLE = "Java / LWJGL3 Game";
	public static final boolean FULLSCREEN = false;

	private static long window;
	private static int width, height;
	private String title;
	private static Input input;
	private boolean fullscreen;
	private boolean hasResized;
	private GLFWWindowSizeCallback windowSizeCallback;
	private GLFWVidMode videoMode;

	public Window() {
		setSize(WIDTH, HEIGHT); // default values
		this.title = TITLE;
		setFullscreen(fullscreen);
		createWindow();
		this.hasResized = false;
	}

	public void createWindow() {
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		
		long monitor = fullscreen ? GLFW.glfwGetPrimaryMonitor() : 0;
		window = GLFW.glfwCreateWindow(width, height, this.title, monitor, 0);
		if (window == 0) {
			throw new IllegalStateException("Failed to create window!");
		}
		if (!fullscreen) {
			videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			GLFW.glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
			GLFW.glfwShowWindow(window);
		}
		GLFW.glfwMakeContextCurrent(window);
		input = new Input(window);
		setLocalCallbacks();
	}

	public void setSize(int newWidth, int newHeight) {
		width = newWidth;
		height = newHeight;
	}

	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}

	public void toggleFullscreen() {
		int newWidth, newHeight;
		if (fullscreen) {
			newWidth = WIDTH;
			newHeight = HEIGHT;
			fullscreen = false;
		} else {
			newWidth = videoMode.width();
			newHeight = videoMode.height();
			fullscreen = true;
		}
		long monitor = fullscreen ? GLFW.glfwGetPrimaryMonitor() : 0;
		GLFW.glfwSetWindowMonitor(window, monitor, 0, 0, newWidth, newHeight, 0);
		Game.onWindowResize();
		if (!fullscreen) {
			GLFW.glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
		}
	}

	public void cleanUp() {
		this.windowSizeCallback.close();
		// glfwFreeCallbacks(window);
	}

	public void update() {
		this.hasResized = false;
		input.update();
		GLFW.glfwPollEvents();
	}

	public static void setCallbacks() {
		GLFW.glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
	}
	
	private void setLocalCallbacks() {
		this.windowSizeCallback = new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long argWindow, int argWidth, int argHeight) {
				width = argWidth;
				height = argHeight;
				hasResized = true;
			}
		};
		
		GLFW.glfwSetWindowSizeCallback(window, windowSizeCallback);
	}

	public static long getWindow() {
		return window;
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

	public static int getWidth() { return width; }
	public static int getHeight() { return height; }
	public boolean hasResized() { return this.hasResized; }
	public boolean isFullscreen() { return this.fullscreen; }
	public static Input getInput() { return input; }
}
