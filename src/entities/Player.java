package entities;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import io.Window;
import render.Camera;
import render.Model;
import render.Shader;
import render.Texture;
import world.Sprite;
import world.World;

public class Player {

	private Model model;
	private Texture texture;
	private Transform transform;

	public Player() {
		this.model = new Model(Sprite.getVertices(), Sprite.getTexCoords(), Sprite.getIndices());
		this.texture = new Texture("player");
		this.transform = new Transform();
	}

	public Player(Vector3f position, Vector3f scale) {
		this();
		this.transform.position = position;
		this.transform.scale = scale;
	}

	public void update(float delta, Window window, Camera camera, World world) {
		if (window.getInput().isKeyDown(GLFW.GLFW_KEY_A)) {
			this.transform.position.add(new Vector3f(-delta, 0, 0));
		}
		if (window.getInput().isKeyDown(GLFW.GLFW_KEY_D)) {
			this.transform.position.add(new Vector3f(delta, 0, 0));
		}
		if (window.getInput().isKeyDown(GLFW.GLFW_KEY_W)) {
			this.transform.position.add(new Vector3f(0, delta, 0));
		}
		if (window.getInput().isKeyDown(GLFW.GLFW_KEY_S)) {
			this.transform.position.add(new Vector3f(0, -delta, 0));
		}
		camera.setPosition(this.transform.position.mul(-world.getScale(), new Vector3f()));
		
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

	public void render(Shader shader, Camera camera) {
		shader.bind();
		shader.setUniform("sampler", 0);
		shader.setUniform("projection", this.transform.getProjection(camera.getProjection()));
		texture.bind(0);
		model.render();
	}
}
