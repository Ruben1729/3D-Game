package shaders;

import java.io.File;
import java.nio.file.Files;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import main.Game;

/**
 * Class that represents a shader program in OpenGL.
 */
public abstract class ShaderProgram {
	
	/**
	 * The program ID used to operate with the program in OpenGL.
	 */
	public int programID;
	
	/**
	 * The shader IDs used to reference the different shaders in the program in OpenGL.
	 */
	private int vertexShaderID;
	private int fragmentShaderID;
	private int geometryShaderID = -1;
	
	
	/**
	 * Creates a shader program using a vertex and fragment shader.
	 * @param vertexShaderFile Path to the vertex shader file.
	 * @param fragmentShaderFile Path to the fragment shader file.
	 */
	public ShaderProgram(String vertexShaderFile, String fragmentShaderFile) {
		
		if(!loadAndCompileShaders(vertexShaderFile, null, fragmentShaderFile))
			throw new RuntimeException("Shader program failed to link.");
		
	}
	
	/**
	 * Creates a shader program using a vertex, fragment, and geometry shader.
	 * @param vertexShaderFile Path to the vertex shader file.
	 * @param geometryShaderFile Path to the geometry shader file.
	 * @param fragmentShaderFile Path to the fragment shader file.
	 */
	public ShaderProgram(String vertexShaderFile, String geometryShaderFile, String fragmentShaderFile) {
		if(!loadAndCompileShaders(vertexShaderFile, geometryShaderFile, fragmentShaderFile))
			throw new RuntimeException("Shader program failed to link.");
		
	}
	
