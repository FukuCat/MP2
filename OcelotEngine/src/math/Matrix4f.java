package math;

import static java.lang.Math.*;

import java.nio.FloatBuffer;

import com.Kon.OcelotEngine.Utils.BufferUtils;

public class Matrix4f {

	private static int SIZE = 4;
	public float[] matrix = new float[SIZE * SIZE];
	
	public static Matrix4f identity() {
		Matrix4f result = new Matrix4f();
		for (int i = 0; i < SIZE; i++) {
			result.matrix[i] = 0.0f;
		}
		// [row + col * max col]
		for (int i = 0; i < SIZE; i++){
		result.matrix[i + i * SIZE] = 1.0f;
		}
		return result;
	}
	
	public Matrix4f multiply(Matrix4f matrix){
		Matrix4f result = new Matrix4f();
		
		for(int col = 0; col < SIZE; col++)
			for(int row = 0; row < SIZE; row++){
				float sum = 0.0f;
				for(int cur = 0; cur < SIZE; cur++)
					sum += this.matrix[cur + col * SIZE] * 
						   matrix.matrix[row + cur * SIZE];
				result.matrix[row + col * SIZE] = sum;
			}
		return result;
	}
	
	/* [ 1 0 0 x ]
	 * [ 0 1 0 y ]
	 * [ 0 0 1 z ]
	 * [ 0 0 0 1 ]
	 */// stores vector graphic location in cur matrix
	public static Matrix4f translate(Vector3f vector){
		Matrix4f result = identity();
		
		result.matrix[0 + 3 * SIZE] = vector.x;
		result.matrix[1 + 3 * SIZE] = vector.y;
		result.matrix[2 + 3 * SIZE] = vector.z;
		
		return result;
	}
	
	/*  c mean cos(x), s means sin(x) where x is an angle
	 * [ c -s 0 0 ]
	 * [ s  c 0 0 ]
	 * [ 0  0 1 0 ]
	 * [ 0  0 0 1 ]
	 */// rotates sprite objects
	public static Matrix4f rotate(float angle){
		Matrix4f result = identity();
		float r = (float) toRadians(angle);
		float sin = (float) sin(r);
		float cos = (float) cos(r);
		
		result.matrix[0 + 0 * SIZE] = cos;
		result.matrix[1 + 0 * SIZE] = sin;

		result.matrix[0 + 1 * SIZE] = -sin;
		result.matrix[1 + 1 * SIZE] = cos;
		
		return result;
	}
	
	/*
	 // 3D implementation
	public static matrix4f rotate(float angle, float x, float y, float z){
	 // floats x y and z are boolean to indicate what axis should rotation be implemented
		Matrix4f result = identity();

		float r = (float) toRadians(angle);
		float cos = (float) cos(r);
		float sin = (float) sin(r);
		float omc = 1.0f - cos;

		result.matrix4f[0 + 0 * 4] = x * omc + cos;
		result.matrix4f[1 + 0 * 4] = y * x * omc + z * sin;
		result.matrix4f[2 + 0 * 4] = x * z * omc - y * sin;

		result.matrix4f[0 + 1 * 4] = x * y * omc - z * sin;
		result.matrix4f[1 + 1 * 4] = y * omc + cos;
		result.matrix4f[2 + 1 * 4] = y * z * omc + x * sin;

		result.matrix4f[0 + 2 * 4] = x * z * omc + y * sin;
		result.matrix4f[1 + 2 * 4] = y * z * omc - x * sin;
		result.matrix4f[2 + 2 * 4] = z * omc + cos;

		return result;
		}
	 */
	
	/* Implementation 2D
	 * right left top bottom => window
	 * near far => min max rendering field
	 * Matrix:
	 * [ 2/(r-l)       0        0 (l+r)/(l-r) ]
	 * [       0  2/(t-b)       0 (b+t)/(b-t) ]
	 * [       0        0 2/(n-f) (n+f)/(f-n) ]
	 * [       0        0       0           1 ]
	 */
	public static Matrix4f orthographic(
			float left, float right, float bottom, 
			float top, float near, float far){
		Matrix4f result = identity();
		
		result.matrix[0 + 0 * SIZE] = 2.0f / (right - left);
		result.matrix[1 + 1 * SIZE] = 2.0f / (top - bottom);
		result.matrix[2 + 2 * SIZE] = 2.0f / (far - near);
		
		result.matrix[0 + 3 * SIZE] = (left + right) / (left - right);
		result.matrix[1 + 3 * SIZE] = (bottom + top) / (bottom - top);
		result.matrix[2 + 3 * SIZE] = (near + far) / (far - near);
		
		return result;
	}
	
	public FloatBuffer toFloatBuffer(){
		return BufferUtils.createFloatBuffer(matrix);
	}
	
}
