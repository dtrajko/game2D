package game;

import java.util.HashMap;
import java.util.Map;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import assets.Assets;
import assets.Cube;
import entities.Entity3D;
import entities.Player;
import entities.Transform;
import gui.Gui;
import io.Timer;
import io.Window;
import models.RawModel;
import models.TexturedModel;
import render.Camera2D;
import render.Camera3D;
import render.Loader;
import render.Renderer;
import render.TileSheet;
import shaders.Shader;
import shaders.StaticShader;
import textures.ModelTexture;
import world.TileRenderer;
import world.World;

public class Game3D extends Game {

	private Loader loader;
	private StaticShader shader;
	private Renderer renderer;
	private RawModel model;
	private Entity3D entity;
	private TexturedModel texModel;
	private Camera3D camera;

	public Game3D() {
		init3D();
	}

	private void init3D() {
		loader = new Loader();
		shader = new StaticShader();
		renderer = new Renderer(shader);
		model = loader.loadToVAO(Cube.getVertices(), Cube.getTexCoords(), Cube.getIndices());
		texModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("grass")));
		entity = new Entity3D(texModel, new Vector3f(2, 2, -10), 0, 0, 0, 1);
		camera = new Camera3D();
	}

	private void render3D() {
		entity.increasePosition(0.002f, 0, 0);
		entity.increaseRotation(1, 1, 0);
		camera.move();
		renderer.prepare();
		shader.bind();
		shader.loadViewMatrix(camera);
		renderer.render(entity, shader);
		shader.unbind();
	}
	
	public static void onWindowResize() {
		GL11.glViewport(0, 0, Window.getWidth(), Window.getHeight());
	}

	public void loop() {

		GL.createCapabilities();

		double frame_cap = 1.0 / 120.0;
		double frame_time = 0;
		double time = Timer.getTime();
		double time_2;
		double unprocessed = 0;
		boolean can_render;
		double passed;

		while (!window.shouldClose()) {

 			can_render = false;
			time_2 = Timer.getTime();
			passed = time_2 - time;
			unprocessed += passed;
			frame_time += passed;
			time = time_2;

			while (unprocessed >= frame_cap) {
				if (window.hasResized()) {
					onWindowResize();
				}
				unprocessed -= frame_cap;
				can_render = true;
				Window.getInput().handle(Window.getWindow());
				window.update();

				if (frame_time >= 1.0) {
					frame_time = 0;
					window.setTitle(Window.TITLE + " | FPS: " + FPS);
					FPS = 0;
				}
			}

			if (can_render) {
				this.render();
				window.swapBuffers();
				FPS++;
			}
		}
	}

	public void render() {
		render3D();
	}

	public void cleanUp() {
		shader.cleanUp();
		loader.cleanUp();
		super.cleanUp();
	}
}
