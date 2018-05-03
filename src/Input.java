import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class Input {

	public static void handle(long window) {
		// Keyboard events
		if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GL11.GL_TRUE) {
			// pass
		}
		if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GL11.GL_TRUE) {
			// pass
		}
		if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GL11.GL_TRUE) {
			// pass
		}
		if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GL11.GL_TRUE) {
			// pass
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
	}
}
