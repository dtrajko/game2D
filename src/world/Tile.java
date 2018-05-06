package world;

public class Tile {

	public static Tile tiles[] = new Tile[16];
	public static byte not = 0; // number of tiles

	public static final Tile test_tile = new Tile("wall");

	private byte id;
	private String texture;

	public Tile(String texture) {
		this.id = not;
		not++;
		this.texture = texture;
		if (tiles[id] != null) {
			throw new IllegalStateException("Tile slot [" + id + "] is already being used!");
		}
		tiles[id] = this;
	}

	public int getId() {
		return id;
	}

	public String getTexture() {
		return texture;
	}
	
}
