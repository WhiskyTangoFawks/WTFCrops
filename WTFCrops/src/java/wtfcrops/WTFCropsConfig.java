package wtfcrops;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class WTFCropsConfig {

	//chances per 500 that it will grow
	public static int growthRateWheat;
	public static int growthRateCarrot;
	public static int growthRatePotato;
	public static int growthRatePumpkin;
	public static int growthRateMelon;
	public static int growthRateMushroom;
	public static int giantMushroomRate;
	public static int growthRateSugar;
	public static int growthRateNetherWart;
	public static int growthRateCocoa;
	
	public static boolean replaceLeaves;
	public static boolean killPlants;
	
	public static void customConfig() {

		Configuration config = new Configuration(new File("config/WTFCropsConfig.cfg"));

		config.load();
		growthRateWheat = config.get("Plant Growth Rate", "Wheat Growth Rate", 5).getInt();
		growthRateCarrot = config.get("Plant Growth Rate", "Carrot Growth Rate", 5).getInt();
		growthRatePotato = config.get("Plant Growth Rate", "Potato Growth Rate", 5).getInt();
		growthRatePumpkin = config.get("Plant Growth Rate", "Pumpkin Growth Rate", 5).getInt();
		growthRateMelon = config.get("Plant Growth Rate", "Melon Growth Rate", 5).getInt();
		growthRateMushroom = config.get("Plant Growth Rate", "Mushroom Growth Rate", 5).getInt();
		giantMushroomRate = config.get("Plant Growth Rate", "Giant Mushroom Growth Rate", 5).getInt();
		growthRateSugar = config.get("Plant Growth Rate", "Sugar Cane Growth Rate", 5).getInt();
		growthRateNetherWart = config.get("Plant Growth Rate", "Nether Wart Growth Rate", 5).getInt();
		growthRateCocoa = config.get("Plant Growth Rate", "Cocoa Growth Rate", 5).getInt();
		
		killPlants = config.get("Plant Options", "Kill the plant in poor conditions", false).getBoolean();
		replaceLeaves = config.get("Plant Options", "Replace vanilla leaves with non-solid leaves", false).getBoolean();
		
		config.save();

	}
	
}
