package render;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

public class Shader {
	
	private int program;
	private int vs;
	private int fs;

	public Shader(String filename) {

		program = GL20.glCreateProgram();

		vs = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		GL20.glShaderSource(vs, readFile(filename + "_vs.glsl"));
		GL20.glCompileShader(vs);
		if (GL20.glGetShaderi(vs, GL20.GL_COMPILE_STATUS) != 1) {
			System.err.println("Failed to compile vertex shader ./shaders/" + filename + "_vs.glsl");
			System.err.println(GL20.glGetShaderInfoLog(vs));
			System.exit(1);
		}

		fs = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(fs, readFile(filename + "_fs.glsl"));
		GL20.glCompileShader(fs);
		if (GL20.glGetShaderi(fs, GL20.GL_COMPILE_STATUS) != 1) {
			System.err.println("Failed to compile fragment shader ./shaders/" + filename + "_fs.glsl");
			System.err.println(GL20.glGetShaderInfoLog(fs));
			System.exit(1);
		}

		GL20.glAttachShader(program, vs);
		GL20.glAttachShader(program, fs);
		
		GL20.glBindAttribLocation(program, 0, "vertices");
		GL20.glBindAttribLocation(program, 1, "textures");

		GL20.glLinkProgram(program);
		if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) != 1) {
			System.err.println("Link program failed for " + filename);
			System.err.println(GL20.glGetProgramInfoLog(program));
			System.exit(1);
		}

		GL20.glValidateProgram(program);
		if (GL20.glGetProgrami(program, GL20.GL_VALIDATE_STATUS) != 1) {
			System.err.println("Validate program failed for " + filename);
			System.err.println(GL20.glGetProgramInfoLog(program));
			System.exit(1);
		}
	}

	public void setUniform(String name, float value) {
		int location = GL20.glGetUniformLocation(program, name);
		if (location != -1) {
			GL20.glUniform1f(location, value);
			// System.out.println("Set value " + value + " to uniform " + name);
		}
	}

	public void setUniform(String name, Matrix4f value) {
		int location = GL20.glGetUniformLocation(program, name);
		if (location != -1) {
			FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
			value.get(buffer);
			GL20.glUniformMatrix4fv(location, false, buffer);
			// System.out.println("Set value " + value + " to uniform " + name);
		}
	}

	public void setUniform(String name, Vector4f value) {
		int location = GL20.glGetUniformLocation(program, name);
		if (location != -1) {
			FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
			value.get(buffer);
			GL20.glUniform4fv(location, buffer);
		}
	}

	public void bind() {
		GL20.glUseProgram(program);
	}

	public void unbind() {
		GL20.glUseProgram(0);
	}

	private String readFile(String filename) {
		StringBuilder string = new StringBuilder();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File("./shaders/" + filename)));
			String line;
			while((line = br.readLine()) != null) {
				string.append(line);
				string.append("\n");
			}
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return string.toString();
	}

	protected void finalize() throws Throwable {
		// GL20.glDetachShader(program, vs);
		// GL20.glDetachShader(program, fs);
		// GL20.glDeleteShader(vs);
		// GL20.glDeleteShader(fs);
		// GL20.glDeleteProgram(program);		
		super.finalize();
	}

}
