package entities;

import org.joml.Matrix4f;
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

public class Entity {

	private static Model model;
	private AABB bounding_box;
	// private Texture texture;
	private Animation texture;
	private Transform transform;

	public Entity(Animation animation, Transform transform) {
		// this.texture = new Texture("player_shadow");
		this.texture = animation;
		this.transform = transform;
		this.bounding_box = new AABB(
			new Vector2f(transform.position.x, transform.position.y), 
			new Vector2f(this.transform.scale.x, this.transform.scale.y));
	}

	public void move(Vector2f direction) {
		transform.position.add(new Vector3f(direction, 0));
		bounding_box.getCenter().set(transform.position.x, transform.position.y);
	}

	public void update(float delta, Window window, Camera camera, World world) {
		AABB[] boxes = new AABB[25];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				boxes[i + j * 5] = world.getTileBoundingBox(
					(int)((transform.position.x / 2 + 0.5f) - 5/2) + i,
					(int)((-transform.position.y / 2 + 0.5f) - 5/2) + j
				);
			}
		}
		AABB box = null;
		for (int i = 0; i < boxes.length; i++) {
			if (boxes[i] != null) {
				if (box == null) {
					box = boxes[i];
				}
				Vector2f length1 = box.getCenter().sub(transform.position.x, transform.position.y, new Vector2f());
				Vector2f length2 = boxes[i].getCenter().sub(transform.position.x, transform.position.y, new Vector2f());
				if (length1.lengthSquared() > length2.lengthSquared()) {
					box = boxes[i];
				}
			}
		}
		if (box != null) {
			Collision data = this.bounding_box.getCollision(box);
			if (data.isIntersecting) {
				bounding_box.correctPosition(box, data);
				transform.position.set(bounding_box.getCenter(), 0);
			}			
		}
		camera.getPosition().lerp(this.transform.position.mul(-world.getScale(), new Vector3f()), 0.02f);
		// camera.setPosition(this.transform.position.mul(-world.getScale(), new Vector3f()));
		correctPosition(window, world);
	}

	public void correctPosition(Window window, World world) {

		Vector3f pos = this.transform.position;

		if (pos.x < 0) {
			pos.x = 0;
		}
		if (pos.x > world.getWidth() * 2 - 2) {
			pos.x = world.getWidth() * 2 - 2;
		}
		if (pos.y > 0) {
			pos.y = 0;
		}
		if (pos.y < -world.getHeight() * 2 + 2) {
			pos.y = -world.getHeight() * 2 + 2;
		}
	}

	public void render(Shader shader, Camera camera, World world) {
		Matrix4f target = camera.getProjection();
		target.mul(world.getWorldMatrix());
		shader.bind();
		shader.setUniform("sampler", 0);
		shader.setUniform("projection", this.transform.getProjection(target));
		texture.bind(0);
		model.render();
	}
	
	public static void initAsset() {
		model = new Model(Sprite.getVertices(), Sprite.getTexCoords(), Sprite.getIndices());
	}

	public static void deleteAsset() {
		model = null;
	}
}
