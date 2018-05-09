package entities;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import io.Window;
import render.Animation;
import render.Camera;
import world.World;

public class Player extends Entity {
	
	public static final int ANIM_IDLE = 0;
	public static final int ANIM_WALK = 1;
	public static final int ANIM_SIZE = 2;
	
	private static final float GRAVITY = 0.08f;
	private static final float JUMP_FORCE = 3f;
	
	private static boolean jump_allowed;
	private static float previous_height;
	private static int subsequent_jumps = 0;

	public Player() {
		this(new Transform());
	}

	public Player(Transform transform) {
		super(ANIM_SIZE, transform);
		this.setAnimation(ANIM_IDLE, new Animation(4, 10, "player/idle"));
		this.setAnimation(ANIM_WALK, new Animation(4, 10, "player/walking"));
		this.previous_height = this.transform.position.y;		
	}

	@Override
	public void update(float delta, Window window, Camera camera, World world) {

		this.useAnimation(ANIM_IDLE);
		Vector2f movement = new Vector2f();

		movement.add(0, -GRAVITY);
		
		if (this.transform.position.y >= this.previous_height) {
			this.jump_allowed = true;
		} else {
			this.jump_allowed = false;			
		}
		this.previous_height = this.transform.position.y;

		if (window.getInput().isKeyDown(GLFW.GLFW_KEY_A) || window.getInput().isKeyDown(GLFW.GLFW_KEY_LEFT)) {
			movement.add(-delta, 0);
			this.useAnimation(ANIM_WALK);
		}
		if (window.getInput().isKeyDown(GLFW.GLFW_KEY_D) || window.getInput().isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
			movement.add(delta, 0);
			this.useAnimation(ANIM_WALK);
		}
		if (window.getInput().isKeyDown(GLFW.GLFW_KEY_W)) {
			// movement.add(0, delta);
			// this.useAnimation(ANIM_WALK);
		}
		if (window.getInput().isKeyDown(GLFW.GLFW_KEY_S)) {
			// movement.add(0, -delta);
			// this.useAnimation(ANIM_WALK);
		}
		if (window.getInput().isKeyDown(GLFW.GLFW_KEY_SPACE) && this.jump_allowed) {
			this.subsequent_jumps++;
			movement.add(0, delta * (JUMP_FORCE / (this.subsequent_jumps * (GRAVITY * 2))));
			this.useAnimation(ANIM_WALK);
		}
		if (window.getInput().isKeyReleased(GLFW.GLFW_KEY_SPACE)) {
			this.subsequent_jumps = 0;
		}
		if (window.getInput().isKeyPressed(GLFW.GLFW_KEY_F) || window.getInput().isKeyPressed(GLFW.GLFW_KEY_ENTER)) {
			window.toggleFullscreen();
		}

		move(movement);
		collideWithTiles(world);
		correctPosition(window, world);
		camera.getPosition().lerp(this.transform.position.mul(-world.getScale(), new Vector3f()), 0.02f);
	}
}
