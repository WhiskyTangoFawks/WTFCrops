package wtfcrops.blocks.customcrops;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.BiomeDictionary.Type;
import wtfcore.WTFCore;
import wtfcrops.Crops;
import wtfcrops.WTFCropsConfig;
import wtfcrops.blocks.customcrops.cropbases.CustomCropStem;
import wtfcrops.blocks.customcrops.cropbases.CustomGourdBase;
import wtfcrops.utilities.BlockAndMeta;

public class CustomPumpkinStem extends CustomCropStem{

	public CustomPumpkinStem(int cultivar) {
		super(cultivar, Crops.blockPumpkin, Crops.pumpkinStalk, Items.pumpkin_seeds, Crops.itemPumpkin);
		this.baseGrowthRate = WTFCropsConfig.growthRatePumpkin;
		this.seed = Items.pumpkin_seeds;
		this.setBlockTextureName("pumpkin_stem");
	}
	
	public int getState(IBlockAccess world, int x, int y, int z)
	{
		
		int meta = world.getBlockMetadata(x, y, z);
		
		int gourdX = x;
		int gourdZ = z;
		int stemDir = -1;
		int gourdDir = -1;
		if (meta < 7) {return -1;}
		else if (world.getBlock(x - 1, y, z) instanceof CustomPumpkinBlock){gourdX=x-1;stemDir=0;gourdDir=3;}
		else if (world.getBlock(x + 1, y, z) instanceof CustomPumpkinBlock){gourdX=x-1;stemDir=1;gourdDir=0;}
		else if (world.getBlock(x, y, z-1) instanceof CustomPumpkinBlock){gourdZ=z-1;stemDir=2;gourdDir=0;}
		else if (world.getBlock(x, y, z+1) instanceof CustomPumpkinBlock){gourdZ=z+1;stemDir=3;gourdDir=2;}
		if (CustomGourdBase.getDirection(world.getBlockMetadata(gourdX, y, gourdZ))==gourdDir){
			return stemDir;
		}
		else {return -1;}

	}
	
	@Override
	protected void loadFarmlandHashMap(){
		float baseFarmland = 1.5F;//this should be the base bonus you get for putting it on farmland- including the bonus for having perfect hydration
		float badHydration = .25F;//this gets multiplied by the hydration value of the plant- it tells you how sensitive the plant is to the wrong hydration	
		int idealHydration = 7; //the hydration value where the plant is happiest, 0-15, 0 is dry, 15 is wet
		float fertaliserBonus = 1f; //bonus the fertalised version of the soil gets

		for (int loop = 0; loop < 16; loop ++){
			float hydroValue = (loop-idealHydration)*badHydration; 
			float hydrationPenalty = hydroValue*hydroValue; 
			farmlandModifier.put(new BlockAndMeta(Blocks.farmland, loop), baseFarmland-hydrationPenalty); 
			farmlandModifier.put(new BlockAndMeta(Crops.fertileFarmland, loop),  fertaliserBonus+baseFarmland-hydrationPenalty);
		}

		farmlandModifier.put(new BlockAndMeta(Blocks.sand, 0), -4F);
		farmlandModifier.put(new BlockAndMeta(Blocks.sand, 1), -4F);//red sand

		farmlandModifier.put(new BlockAndMeta(Blocks.dirt, 0), -1F);
		farmlandModifier.put(new BlockAndMeta(Blocks.dirt, 1), -1.5F);//coarse dirt
		farmlandModifier.put(new BlockAndMeta(Blocks.dirt, 2), +1F);//podzol

		farmlandModifier.put(new BlockAndMeta(Blocks.grass, 0), -3F);
		
		for (int loop = 0; loop < 8; loop++){
			farmlandModifier.put(new BlockAndMeta(Crops.pumpkinStalk, 0), 0F);
		}
	}

	@Override
	protected void loadBiomeHashMap() {
		biomeMods.put(Type.HOT, -0.5F);
		biomeMods.put(Type.COLD, -1F);
		biomeMods.put(Type.SPARSE, -0.2F);
		biomeMods.put(Type.DENSE, 0.2F);
		biomeMods.put(Type.SAVANNA, 0.5F);
		biomeMods.put(Type.CONIFEROUS, +1F);
		biomeMods.put(Type.JUNGLE, 0F);
		biomeMods.put(Type.SPOOKY, +3F);
		biomeMods.put(Type.DEAD, 0F);
		biomeMods.put(Type.LUSH, 0F);
		biomeMods.put(Type.NETHER, -2F);
		biomeMods.put(Type.END, 0F);
		biomeMods.put(Type.MUSHROOM, 0F);
		biomeMods.put(Type.MAGICAL, 0F);
		biomeMods.put(Type.OCEAN, -1F);
		biomeMods.put(Type.RIVER, 0.5F);
		biomeMods.put(Type.MESA, -0.5F);
		biomeMods.put(Type.FOREST, 1.5F);
		biomeMods.put(Type.PLAINS, -0.1F);
		biomeMods.put(Type.MOUNTAIN, 0.5F);
		biomeMods.put(Type.HILLS, 0.2F);
		biomeMods.put(Type.SWAMP, +0.2F);
		biomeMods.put(Type.SANDY, -3F);
		biomeMods.put(Type.SNOWY, -0.5F);
		biomeMods.put(Type.WASTELAND, -1F);
		biomeMods.put(Type.BEACH, -1F);
	}
}
