package world;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import collision.AABB;
import io.Window;
import render.Camera;
import render.Shader;

public class World {
	private final int view_width = 26;
	private final int view_height = 16;
	private byte[] tiles;
	private AABB[] bounding_boxes;
	private int width;
	private int height;
	private int scale;
	
	private Matrix4f world;

	public World(int width, int height, int scale) {
		this.width = width;   // 16
		this.height = height; // 16
		this.scale = scale;   // 16
		tiles = new byte[width * height];
		bounding_boxes = new AABB[width * height];
		this.world = new Matrix4f().setTranslation(new Vector3f(0));
		this.world.scale(scale);
	}
	
	public World(String worldName, int scale) {
		try {
			BufferedImage tile_sheet = ImageIO.read(new File("./res/levels/" + worldName + "/tiles.png"));
			// BufferedImage entity_sheet = ImageIO.read(new File("./res/levels/" + worldName + "/entity.png"));
			this.width = tile_sheet.getWidth();
			this.height = tile_sheet.getHeight();
			this.scale = scale;
			int[] colorTileSheet = tile_sheet.getRGB(0, 0, width, height, null, 0, width);
			this.tiles = new byte[width * height];
			this.bounding_boxes = new AABB[width * height];
			this.world = new Matrix4f().setTranslation(new Vector3f(0));
			this.world.scale(scale);
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int red = (colorTileSheet[x + y * width] >> 16) & 0xFF;

					/*
					if (red == 255) red = 0;
					System.out.print(red);
					if (x == width - 1) System.out.println();
					*/

					Tile tile;
					try {
						tile = Tile.tiles[red];
					} catch (ArrayIndexOutOfBoundsException e) {
						tile = null;
						// tile = Tile.tiles[0];
					}
					if (tile != null) {
						setTile(tile, x, y);						
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		bounding_boxes[x + y * width] = tile.isSolid() ?
			new AABB(new Vector2f(x * 2, -y * 2), new Vector2f(1, 1)) : null;
	}

	public Tile getTile(int x, int y) {
		try {
			return Tile.tiles[tiles[x + y * width]];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	public AABB getTileBoundingBox(int x, int y) {
		try {
			return bounding_boxes[x + y * width];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
}
