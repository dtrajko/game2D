package world;

public class Tile {

	public static Tile tiles[] = new Tile[16]; // range of RGB color channels
	public static byte not = 0; // number of tiles
	private boolean solid;

	public static final Tile tile_00 = new Tile("brick_wall");
	public static final Tile tile_01 = new Tile("stone").setSolid();
	public static final Tile tile_02 = new Tile("wall").setSolid();
	public static final Tile tile_03 = new Tile("lava");
	public static final Tile tile_04 = new Tile("door");

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
	public byte getId() { return id; }
	public String getTexture() { return texture; }
	
}
