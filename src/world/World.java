package world;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import io.Window;
import render.Camera;
import render.Shader;

public class World {
	
	private int[] tiles;
	
	private int width;
	private int height;
	private int scale;
	
	private Matrix4f world;

	public World(int width, int height, int scale) {
		this.width = width;   // 16
		this.height = height; // 16
		this.scale = scale; // 16
		tiles = new int[width * height];
		world = new Matrix4f().setTranslation(new Vector3f(0));
		world.scale(scale);
	}

	public void setMatrix(Matrix4f matrix) {
		this.world = matrix;
	}

	public void render(TileRenderer renderer, Shader shader, Camera camera) {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				renderer.renderTile(tiles[j + i * width], j, -i, shader, world, camera);
			}
		}
	}
	
	public void correctCamera(Camera camera, Window window) {

		Vector3f pos = camera.getPosition();
		
		// System.out.println("Cam pos: " + "X=" + pos.x + " Y=" + pos.y + " Z=" + pos.z);
		// System.out.println("Width: " + (int)width + " Height: " + (int)height + " Scale: " + (int)scale);

		int w = -width * scale * 2;
		int h = height * scale * 2;

		if (pos.x > -(window.getWidth() / 2) + scale) {
			pos.x = -(window.getWidth() / 2) + scale;
		}
		if (pos.x < w + (window.getWidth() / 2) + scale) {
			pos.x = w + (window.getWidth() / 2) + scale;
		}
		if (pos.y < (window.getHeight() / 2) - scale) {
			pos.y = (window.getHeight() / 2) - scale;
		}
		if (pos.y > h - (window.getHeight() / 2) - scale) {
			pos.y = h - (window.getHeight() / 2) - scale;
		}
	}

	public void setTile(Tile tile, int x, int y) {
		tiles[x + y * width] = tile.getId();
	}

}
