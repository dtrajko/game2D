package entities;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import io.Window;
import render.Animation;
import render.Camera;
import world.World;

public class Player extends Entity {

	public Player() {
		this(new Transform());
	}

	public Player(Transform transform) {
		super(new Animation(4, 4, "vulin"), transform);
	}

	@Override
	public void update(float delta, Window window, Camera camera, World world) {
		Vector2f movement = new Vector2f();
		if (window.getInput().isKeyDown(GLFW.GLFW_KEY_A)) {
			movement.add(-delta, 0);
		}
		if (window.getInput().isKeyDown(GLFW.GLFW_KEY_D)) {
			movement.add(delta, 0);
		}
		if (window.getInput().isKeyDown(GLFW.GLFW_KEY_W)) {
			movement.add(0, delta);
		}
		if (window.getInput().isKeyDown(GLFW.GLFW_KEY_S)) {
			movement.add(0, -delta);
		}

		move(movement);
		collideWithTiles(world);
		correctPosition(window, world);
		camera.getPosition().lerp(this.transform.position.mul(-world.getScale(), new Vector3f()), 0.02f);
	}
}
