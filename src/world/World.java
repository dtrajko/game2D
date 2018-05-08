package world;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import collision.AABB;
import entities.Entity;
import entities.Player;
import entities.Transform;
import io.Window;
import render.Animation;
import render.Camera;
import render.Shader;

public class World {
	private final int view_width = 26;
	private final int view_height = 16;
	private byte[] tiles;
	private AABB[] bounding_boxes;
	private List<Entity> entities;
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
			this.entities = new ArrayList<Entity>();
			this.world = new Matrix4f().setTranslation(new Vector3f(0));
			this.world.scale(scale);
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int red = (colorTileSheet[x + y * width] >> 16) & 0xFF;

					Tile tile;
					try {
						tile = Tile.tiles[red];
					} catch (ArrayIndexOutOfBoundsException e) {
						tile = null;
					}
					if (tile != null) {
						setTile(tile, x, y);						
					}
				}
			}

			// TODO finish level loader
			entities.add(new Player(new Transform()));
			entities.add(new Entity(new Animation(1, 1, "tank"), new Transform(new Vector3f(12, -4, 0), new Vector3f(1, 1, 0))) {
				@Override
				public void update(float delta, Window window, Camera camera, World world) {
					move(new Vector2f(0.15f * delta, 0));
				}
			});
			entities.add(new Entity(new Animation(1, 1, "tank"), new Transform(new Vector3f(6, -11, 0), new Vector3f(1, 1, 0))) {
				@Override
				public void update(float delta, Window window, Camera camera, World world) {
					move(new Vector2f(0.15f * delta, 0));
				}
			});

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Matrix4f getWorldMatrix() { return this.world; }

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
		for (Entity entity : entities) {
			entity.render(shader, camera, this);
		}
	}

	public void update(float delta, Window window, Camera camera) {
		for (Entity entity : entities) {
			entity.update(delta, window, camera, this);
		}
		for (int e1 = 0; e1 < entities.size(); e1++) {
			for (int e2 = e1 + 1; e2 < entities.size(); e2++) {
				entities.get(e1).collideWithEntity(entities.get(e2));
			}
			entities.get(e1).collideWithTiles(this);
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
