package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import renderEngine.EntityRenderer;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;

/*
	@author ivanc
*/

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
	
		RawModel model = OBJLoader.loadObjModel("tree", loader);
		
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("tree")));	
		ModelTexture texture = staticModel.getTexture();
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		
		/* -- */
		
		final int NUMBER_ENTITIES = 40000;
		
		List<Entity> entities = new ArrayList<Entity>();
		
		Random randomGenerator = new Random();
		
		 for (int idx = 1; idx <= NUMBER_ENTITIES; ++idx){
		      entities.add(new Entity(staticModel, new Vector3f(randomGenerator.nextInt(800), 0, -randomGenerator.nextInt(800)),0, 0 , 0, 1));
		    }
		
		/* -- */ 
		 
		Entity entity = new Entity(staticModel, new Vector3f(40,0,-60) , 0, 0 , 0, 1);
		Entity entity2 = new Entity(staticModel, new Vector3f(40,0,-40) , 0, 0 , 0, 1);
		
		
		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1,1,1));
		
		Terrain terrain = new Terrain(1, -1, loader, new ModelTexture(loader.loadTexture("grass"))); //(0,-1), (1,-1)
		Terrain terrain2 = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass"))); //(0,-1), (1,-1)
		
		Camera camera = new Camera();
		
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()) {
			// game logic
			entity.increaseRotation(0, 1, 0);
			camera.move();
			
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			renderer.processEntity(entity);
			renderer.processEntity(entity2);
			
			for(int index = 0; index < entities.size(); index++) {
				renderer.processEntity(entities.get(index));
				entities.get(index).increaseRotation(0, 1, 0);
			}
			
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
