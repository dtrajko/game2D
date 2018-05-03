import org.joml.Matrix4f;
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

		float[] vertices = new float[] {
				-0.5f,  0.5f, 0, // TOP LEFT     0
				-0.5f, -0.5f, 0, // BOTTOM LEFT  1
				0.5f, -0.5f, 0,  // BOTTOM RIGHT 2
				0.5f,  0.5f, 0,  // TOP RIGHT    3
		};

		float[] tex_coords = new float[] {
			0, 0,
			0, 1,
			1, 1,
			1, 0,
		};

		int[] indices = new int[] {
			0, 1, 3, // top left triangle
			3, 1, 2  // bottom right triangle
		};

		Texture texture = new Texture("./res/head.png");
		Model model = new Model(vertices, tex_coords, indices);
		Shader shader = new Shader("shader");
		Matrix4f projection = new Matrix4f().ortho2D(-WIDTH/2, WIDTH/2, -HEIGHT/2, HEIGHT/2);
		Matrix4f scale = new Matrix4f().scale(200);
		Matrix4f target = new Matrix4f();
		projection.mul(scale, target);
		
		while (!GLFW.glfwWindowShouldClose(window)) {
			
			Input.handle(window);

			GLFW.glfwPollEvents();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

			shader.bind();
			shader.setUniform("sampler", 0);
			shader.setUniform("projection", target);
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
