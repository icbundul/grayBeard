package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		/*float[] vertices = { 
				// Left bottom triangle
				-0.5f, 0.5f, 0f,	//V0 
				-0.5f, -0.5f, 0f, 	//V1
				0.5f, -0.5f, 0f,	//V2 	
				0.5f, 0.5f, 0f, 	//V3
			};
		
		int[] indices = {
				0,1,3,	// Top left triangle (V0, V1, V3)
				3,1,2	// Bottom right triangle (V3,V1,V2)
		};
		
		float[] textureCoords = {
				0,0, //V0
				0,1, //V1
				1,1, //V2
				1,0	 //V3
		};*/
		
		//RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
	
		RawModel model = OBJLoader.loadObjModel("dragon", loader);
		
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("white")));	
		ModelTexture texture = staticModel.getTexture();
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		
		Entity entity = new Entity(staticModel, new Vector3f(0,0,-25) , 0, 160 , 0, 1);
		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1,1,1));
		
		Camera camera = new Camera();
		
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()) {
			// game logic
			entity.increaseRotation(0, 1, 0);
			camera.move();
			
			
			renderer.processEntity(entity);
			
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
