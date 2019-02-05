package engine;

import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import util.Vao;

/**
 * Util class for loading meshes into Vertex Array Objects (VAOs).
 */
public class Loader {
	
	public static Texture MISSING_TEXTURE = new Loader().loadTexture("missing.png", true);
	
	/**
	 * We store a list of VAOs in order to clean them up on exit.
	 */
	private ArrayList<Vao> vaos = new ArrayList<Vao>();
	
	/**
	 * We store a list of textures in order to clean them up on exit.
	 */
	private ArrayList<Texture> textures = new ArrayList<Texture>();
	
	/**
	 * Loads a list of vertices into a VAO and returns a mesh object representing it.
	 * @param vertices The vertex data.
	 * @param uvs The texture coordinate data.
	 * @return [Mesh] A {@link Mesh} object representing the three-dimensional model.
	 */
	public int load(float[] vertices, float[] uvs) {
       
		// Create the VAO and add it to the list.
		Vao vao = Vao.create();
		
		// Bind the VAO.
		vao.bind();
		
        // Store the vertex data in attribute list 0.
		vao.storeFloatData(0, vertices, 2);
		
		// Store the texture coordinate data in attribute list 1.
		vao.storeFloatData(1, uvs, 2);

        // Unbind.
		vao.unbind();
		
		// Return the new mesh object.
        return vao.getID();
    }
	
	/**
	 * Loads a list of vertices using indexes into a VAO and returns a mesh object representing it.
	 * @param indices The index data for the vertices.
	 * @param vertices The vertex data.
	 * @param normals The normal data.
	 * @param uvs The texture coordinate data.
	 * @return [Mesh] A {@link Mesh} object representing the three-dimensional model.
	 */
	public Mesh load(int[] indices, float[] vertices, float[] normals, float[] uvs) {
		
		// Create the VAO and add it to the list.
		Vao vao = Vao.create();
	
		// Bind the VAO.
		vao.bind();
		
		// Store the index data in an element array buffer.
		vao.storeIndices(indices);
		
		// Store the vertex data in attribute list 0.
		vao.storeFloatData(0, vertices, 3);
		
		// Store the vertex data in attribute list 0.
		vao.storeFloatData(1, normals, 3);
		
		// Store the vertex data in attribute list 0.
		vao.storeFloatData(2, uvs, 2);
		
		// Unbind.
		vao.unbind();
		
		// Return the new mesh object.
		return new Mesh(vao, indices.length);
	}
	
	public Mesh load(int[] indices, float[] vertices, float[] normals, float[] uvs, int[] jointIDs, float[] jointWeights) {

		// Create the VAO and add it to the list.
		Vao vao = Vao.create();
		
		// Bind the VAO.
		vao.bind(); 
		
		// Store the index data in an element array buffer.
		vao.storeIndices(indices);
		
		// Store the vertex data in attribute list 0.
		vao.storeFloatData(0, vertices, 3);
		
		// Store the normal data in attribute list 1.
		vao.storeFloatData(1, normals, 3);
		
		// Store the texture coordinate data in attribute list 2.
		vao.storeFloatData(2, uvs, 2);
		
		// Store the joint ID data in attribute list 3.
		vao.storeIntData(3, jointIDs, 3);
		
		// Store the joint weight data in attribute list 4.
		vao.storeFloatData(4, jointWeights, 3);
		
		// Unbind.
		vao.unbind();
		
		// Return the new mesh object.
		return new Mesh(vao, indices.length);
		
	}
	
	/**
	 * Loads a texture from a file.
	 * @param filename The file name.
	 * @param mipmap Whether or not to generate a mipmap for the texture.
	 * @return [int] The texture ID of the loaded texture.
	 */
	public Texture loadTexture(String filename, boolean mipmap) {
		try {
			BufferedImage img = ImageIO.read(new File("./res/tex/" + filename));
			int texture = glGenTextures();
			Texture result = null;
			
			glBindTexture(GL_TEXTURE_2D, texture);
			
			int[] pixels = new int[img.getWidth() * img.getHeight()];
			img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());

	        ByteBuffer buffer = ByteBuffer.allocateDirect(img.getWidth() * img.getHeight() * img.getColorModel().getPixelSize() / 8); //4 for RGBA, 3 for RGB
	        int pSize = img.getColorModel().getPixelSize();
	        for(int y = 0; y < img.getHeight(); y++){
	            for(int x = 0; x < img.getWidth(); x++){
	                int pixel = pixels[y * img.getWidth() + x];
	                buffer.put((byte) ((pixel >> (pSize / 2)) & 0xFF));     // Red component
	                buffer.put((byte) ((pixel >> (pSize / 4)) & 0xFF));      // Green component
	                buffer.put((byte) (pixel & 0xFF));             // Blue component
	                buffer.put((byte) ((pixel >> (3 * pSize / 4)) & 0xFF));    // Alpha component. Only for RGBA
	            }
	        }

	        buffer.flip();
			
	        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, img.getWidth(), img.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			glGenerateMipmap(GL_TEXTURE_2D);
			if(!mipmap) {
				result = new Texture(texture, GL_TEXTURE_2D);
				textures.add(result);
				return result;
			}
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.4f);
			result = new Texture(texture, GL_TEXTURE_2D);
			textures.add(result);
			return result;
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * Deletes all VBOs, VAOs, and textures.
	 * @return [void]
	 */
	public void cleanUp() {
		
		for(Vao vao : vaos)
			vao.delete();
		
		for(Texture tex : textures)
			tex.delete();
		
	}

}	
