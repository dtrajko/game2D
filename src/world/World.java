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

import assets.Cube;
import collision.AABB;
import entities.Entity;
import entities.Player;
import entities.Transform;
import game.Game;
import io.Window;
import render.Camera2D;
import shaders.Shader;

public class World {
	private int view_width = 26;
	private int view_height = 16;
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

	public World(String worldName, Camera2D camera, int scale, int bg_tile, Game game) {

		String tileSheetPath = "./res/levels/" + worldName + "/tiles.png";
		String entitySheetPath = "./res/levels/" + worldName + "/entities.png";
		BufferedImage tile_sheet = null;
		BufferedImage entity_sheet = null;

		try {
			tile_sheet = ImageIO.read(new File(tileSheetPath));
		} catch (IOException e) {
			System.out.println("Failed to load file '" + tileSheetPath + "'");
			e.printStackTrace();
		}

		try {
			entity_sheet = ImageIO.read(new File(entitySheetPath));
		} catch (IOException e) {
			System.out.println("Failed to load file '" + tileSheetPath + "'");
			e.printStackTrace();
		}

		this.width = tile_sheet.getWidth();
		this.height = tile_sheet.getHeight();
		this.scale = scale;
		int[] colorTileSheet = tile_sheet.getRGB(0, 0, width, height, null, 0, width);
		int[] colorEntitySheet = entity_sheet.getRGB(0, 0, width, height, null, 0, width);
		this.tiles = new byte[width * height];
		this.bounding_boxes = new AABB[width * height];
		this.entities = new ArrayList<Entity>();
		this.world = new Matrix4f().setTranslation(new Vector3f(0));
		this.world.scale(scale);

		Transform transform;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				
				this.tiles[x + y * width] = (byte) bg_tile;

				int red = (colorTileSheet[x + y * width] >> 16) & 0xFF;
				int entity_index = (colorEntitySheet[x + y * width] >> 16) & 0xFF;
				int entity_alpha = (colorEntitySheet[x + y * width] >> 24) & 0xFF;

				Tile tile;
				try {
					tile = Tile.tiles[red];
				} catch (ArrayIndexOutOfBoundsException e) {
					tile = null;
				}
				if (tile != null) {
					setTile(tile, x, y);						
				}
				
				if (entity_alpha > 0) {
					transform = new Transform();
					transform.position.x = x * 2;
					transform.position.y = -y * 2;
					switch (entity_index) {
						case 1:
							Player player = new Player(transform);
							game.setPlayer(player);
							entities.add(player);
							camera.getPosition().set(transform.position.mul(-scale, new Vector3f()));
							break;
						default:
							break;
					}
				}
			}
		}
	}

	public void calculateView() {
		this.view_width = Window.getWidth() / (scale * 2) + 2;
		this.view_height = Window.getHeight() / (scale * 2) + 4;
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

	public void render(TileRenderer renderer, Shader shader, Camera2D camera) {
		int posX = (int)camera.getPosition().x / (scale * 2);
		int posY = (int)camera.getPosition().y / (scale * 2);
		for (int i = 0; i < view_width; i++) {
			for (int j = 0; j < view_height; j++) {
				Tile tile = getTile(i - posX - (this.view_width / 2) + 1, j + posY - (this.view_height / 2));
				if (tile != null) {
					renderer.renderTile(tile, i - posX - (this.view_width / 2) + 1, -j - posY + (this.view_height / 2), shader, world, camera);
				}
			}
		}
		for (Entity entity : entities) {
			entity.render(shader, camera, this);
		}
	}

	public void update(float delta, Window window, Camera2D camera, Game game) {
		for (Entity entity : entities) {
			entity.update(delta, window, camera, this, game);
		}
		for (int e1 = 0; e1 < entities.size(); e1++) {
			for (int e2 = e1 + 1; e2 < entities.size(); e2++) {
				entities.get(e1).collideWithEntity(entities.get(e2));
			}
			entities.get(e1).collideWithTiles(this);
		}
	}

	public void correctCamera(Camera2D camera) {

		Vector3f pos = camera.getPosition();

		int w = -width * scale * 2;
		int h = height * scale * 2;

		if (pos.x > -(Window.getWidth() / 2) + scale) {
			pos.x = -(Window.getWidth() / 2) + scale;
		}
		if (pos.x < w + (Window.getWidth() / 2) + scale) {
			pos.x = w + (Window.getWidth() / 2) + scale;
		}
		if (pos.y < (Window.getHeight() / 2) - scale) {
			pos.y = (Window.getHeight() / 2) - scale;
		}
		if (pos.y > h - (Window.getHeight() / 2) - scale) {
			pos.y = h - (Window.getHeight() / 2) - scale;
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
