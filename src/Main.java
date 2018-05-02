import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.joml.*;

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

		float[] vertices = new float[] {
			-0.5f,  0.5f, 0, // TOP LEFT     0
			 0.5f,  0.5f, 0, // TOP RIGHT    1
			 0.5f, -0.5f, 0, // BOTTOM RIGHT 2
			-0.5f, -0.5f, 0, // BOTTOM LEFT  3
		};

		float[] tex_coords = new float[] {
			0, 0,
			1, 0,
			1, 1,
			0, 1,
		};

		int[] indices = new int[] {
			0, 1, 2,
			2, 3, 0
		};

		Texture texture = new Texture("./res/grass.png");
		Model model = new Model(vertices, tex_coords, indices);
		Shader shader = new Shader("shader");

		float x = 0.0f;
		float y = 0.0f;
		
		while (!GLFW.glfwWindowShouldClose(window)) {
			
			// Keyboard events
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

			// Mouse events
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

			shader.bind();

			shader.setUniform1f("sampler", 0);
			texture.bind(0);

			model.render();

			GLFW.glfwSwapBuffers(window);
		}
		
		GLFW.glfwTerminate();
	}

	public static void main(String[] args) {
		new Main();
	}
}
