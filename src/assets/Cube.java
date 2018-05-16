package assets;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import render.Camera;
import render.Model;
import render.Shader;
import render.Texture;

public class Cube {
	
	private Model model;
	private Texture texture;

	private static final float[] vertices = new float[] {
		-0.5f, 0.5f,-0.5f,
		-0.5f,-0.5f,-0.5f,
		 0.5f,-0.5f,-0.5f,
		 0.5f, 0.5f,-0.5f,

		-0.5f, 0.5f, 0.5f,
		-0.5f,-0.5f, 0.5f,
		 0.5f,-0.5f, 0.5f,
		 0.5f, 0.5f, 0.5f,

		 0.5f, 0.5f,-0.5f,
		 0.5f,-0.5f,-0.5f,
		 0.5f,-0.5f, 0.5f,
		 0.5f, 0.5f, 0.5f,

		-0.5f, 0.5f,-0.5f,
		-0.5f,-0.5f,-0.5f,
		-0.5f,-0.5f, 0.5f,
		-0.5f, 0.5f, 0.5f,

		-0.5f, 0.5f, 0.5f,
		-0.5f, 0.5f,-0.5f,
		 0.5f, 0.5f,-0.5f,
		 0.5f, 0.5f, 0.5f,

		-0.5f,-0.5f, 0.5f,
		-0.5f,-0.5f,-0.5f,
		 0.5f,-0.5f,-0.5f,
		 0.5f,-0.5f, 0.5f,
	};

	private static final float[] tex_coords = new float[] {
		0, 0,
		0, 1,
		1, 1,
		1, 0,		
		0, 0,
		0, 1,
		1, 1,
		1, 0,
		0, 0,
		0, 1,
		1, 1,
		1, 0,
		0, 0,
		0, 1,
		1, 1,
		1, 0,
		0, 0,
		0, 1,
		1, 1,
		1, 0,
		0, 0,
		0, 1,
		1, 1,
		1, 0,
	};

	private static final int[] indices = new int[] {
		 0,  1,  3,
		 3,  1,  2,
		 4,  5,  7,
		 7,  5,  6,
		 8,  9, 11,
		11,  9, 10,
		12, 13, 15,
		15, 13, 14,
		16, 17, 19,
		19, 17, 18,
		20, 21, 23,
		23, 21, 22,
	};

	public Cube() {
		model = new Model(getVertices(), getTexCoords(), getIndices());
		texture = new Texture("grass_light");
	}

	public static float[] getVertices() {
		return vertices;
	}

	public static float[] getTexCoords() {
		return tex_coords;
	}

	public static int[] getIndices() {
		return indices;
	}

	public Model getModel() { return model; }

	public void delete() {
		model = null;
	}

	public void render(int x, int y, int z, Shader shader, Matrix4f world, Camera camera) {
		shader.bind();
		
		texture.bind(0);
		
		Matrix4f position = new Matrix4f().translate(new Vector3f(x * 2, y * 2, 0));
		Matrix4f target = new Matrix4f();
		
		camera.getProjection().mul(world, target);
		target.mul(position);

		shader.setUniform("sampler", 0);
		shader.setUniform("projection", target);

		model.render();
	}
}
