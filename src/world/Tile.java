package world;

public class Tile {

	public static Tile tiles[] = new Tile[16];
	public static byte not = 0; // number of tiles
	private boolean solid;

	public static final Tile tile_grass = new Tile("grass");
	public static final Tile tile_wall = new Tile("wall").setSolid();

	private byte id;
	private String texture;

	public Tile(String texture) {
		this.id = not;
		not++;
		this.texture = texture;
		this.solid = false;
		if (tiles[id] != null) {
			throw new IllegalStateException("Tile slot [" + id + "] is already being used!");
		}
		tiles[id] = this;
	}
	
	public Tile setSolid() {
		this.solid = true;
		return this;
	}

	public boolean isSolid() { return solid; }
	public int getId() { return id; }
	public String getTexture() { return texture; }
	
}
