package render;

import org.joml.Matrix4f;

import camera.Camera;
import engine.Loader;
import entities.Entity;
import lights.Light;
import shaders.StaticShader;

public class EntityRenderer {
	
	private StaticShader shader;
	
	public EntityRenderer(Matrix4f projection) {
		
		// Create the shader program.
		shader = new StaticShader();
		
		// Start the shader.
		shader.use();
		
		// Load projection matrix into shader.
		shader.loadMatrix(shader.location_projectionMatrix, projection);
		
		// Load identity view matrix for now.
		shader.loadMatrix(shader.location_viewMatrix, new Matrix4f().identity());
		
		// Use texture 0 always.
		shader.loadInt(shader.location_texture, 0);
		
		// Stop the shader.
		shader.stop();
		
	}
	
	
	
	public void render(Entity ent, Camera cam, Light[] lights) {
		
		begin();
		
		for(int i = 0; i < lights.length; i++)
			shader.loadLight(i, lights[i]);
		
		shader.loadMatrix(shader.location_modelMatrix, ent.getTransform());
		shader.loadMatrix(shader.location_viewMatrix, cam.getTransform());
		
		
		if(ent.getTexturedMesh().getTexture() != null)
			ent.getTexturedMesh().getTexture().bindToUnit(0);
		else
			Loader.MISSING_TEXTURE.bindToUnit(0);
		
		ent.getTexturedMesh().getMesh().getVao().bind(0, 1, 2);
		ent.getTexturedMesh().getMesh().draw();
		ent.getTexturedMesh().getMesh().getVao().unbind(0, 1, 2);
		
		end();
		
	}
	
	private void begin() {
		shader.use();
	}
	
	private void end() {
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
	
}
