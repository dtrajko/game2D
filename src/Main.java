import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Main {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final String TITLE = "Java / LWJGL3 Game";

	public Main() {
		if ( !GLFW.glfwInit()) {
			System.out.println("Unable to initialize GLFW");
			System.exit(-1);
		}

		Window window = new Window(WIDTH, HEIGHT, TITLE);
		
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

		Camera camera = new Camera(window.getWidth(), window.getHeight());

		Texture texture = new Texture("./res/head.png");
		Model model = new Model(vertices, tex_coords, indices);
		Shader shader = new Shader("shader");

		Matrix4f scale = new Matrix4f()
				.translate(new Vector3f(100, 0, 0))
				.scale(0.5f);
		Matrix4f target = new Matrix4f();
		
		camera.setPosition(new Vector3f(-99.5f, 0, 0));

		double frame_cap = 1.0 / 60.0;
		double frame_time = 0;
		int frames = 0;
		double time = Timer.getTime();
		double unprocessed = 0;
		
		while (!window.shouldClose()) {
			
			boolean can_render = false;
			double time_2 = Timer.getTime();
			double passed = time_2 - time;
			unprocessed += passed;
			frame_time += passed;
			time = time_2;
			
			while (unprocessed >= frame_cap) {
				unprocessed -= frame_cap;
				can_render = true;
				target = scale;

				if (window.getInput().isKeyPressed(GLFW.GLFW_KEY_Q)) {
					System.out.println("Key Q pressed!");
				}
				if (window.getInput().isKeyReleased(GLFW.GLFW_KEY_Q)) {
					System.out.println("Key Q released!");
				}
				if (window.getInput().isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
					System.out.println("Mouse button pressed!");
				}
				if (window.getInput().isMouseButtonReleased(GLFW.GLFW_MOUSE_BUTTON_1)) {
					System.out.println("Mouse button released!");
				}

				window.getInput().handle(window.getWindow());
				window.update();
				if (frame_time >= 1.0) {
					frame_time = 0;
					// System.out.println("FPS: " + frames);
					frames = 0;
				}
			}
			
			if (can_render) {

				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				shader.bind();
				shader.setUniform("sampler", 0);
				shader.setUniform("projection", camera.getProjection().mul(target));
				model.render();
				texture.bind(0);
				window.swapBuffers();
				frames++;
			}
		}
		
		GLFW.glfwTerminate();
	}

	public static void main(String[] args) {
		new Main();
	}
}
