package wtfcrops.blocks.customcrops;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.BiomeDictionary.Type;
import wtfcrops.Crops;
import wtfcrops.WTFCropsConfig;
import wtfcrops.blocks.customcrops.cropbases.CustomCrop;
import wtfcrops.utilities.BlockAndMeta;

public class CustomWheat extends CustomCrop{

	public CustomWheat(int cultivar) {
		super(cultivar, Items.wheat_seeds, Items.wheat);
		this.baseGrowthRate = WTFCropsConfig.growthRateWheat;

	}
	@Override
	@SideOnly(Side.CLIENT)
	protected String getTextureName()
	{
		return "minecraft:wheat";
	}
	
	@Override
	protected void loadFarmlandHashMap(){
		
		float baseFarmland = 1.5F;//this should be the base bonus you get for putting it on farmland- including the bonus for having perfect hydration
		float badHydration = .25F;//this gets multiplied by the hydration value of the plant- it tells you how sensitive the plant is to the wrong hydration	
		int idealHydration = 8; //the hydration value where the plant is happiest, 0-15, 0 is dry, 15 is wet
		float fertaliserBonus = 1f; //bonus the fertalised version of the soil gets
		
		for (int loop = 0; loop < 16; loop ++){
			float hydroValue = (loop-idealHydration)*badHydration; 
			float hydrationPenalty = hydroValue*hydroValue; 
			farmlandModifier.put(new BlockAndMeta(Blocks.farmland, loop), baseFarmland-hydrationPenalty); 
			farmlandModifier.put(new BlockAndMeta(Crops.fertileFarmland, loop),  fertaliserBonus+baseFarmland-hydrationPenalty);
		}
		
		farmlandModifier.put(new BlockAndMeta(Blocks.sand, 0), -4F);
		farmlandModifier.put(new BlockAndMeta(Blocks.sand, 1), -4F);//red sand
		
		farmlandModifier.put(new BlockAndMeta(Blocks.dirt, 0), -1.5F);
		farmlandModifier.put(new BlockAndMeta(Blocks.dirt, 1), -1.5F);//coarse dirt
		farmlandModifier.put(new BlockAndMeta(Blocks.dirt, 2), -1.5F);//podzol
		
		
		farmlandModifier.put(new BlockAndMeta(Blocks.grass, 0), -3F);
	}

	@Override
	protected void loadBiomeHashMap() {
		biomeMods.put(Type.HOT, -0.5F);
		biomeMods.put(Type.COLD, -1F);
		biomeMods.put(Type.SPARSE, -0.2F);
		biomeMods.put(Type.DENSE, 0.2F);
		biomeMods.put(Type.SAVANNA, 0.5F);
		biomeMods.put(Type.CONIFEROUS, 0F);
		biomeMods.put(Type.JUNGLE, 0F);
		biomeMods.put(Type.SPOOKY, 0F);
		biomeMods.put(Type.DEAD, -5F);
		biomeMods.put(Type.LUSH, 0F);
		biomeMods.put(Type.NETHER, -5F);
		biomeMods.put(Type.END, 0F);
		biomeMods.put(Type.MUSHROOM, 0F);
		biomeMods.put(Type.MAGICAL, 0F);
		biomeMods.put(Type.OCEAN, -3F);
		biomeMods.put(Type.RIVER, 0F);
		biomeMods.put(Type.MESA, +1F);
		biomeMods.put(Type.FOREST, -0.2F);
		biomeMods.put(Type.PLAINS, +1F);
		biomeMods.put(Type.MOUNTAIN, -0.5F);
		biomeMods.put(Type.HILLS, -0.2F);
		biomeMods.put(Type.SWAMP, -1F);
		biomeMods.put(Type.SANDY, -2F);
		biomeMods.put(Type.SNOWY, -1.5F);
		biomeMods.put(Type.WASTELAND, -1F);
		biomeMods.put(Type.BEACH, -1F);
	}

}
