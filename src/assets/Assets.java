package assets;

import render.Model;

public class Assets {
	
	private static Model model;
	
	public static Model getModel() { return model; }

	public static void initAsset() {
		model = new Model(Sprite.getVertices(), Sprite.getTexCoords(), Sprite.getIndices());
	}

	public static void deleteAsset() {
		model = null;
	}
}
