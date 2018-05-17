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

public class Game {

	private static int FPS = 0;
	private static int current_level = 1;
	private static int TOTAL_LEVELS = 2;
	private static World level;
	private static Window window;
	private static Camera2D camera2D;
	private int level_scale = 26;
	private static Map<Gui, Transform> guis = new HashMap<Gui, Transform>();
	private static Player player;
	private static boolean switchLevel = true;

	private static Shader shader2D;
	private static TileRenderer renderer2D;
	private static TileSheet sheet;

	private Loader loader3D;
	private StaticShader shader3D;
	private Renderer renderer3D;
	private RawModel model3D;
	private Entity3D entity3D;
	private TexturedModel texModel3D;
	private Camera3D camera3D;

	public Game() {
		Game.window = new Window();
		GL.createCapabilities();
		GL11.glClearColor(0.2f, 0.3f, 0.8f, 1.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		init2D();
		init3D();
	}

	private void init2D() {
		renderer2D = new TileRenderer();
		shader2D = new Shader("shader");
		camera2D = new Camera2D(Window.getWidth(), Window.getHeight());
		sheet = new TileSheet("lives", 3);
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

	public static Camera2D getCamera() {
		return camera2D;
	}

	public static World getLevel() {
		return level;
	}

	public void updateGui() {
		guis.clear();
		int lives_x = -600;
		int lives_y = -320;
		for (int i = 0; i < player.getLives(); i++) {
			guis.put(new Gui(sheet, window), new Transform(new Vector3f(lives_x, lives_y, 0), 20));
			lives_x += 45;
		}
	}

	public void beginLevel() {
		switch (Game.current_level) {
		case 1:
			level = new World("level_1", Game.camera2D, this.level_scale, 5, this);
			level.calculateView();
			break;
		case 2:
			level = new World("level_2", Game.camera2D, this.level_scale, 0, this);
			level.calculateView();
			break;
		default:
			System.err.println("Level index is not correct.");
			break;
		}
	}
	
	public static void onWindowResize() {
		camera2D.setProjection(Window.getWidth(), Window.getHeight());
		level.calculateView();
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

		while (!window.shouldClose() && !gameOver()) {

			if (switchLevel == true) {
				beginLevel();
				switchLevel = false;
			}

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
				update2D((float) frame_cap);
				window.update();

				if (frame_time >= 1.0) {
					frame_time = 0;
					window.setTitle(Window.TITLE + " | FPS: " + FPS);
					FPS = 0;
				}
			}

			if (can_render) {
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
				this.render();
				for (Gui gui : guis.keySet()) {
					gui.render(guis.get(gui), 0);
				}
				window.swapBuffers();
				Game.FPS++;
			}
		}
	}

	private boolean gameOver() {
		if (player instanceof Player) {
			return player.getLives() <= 0;
		}
		return false;
	}

	public void setPlayer(Player player) {
		Game.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setLevel(int level) {
		if (level < 1) level = 1;
		if (level > TOTAL_LEVELS) level = 1; // TOTAL_LEVELS;
		current_level = level;
		switchLevel = true;
	}

	public int getCurrentLevel() {
		return current_level;
	}

	public void update2D(float frame_cap) {
		updateGui();
		level.update(frame_cap * 10, window, camera2D, this);
		level.correctCamera(camera2D);
	}
	
	public void render() {
		// level.render(renderer, shader, camera2D);
		render3D();
	}

	public void cleanUp() {
		Assets.deleteAsset();
		shader3D.cleanUp();
		loader3D.cleanUp();
		GLFW.glfwTerminate();
	}
}
