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

	private String title;
	private int FPS = 0;
	private int current_level = 1;
	private World level;
	private Window window;
	private Camera camera;
	private Shader shader;
	private TileRenderer renderer;
	private int level_scale = 26;
	private Map<Gui, Transform> guis = new HashMap<Gui, Transform>();
	private Player player;

	public Game(String title, Window window, Camera camera, Shader shader, TileRenderer renderer) {
		this.window = window;
		this.camera = camera;
		this.shader = shader;
		this.renderer = renderer;
		this.title = title;
	}
	
	public void initGui() {
		TileSheet sheet = new TileSheet("lives", 3);	
		guis.put(new Gui(sheet, window), new Transform(new Vector3f(-590, -320, 0), 30));
		guis.put(new Gui(sheet, window), new Transform(new Vector3f(-530, -320, 0), 30));
		guis.put(new Gui(sheet, window), new Transform(new Vector3f(-470, -320, 0), 30));
	}
	
	public void run() {
		initGui();
		beginLevel();
		loop();
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setLevel(int level) {
		this.current_level = level;
	}

	public void beginLevel() {
		switch (this.current_level) {
		case 1:
			this.level = new World("level_1", this.camera, this.level_scale, 5, this);
			this.level.calculateView(this.window);
			break;
		case 2:
			this.level = new World("level_2", this.camera, this.level_scale, 0, this);
			this.level.calculateView(this.window);
			break;
		default:
			System.err.println("Level index is not correct.");
			break;
		}
	}

	public World getLevel() {
		return level;
	}

	public void update(float frame_cap) {
		this.level.update(frame_cap * 10, this.window, this.camera, this);
		this.level.correctCamera(this.window, this.camera);
	}
	
	public void render() {
		this.level.render(renderer, shader, this.camera);
	}

	public void loop() {
		
		double frame_cap = 1.0 / 120.0;
		double frame_time = 0;
		double time = Timer.getTime();
		double unprocessed = 0;
		
		while (!window.shouldClose()) {
			
			boolean can_render = false;
			double time_2 = Timer.getTime();
			double passed = time_2 - time;
			unprocessed += passed;
			frame_time += passed;
			time = time_2;
			
			while (unprocessed >= frame_cap) {
				if (window.hasResized()) {
					camera.setProjection(window.getWidth(), window.getHeight());
					// life.resizeCamera(window);
					this.level.calculateView(window);
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
					this.window.setTitle(this.title + " | FPS: " + this.FPS);
					this.FPS = 0;
				}
			}

			if (can_render) {

				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				this.render();
				for (Gui gui : guis.keySet()) {
					gui.render(guis.get(gui), 0);
				}
				window.swapBuffers();
				this.FPS++;
			}
		}
	}
}