	/**
	 * Loads and compiles the specified shaders.
	 * @param vert The vertex shader name.
	 * @param geom The geometry shader name (optional).
	 * @param frag The fragment shader name.
	 * @return [boolean] True if all shaders were compiled and linked successfully, false otherwise.
	 */
	private boolean loadAndCompileShaders(String vert, String geom, String frag) {
		try {
			String vertCode, geomCode, fragCode;
			
			vertCode = new String(Files.readAllBytes(new File(Game.class.getResource("/shaders/" + vert).getFile()).toPath()));
			geomCode = geom != null ? new String(Files.readAllBytes(new File(Game.class.getResource("/shaders/" + geom).getFile()).toPath())) : null;
			fragCode = new String(Files.readAllBytes(new File(Game.class.getResource("/shaders/" + frag).getFile()).toPath()));
			
			programID = GL30.glCreateProgram();
			
			vertexShaderID = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
			GL30.glShaderSource(vertexShaderID, vertCode);
			GL30.glCompileShader(vertexShaderID);
			
			if(!checkCompileErrors(vertexShaderID, GL30.GL_VERTEX_SHADER))
				throw new RuntimeException("Failed to compile vertex shader.");
			
			
			GL30.glAttachShader(programID, vertexShaderID);
			if(geomCode != null) {
				geometryShaderID = GL30.glCreateShader(GL32.GL_GEOMETRY_SHADER);
				GL30.glShaderSource(geometryShaderID, geomCode);
				GL30.glCompileShader(geometryShaderID);
				checkCompileErrors(geometryShaderID, GL32.GL_GEOMETRY_SHADER);
				GL30.glAttachShader(programID, geometryShaderID);
			}
			
			
			fragmentShaderID = GL30.glCreateShader(GL30.GL_FRAGMENT_SHADER);
			GL30.glShaderSource(fragmentShaderID, fragCode);
			GL30.glCompileShader(fragmentShaderID);
			checkCompileErrors(fragmentShaderID, GL30.GL_FRAGMENT_SHADER);
			GL30.glAttachShader(programID, fragmentShaderID);
			
			bindAttributes();
			
			GL30.glLinkProgram(programID);
			GL30.glValidateProgram(programID);
			
			if (!checkCompileErrors(programID, 0));
				getAllUniformLocations();
			
			return true;
			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	/**
	 * Checks for compilation errors in a shader or program.
	 * @param shader The shader/program handle.
	 * @param type The type of shader (0 for program).
	 * @return [boolean] True if there are no compilation errors, false otherwise.
	 */
	private boolean checkCompileErrors(int shader, int type) {
		
		switch(type) {
			case GL30.GL_VERTEX_SHADER:
			case GL30.GL_FRAGMENT_SHADER:
			case GL32.GL_GEOMETRY_SHADER:
				int success = GL30.glGetShaderi(shader, GL20.GL_COMPILE_STATUS);
				if(success == 0) {
					String infoLog = GL30.glGetShaderInfoLog(shader);
					Game.LOGGER.println("ERROR::SHADER_COMPILATION_ERROR of type: " + 
					(	type == GL30.GL_VERTEX_SHADER ? "VERTEX" : 
						type == GL30.GL_FRAGMENT_SHADER ? "FRAGMENT" : 
							"GEOMETRY")
					+ "\n" + infoLog + "\n -- --------------------------------------------------- -- ");
					return false;
				}
				return true;
			default:
				int progSuccess = GL30.glGetProgrami(programID, GL20.GL_LINK_STATUS);
				if(progSuccess == 0) {
					String infoLog = GL30.glGetProgramInfoLog(programID);
					Game.LOGGER.println("ERROR::PROGRAM_LINKING_ERROR of type: " + type + "\n" + infoLog + "\n -- --------------------------------------------------- -- ");
					return false;
				}
				return false;
		}
	}
	
	/**
	 * Abstract void defined by classes extending ShaderProgram that bind all necessary attributes to the shader.
	 * @return [void]
	 */
	protected abstract void bindAttributes();
	
	/**
	 * Abstract void defined by classes extending ShaderProgram used to retrieve all uniform locations.
	 * @return [void]
	 */
	protected abstract void getAllUniformLocations();
	
	/**
	 * Returns the location of uniform variable in the shader.
	 * @param name The name of the uniform variable.
	 * @return [int] The location of the uniform variable.
	 */
	protected int getUniformLocation(String name) {
		return GL30.glGetUniformLocation(programID, name);
	}
	
	/**
	 * Binds the specified attribute index to the specified attribute name as an input in the shader.
	 * @param attribIndex The index of the attribute to bind (0-14).
	 * @param attribName The name of the attribute as written in the shader.
	 * @return [void]
	 */
	protected void bindAttribute(int attribIndex, String attribName) {
		GL30.glBindAttribLocation(programID, attribIndex, attribName);
	}
	
	/**
	 * Tells OpenGL to use the shader program to render future primitives.
	 * @return [void]
	 */
	public void use() {
		GL30.glUseProgram(programID);
	}
	
	/**
	 * Stops using the shader for future render calls.
	 * @return [void]
	 */
	public void stop() {
		GL30.glUseProgram(0);
	}
	
	/**
	 * Loads a matrix to a uniform variable in the shader.
	 * @param location The uniform to write to.
	 * @param mat The matrix to write.
	 * @return [void]
	 */
	public void loadMatrix(int location, Matrix4f mat) {
		float[] matrix = new float[16];
		mat.get(matrix);
		GL20.glUniformMatrix4fv(location, false, matrix);
	}
	
	/**
	 * Loads the transpose of a matrix to a uniform variable in the shader.
	 * @param location The uniform to write to.
	 * @param mat The matrix to transpose and write.
	 * @return [void]
	 */
	public void loadMatrixTranspose(int location, Matrix4f mat) {
		float[] matrix = new float[16];
		mat.get(matrix);
		GL20.glUniformMatrix4fv(location, true, matrix);
	}
	
	/**
	 * Loads a two-dimensional vector to a uniform variable in the shader.
	 * @param location The uniform location to write to.
	 * @param vec The vector to write.
	 * @return [void]
	 */
	protected void load2DVector(int location, Vector2f vector){
        GL20.glUniform2f(location,vector.x,vector.y);
    }
	
	/**
	 * Loads a three-dimensional vector to a uniform variable in the shader.
	 * @param location The uniform location to write to.
	 * @param vec The vector to write.
	 * @return [void]
	 */
	public void loadVector(int location, Vector3f vec) {
		GL20.glUniform3fv(location, new float[] { vec.x, vec.y, vec.z });
	}
	
	/**
	 * Loads a four-dimensional vector to a uniform variable in the shader.
	 * @param location The uniform location to write to.
	 * @param vec The vector to write.
	 * @return [void]
	 */
	public void loadVector(int location, Vector4f vec) {
		GL20.glUniform4fv(location, new float[] { vec.x, vec.y, vec.z, vec.w });
	}
	
	/**
	 * Loads an integer to a uniform variable in the shader.
	 * @param location The uniform location to write to.
	 * @param value The integer to write.
	 * @return [void]
	 */
	public void loadInt(int location, int value) {
		GL20.glUniform1i(location, value);
	}
	
	/**
	 * Cleanup method to delete the shader program on game exit.
	 * @return [void]
	 */
	public void cleanUp(){
        stop();
        
        if(geometryShaderID != -1){
        	GL20.glDetachShader(programID, geometryShaderID);
            GL20.glDeleteShader(geometryShaderID);
        }
        
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }
	
}
