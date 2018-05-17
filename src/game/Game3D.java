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

	private Loader loader3D;
	private StaticShader shader3D;
	private Renderer renderer3D;
	private RawModel model3D;
	private Entity3D entity3D;
	private TexturedModel texModel3D;
	private Camera3D camera3D;

	public Game3D() {
		init3D();
	}

	private void init3D() {
		loader3D = new Loader();
		shader3D = new StaticShader();
		renderer3D = new Renderer(shader3D);
		model3D = loader3D.loadToVAO(Cube.getVertices(), Cube.getTexCoords(), Cube.getIndices());
		texModel3D = new TexturedModel(model3D, new ModelTexture(loader3D.loadTexture("grass")));
		entity3D = new Entity3D(texModel3D, new Vector3f(0, 0, -5), 0, 0, 0, 1);
		camera3D = new Camera3D();
	}

	private void render3D() {
		entity3D.increasePosition(0.002f, 0, 0);
		entity3D.increaseRotation(1, 1, 0);
		camera3D.move();
		renderer3D.prepare();
		shader3D.bind();
		shader3D.loadViewMatrix(camera3D);
		renderer3D.render(entity3D, shader3D);
		shader3D.unbind();
	}
	
	public static void onWindowResize() {
		GL11.glViewport(0, 0, Window.getWidth(), Window.getHeight());
	}

	public void loop() {

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
				GL11.glEnable(GL11.GL_DEPTH);
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
				this.render();
				window.swapBuffers();
				Game3D.FPS++;
			}
		}
	}

	public void render() {
		render3D();
	}

	public void cleanUp() {
		shader3D.cleanUp();
		loader3D.cleanUp();
		super.cleanUp();
	}
}
