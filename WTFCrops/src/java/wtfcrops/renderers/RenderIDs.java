package wtfcrops.renderers;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderIDs {
	
	public static int renderPass;
	
	public static int customFarmlandRenderer;
	public static int customCropRenderer;
	
	
	public static void register(){
		customFarmlandRenderer = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new CustomFarmlandRenderer());
		
		customCropRenderer = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new CustomCropRenderer());
	}
}
