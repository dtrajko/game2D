package render;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Texture {
	
	private int id;
	private int width;
	private int height;
	
	public Texture(String filename) {
		
		String filePath = "./res/textures/" + filename + ".png";
		try {
			 BufferedImage bi = ImageIO.read(new File(filePath));
			 width  = bi.getWidth();
			 height = bi.getHeight();
			 int[] pixels_raw = new int[width * height];
			 pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width);
			 ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

			 for (int i = 0; i < width; i++) {
				 for (int j = 0; j < height; j++) {
					 int pixel = pixels_raw[i * width + j];
					 pixels.put((byte)((pixel >> 16) & 0xFF)); // RED
					 pixels.put((byte)((pixel >>  8) & 0xFF)); // GREEN
					 pixels.put((byte)((pixel >>  0) & 0xFF)); // BLUE
					 pixels.put((byte)((pixel >> 24) & 0xFF)); // ALPHA
				 }
			 }

			 pixels.flip();
			 
			 id = GL11.glGenTextures();
			 GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
			 GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			 GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			 GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);

		} catch (IOException e) {
			System.err.println("Failed to load texture '" + filePath + "'");
			e.printStackTrace();
		}
	}
	
	public void bind(int sampler) {
		if (sampler >= 0 && sampler <= 31 ) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + sampler);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		}
	}
	
	protected void finalize() throws Throwable {
		// GL11.glDeleteTextures(id);
		super.finalize();
	}
}
