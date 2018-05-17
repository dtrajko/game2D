package entities;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import game.Game;
import io.Window;
import render.Animation;
import render.Camera2D;
import world.Tile;
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
	private static int lives = 5;

	public Player() {
		this(new Transform());
	}

	public Player(Transform transform) {
		super(ANIM_SIZE, transform);
		this.setAnimation(ANIM_IDLE, new Animation(4, 10, "player/idle"));
		this.setAnimation(ANIM_WALK, new Animation(4, 10, "player/walking"));
		previous_height = this.transform.position.y;		
	}

	@Override
	public void update(float delta, Window window, Camera2D camera, World world, Game game) {

		this.useAnimation(ANIM_IDLE);
		Vector2f movement = new Vector2f();

		movement.add(0, -GRAVITY);
		
		if (this.transform.position.y >= previous_height) {
			jump_allowed = true;
		} else {
			jump_allowed = false;			
		}
		previous_height = this.transform.position.y;

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
		if (window.getInput().isKeyDown(GLFW.GLFW_KEY_SPACE) && jump_allowed) {
			subsequent_jumps++;
			movement.add(0, delta * (JUMP_FORCE / (subsequent_jumps * (GRAVITY * 2))));
			this.useAnimation(ANIM_WALK);
		}
		if (window.getInput().isKeyReleased(GLFW.GLFW_KEY_SPACE)) {
			subsequent_jumps = 0;
		}
		if (window.getInput().isKeyReleased(GLFW.GLFW_KEY_F) || window.getInput().isKeyPressed(GLFW.GLFW_KEY_ENTER)) {
			window.toggleFullscreen();
		}
		if (window.getInput().isKeyReleased(GLFW.GLFW_KEY_1)) {
			game.setLevel(1);
		}
		if (window.getInput().isKeyReleased(GLFW.GLFW_KEY_2)) {
			game.setLevel(2);
		}

		move(movement);
		collideWithTiles(world);
		correctPosition(window, world);
		camera.getPosition().lerp(this.transform.position.mul(-world.getScale(), new Vector3f()), 0.02f);	
		manageLives(game, world);
		manageLevels(game, world);
	}

	public void manageLevels(Game game, World world) {
		if (isNextLevel(world)) {
			game.setLevel(game.getCurrentLevel() + 1);
		} else if (isPreviousLevel(world)) {
			// game.setLevel(game.getCurrentLevel() - 1);
		}
	}

	public Tile getCurrentTile(World world) {
		int x = (int)(transform.position.x / 2);
		int y = (int)(-transform.position.y / 2);
		Tile tile = world.getTile(x, y);
		return tile;
	}

	public boolean isNextLevel(World world) {
		return getCurrentTile(world).isNextLevel();
	}

	public boolean isPreviousLevel(World world) {
		return getCurrentTile(world).isPreviousLevel();
	}

	public void manageLives(Game game, World world) {
		if (previous_height == this.transform.position.y) {
			return;
		}
		int y = (int)(-transform.position.y / 2);
		if (y >= world.getHeight() - 1) {
			lives--;
			if (lives < 0) lives = 0;
			game.setLevel(game.getCurrentLevel());
		}
	}

	public int getLives() {
		return lives;
	}
}
