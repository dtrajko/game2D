package game;

import java.util.HashMap;
import java.util.Map;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import entities.Player;
import entities.Transform;
import gui.Gui;
import io.Timer;
import io.Window;
import render.Camera;
import render.Shader;
import render.TileSheet;
import world.TileRenderer;
import world.World;

public class Game {

	private static String title;
	private static int FPS = 0;
	private static int current_level = 1;
	private static int TOTAL_LEVELS = 2;
	private static World level;
	private static Window window;
	private static Camera camera;
	private static Shader shader;
	private static TileRenderer renderer;
	private int level_scale = 26;
	private static Map<Gui, Transform> guis = new HashMap<Gui, Transform>();
	private static Player player;
	private static boolean switchLevel = true;

	public Game(String title, Window window, Camera camera, Shader shader, TileRenderer renderer) {
		Game.window = window;
		Game.camera = camera;
		Game.shader = shader;
		Game.renderer = renderer;
		Game.title = title;
	}
	
	public void initGui() {
		TileSheet sheet = new TileSheet("lives", 3);	
		guis.put(new Gui(sheet, window), new Transform(new Vector3f(-590, -320, 0), 30));
		guis.put(new Gui(sheet, window), new Transform(new Vector3f(-530, -320, 0), 30));
		guis.put(new Gui(sheet, window), new Transform(new Vector3f(-470, -320, 0), 30));
	}

	public void beginLevel() {
		switch (Game.current_level) {
		case 1:
			level = new World("level_1", Game.camera, this.level_scale, 5, this);
			level.calculateView(window);
			break;
		case 2:
			level = new World("level_2", Game.camera, this.level_scale, 0, this);
			level.calculateView(window);
			break;
		default:
			System.err.println("Level index is not correct.");
			break;
		}
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

			if (switchLevel == true) {
				initGui();
				beginLevel();
				switchLevel = false;
			}
			
 			can_render = false;
			time_2 = Timer.getTime();
			passed = time_2 - time;
			unprocessed += passed;
			frame_time += passed;
			time = time_2;
			// System.out.println("WHILE loop SECTION 3");

			while (unprocessed >= frame_cap) {
				if (window.hasResized()) {
					camera.setProjection(window.getWidth(), window.getHeight());
					// life.resizeCamera(window);
					level.calculateView(window);
					GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
				}
				unprocessed -= frame_cap;
				can_render = true;
				window.getInput().handle(window.getWindow());
				this.update((float) frame_cap);
				window.update();

				if (frame_time >= 1.0) {
					frame_time = 0;
					// System.out.println("FPS: " + Main.FPS);
					window.setTitle(title + " | FPS: " + FPS);
					FPS = 0;
				}
			}

			if (can_render) {

				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				this.render();
				for (Gui gui : guis.keySet()) {
					gui.render(guis.get(gui), 0);
				}
				window.swapBuffers();
				Game.FPS++;
			}
			
			// System.out.println("WHILE loop SECTION 6");
		}
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
		if (current_level != level) {
			current_level = level;
			this.switchLevel = true;			
		}
	}

	public int getCurrentLevel() {
		return current_level;
	}

	public World getLevel() {
		return level;
	}

	public void update(float frame_cap) {
		level.update(frame_cap * 10, window, camera, this);
		level.correctCamera(window, camera);
	}
	
	public void render() {
		level.render(renderer, shader, camera);
	}
}
