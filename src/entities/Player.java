package entities;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import collision.AABB;
import collision.Collision;
import io.Window;
import render.Animation;
import render.Camera;
import render.Model;
import render.Shader;
import render.Texture;
import world.Sprite;
import world.Tile;
import world.World;

public class Player extends Entity {

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
		super.update(delta, window, camera, world);
	}
}
