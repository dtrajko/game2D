package world;

import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import assets.Sprite;
import render.Camera;
import render.Model;
import render.Shader;
import render.Texture;

public class TileRenderer {
	
	private HashMap<String, Texture> tile_textures;
	private Model model;
	
	public TileRenderer() {

		tile_textures = new HashMap<String, Texture>();

		this.model = new Model(Sprite.getVertices(), Sprite.getTexCoords(), Sprite.getIndices());
		
		for (int i = 0; i < Tile.tiles.length; i++) {
			if (Tile.tiles[i] != null) {
				if (!tile_textures.containsKey(Tile.tiles[i].getTexture())) {
					String texturePath = Tile.tiles[i].getTexture();
					tile_textures.put(texturePath, new Texture(texturePath));
				}
			}
		}
	}

	public void renderTile(Tile tile, int x, int y, Shader shader, Matrix4f world, Camera camera) {
		shader.bind();

		if (tile_textures.containsKey(tile.getTexture())) {
			tile_textures.get(tile.getTexture()).bind(0);
		}
		
		Matrix4f tile_pos = new Matrix4f().translate(new Vector3f(x * 2, y * 2, 0));
		Matrix4f target = new Matrix4f();
		
		camera.getProjection().mul(world, target);
		target.mul(tile_pos);

		shader.setUniform("sampler", 0);
		shader.setUniform("projection", target);

		model.render();
	}
}
