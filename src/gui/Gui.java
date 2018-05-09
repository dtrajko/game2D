package gui;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import assets.Assets;
import io.Window;
import render.Camera;
import render.Shader;
import render.TileSheet;

public class Gui {

	private Shader shader;
	private Camera camera;
	private TileSheet sheet;

	public Gui(Window window) {
		this.shader = new Shader("gui");
		this.camera = new Camera(window.getWidth(), window.getHeight());
		this.sheet = new TileSheet("test", 3);
	}

	public void resizeCamera(Window window) {
		camera.setProjection(window.getWidth(), window.getHeight());
	}

	public void render() {
		Matrix4f projectionMatrix = new Matrix4f();
		camera.getProjection().scale(50, projectionMatrix);
		shader.bind();
		shader.setUniform("projection", projectionMatrix);
		// shader.setUniform("color", new Vector4f(1, 0, 1, 0.5f));
		sheet.bindTile(shader, 0, 0); // 0, 0 - tile location in sheet
		Assets.getModel().render();
	}
}
