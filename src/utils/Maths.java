package utils;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import render.Camera3D;

public class Maths {

	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.identity();
		matrix.translate(translation, matrix);
		matrix.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix);
		matrix.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix);
		matrix.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix);
		matrix.scale(new Vector3f(scale, scale, scale), matrix);
		return matrix;
	}

	public static Matrix4f createViewMatrix(Camera3D camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.identity();
		viewMatrix.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix);
		viewMatrix.rotate((float) Math.toRadians(camera.getYaw()),   new Vector3f(0, 1, 0), viewMatrix);
		viewMatrix.rotate((float) Math.toRadians(camera.getRoll()),  new Vector3f(0, 0, 1), viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		viewMatrix.translate(negativeCameraPos, viewMatrix);
		return viewMatrix;
	}

	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}

	/*
	 * returns a vector that contains larger of each component of input vectors
	 */
	public static Vector3f vectorMax3f(Vector3f v1, Vector3f v2) {
		return new Vector3f(
			Math.max(v1.x, v2.x),
			Math.max(v1.y, v2.y),
			Math.max(v1.z, v2.z)
		);
	}

	/*
	 * returns max component in a vector
	 */
	public static float vectorMaxComponent3f(Vector3f v) {
		float max = v.x;
		if (v.y > max) max = v.y;
		if (v.z > max) max = v.z;
		return max;
	}
}
