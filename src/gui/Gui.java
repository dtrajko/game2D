package gui;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import assets.Assets;
import entities.Transform;
import io.Window;
import render.Camera;
import render.Shader;
import render.TileSheet;

public class Gui {

	private Shader shader;
	private Camera camera;
	private TileSheet sheet;

	public Gui(TileSheet sheet, Window window) {
		this.shader = new Shader("gui");
		this.camera = new Camera(window.getWidth(), window.getHeight());
		this.sheet = sheet;
	}

	public Gui(Window window) {
		this.shader = new Shader("gui");
		this.camera = new Camera(window.getWidth(), window.getHeight());
		this.sheet = new TileSheet("test", 3);
	}

	public void resizeCamera(Window window) {
		camera.setProjection(window.getWidth(), window.getHeight());
	}

	public void render(Transform transform, int tileIndex) {
		Matrix4f projectionMatrix = camera.getProjection();
		projectionMatrix.translate(transform.position);
		projectionMatrix.scale(transform.scale);
		shader.bind();
		shader.setUniform("projection", projectionMatrix);
		sheet.bindTile(shader, tileIndex);
		Assets.getModel().render();
	}
}
