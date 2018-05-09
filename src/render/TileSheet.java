package render;

import org.joml.Matrix4f;

public class TileSheet {
	
	private Texture texture;
	private Matrix4f scale;
	private Matrix4f translation;

	public TileSheet(String textureName, int amountOfTiles) {
		this.texture = new Texture("sheets/" + textureName);
		this.scale = new Matrix4f().scale(1.0f / (float)amountOfTiles);
		this.translation = new Matrix4f();
	}
	
	public void bindTile(Shader shader, int x, int y) {
		this.scale.translate(x, y, 0, translation);
		shader.setUniform("sampler", 0);
		shader.setUniform("texModifier", translation);
		this.texture.bind(0);
	}
}
