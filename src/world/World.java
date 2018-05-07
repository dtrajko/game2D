package world;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import io.Window;
import render.Camera;
import render.Shader;

public class World {
	private final int view_width = 26;
	private final int view_height = 16;
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

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getScale() {
		return scale;
	}

	public void render(TileRenderer renderer, Shader shader, Camera camera, Window window) {
		/*
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				renderer.renderTile(tiles[j + i * width], j, -i, shader, world, camera);
			}
		}
		*/
		int posX = ((int) camera.getPosition().x + (window.getWidth() / 2)) / (scale * 2);
		int posY = ((int) camera.getPosition().y - (window.getHeight() / 2)) / (scale * 2);
		for (int i = 0; i < view_width; i++) {
			for (int j = 0; j < view_height; j++) {
				Tile tile = getTile(i - posX, j + posY);
				if (tile != null) {
					renderer.renderTile(tile, i - posX, -j - posY, shader, world, camera);
				}
			}
		}
	}

	public void correctCamera(Camera camera, Window window) {

		Vector3f pos = camera.getPosition();

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
	
	public Tile getTile(int x, int y) {
		try {
			return Tile.tiles[tiles[x + y * width]];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

}
