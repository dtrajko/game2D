package render;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import io.Window;

public class Camera3D {

	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch;
	private float yaw;
	private float roll;
	
	public Camera3D() {}

	public void move() {
		if (Window.getInput().isKeyDown(GLFW.GLFW_KEY_D)) {
			position.x -= 0.01f;
		}
		if (Window.getInput().isKeyDown(GLFW.GLFW_KEY_A)) {
			position.x += 0.01f;
		}
		if (Window.getInput().isKeyDown(GLFW.GLFW_KEY_W)) {
			position.y -= 0.01f;
		}
		if (Window.getInput().isKeyDown(GLFW.GLFW_KEY_S)) {
			position.y += 0.01f;
		}
		if (Window.getInput().isKeyDown(GLFW.GLFW_KEY_UP)) {
			position.z -= 0.02f;
		}
		if (Window.getInput().isKeyDown(GLFW.GLFW_KEY_DOWN)) {
			position.z += 0.02f;
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}	
}