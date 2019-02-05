package render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import animation.AnimatedModel;
import camera.Camera;
import engine.Loader;
import shaders.AnimatedModelShader;

public class AnimatedModelRenderer {
	
	private AnimatedModelShader shader;
	
	public AnimatedModelRenderer(Matrix4f projection) {
		
		// Create the shader program.
		shader = new AnimatedModelShader();
		
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
	
	
	
	public void render(AnimatedModel ent, Camera cam, Vector3f lightDirection) {
		
		begin();
		
		shader.loadVector(shader.location_lightDirection, lightDirection);
		shader.loadMatrix(shader.location_modelMatrix, new Matrix4f().rotate((float)Math.toRadians(-90), new Vector3f(1, 0, 0)));
		shader.loadMatrix(shader.location_viewMatrix, cam.getTransform());
		
		Matrix4f[] transforms = ent.getJointTransforms();
		//System.out.println(transforms.length);
		for(int i = 0; i < transforms.length; i++)
			shader.loadMatrix(shader.location_jointTransforms[i], transforms[i]);
		
		if(ent.getTexture() != null)
			ent.getTexture().bindToUnit(0);
		else
			Loader.MISSING_TEXTURE.bindToUnit(0);
		
		ent.getModel().getVao().bind(0, 1, 2, 3, 4);
		ent.getModel().draw();
		ent.getModel().getVao().unbind(0, 1, 2, 3, 4);
		
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
