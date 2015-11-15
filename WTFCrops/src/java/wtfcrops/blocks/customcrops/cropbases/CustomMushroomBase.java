package wtfcrops.blocks.customcrops.cropbases;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraftforge.common.BiomeDictionary.Type;
import wtfcore.WTFCore;
import wtfcrops.Crops;
import wtfcrops.WTFCrops;
import wtfcrops.WTFCropsConfig;
import wtfcrops.renderers.ICropRenderInfo;
import wtfcrops.renderers.RenderIDs;
import wtfcrops.utilities.BlockAndMeta;

public class CustomMushroomBase extends CustomCropWithoutItem implements ICropRenderInfo{

	public CustomMushroomBase(int cultivar, Block[] crop, Item seed) {
		super(cultivar, crop, seed, seed);
		this.baseGrowthRate = WTFCropsConfig.growthRateMushroom;
		switch (cultivar){
		case Crops.wild://Wild: grows at 3x standard rate, drops: 1-3 seeds, 0/0/1 wheat
			maxGrowth = 2;break;
		case Crops.simple://simple: grows at standard rate: 1-3 seeds, 1.5 wheat
			maxGrowth = 3;break;
		case Crops.giant://giant grows at 1/3 standard rate, negative modifiers x2, 2x 1-2 seeds, 2-4 wheat
			maxGrowth = 4;
		case Crops.dwarf://dwarf: grows at standard rate, negative modifiers 1/2, 1-2 seeds, 1.5 wheat
			maxGrowth = 1;
		case Crops.golden://golden: grows at 1/10th standard rate, 10x as much food or golden version
			maxGrowth = 2;
		}
	}
	 
	@Override
	public int getRenderType() {
		return RenderIDs.customCropRenderer;
	}
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
        float f = (world.getBlockMetadata(x,y,z)+1)*0.1F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
	}
	
	@Override
	public float getLightModifier(World world, int x, int y, int z){
		int lightValue = world.getFullBlockLightValue(x, y, z);
		if (lightValue > 12){return -15F;}
		return (lightValue-6)/-3F;
	}



	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (!world.isRemote){
			this.checkAndDropBlock(world, x, y, z);
			int meta = world.getBlockMetadata(x, y, z);
			if (meta < maxGrowth) {
				if (shouldGrow(world, random, x, y, z)) {
					growCrop(world, x, y, z, meta);
				}
			}
			else {
				if (this.cultivar == Crops.giant && shouldGrowGiant(world, random, x, y, z) && genGiantMushroom(world, x, y, z)){
					//don't spawn sideways
				}
				else {
					spawnNearby(world, random, x, y, z, meta);
				}
			}
		}
	}

	public boolean shouldGrowGiant(World world, Random random, int x, int y, int z){
		float growthModifier = getGrowthModifier(world, x, y, z);
		if (growthModifier > 10F){
			int randomGrowth = MathHelper.ceiling_float_int(235/growthModifier);
			if (random.nextInt(randomGrowth) < WTFCropsConfig.giantMushroomRate){
				return true;
			}
		}
		int randomGrowth = MathHelper.ceiling_float_int(235/growthModifier);
		if (random.nextInt(randomGrowth) < baseGrowthRate*getCultivarModifier()){
			return true;
		}
		return false;
	}
	
	@Override
	public void spawnNearby(World world, Random random, int x, int y, int z, int meta){
		//use food.cultivars[cultivar] to get the block to place
		ArrayList<ChunkPosition> arraylist = new ArrayList<ChunkPosition>();
    	for (int xloop = -4; xloop < 5; xloop++){
    		for (int zloop = -4; zloop < 5; zloop++){
    			for (int yloop = 3; yloop > -3 || world.isAirBlock(xloop, y+yloop, z); yloop--){
    				if (canBlockStay(world, x+xloop, y+yloop+1, z+zloop)){
    				arraylist.add(new ChunkPosition(x+xloop, y+yloop+1, z+zloop));
    				}
    			}
    		}
    	}
    	   ChunkPosition chunkposition = arraylist.get(random.nextInt(arraylist.size()));
    	   world.setBlock(chunkposition.chunkPosX, chunkposition.chunkPosY, chunkposition.chunkPosZ, crop[this.getSeedMetaToDrop(z, random, getGrowthModifier(world, x, y, z))]);
	}

	public boolean genGiantMushroom(World world, int x, int y, int z)
	{
		world.setBlockToAir(x, y, z);
		WorldGenBigMushroom worldgenbigmushroom = null;

		if (this == Blocks.brown_mushroom){
			worldgenbigmushroom = new WorldGenBigMushroom(0);
		}
		else if (this == Blocks.red_mushroom){
			worldgenbigmushroom = new WorldGenBigMushroom(1);
		}

		if (worldgenbigmushroom != null && worldgenbigmushroom.generate(world, new Random(), x, y, z))		{
			return true;
		}
		return false;
	}


	


	@Override
	public float getFarmlandModifier(World world, int x, int y, int z){
		BlockAndMeta block = new BlockAndMeta(world, x, y-1, z);
		if (farmlandModifier.containsKey(block)){
			float modifier = farmlandModifier.get(block);
			return modifier;
		}
		else {
			return -5F;
		}
	}
	
	@Override
	protected void loadFarmlandHashMap(){
		float baseFarmland = 0F;//this should be the base bonus you get for putting it on farmland- including the bonus for having perfect hydration
		float badHydration = 0.85F;//this gets multiplied by the hydration value of the plant- it tells you how sensitive the plant is to the wrong hydration	
		int idealHydration = 7; //the hydration value where the plant is happiest, 0-15, 0 is dry, 15 is wet
		float fertaliserBonus = 1f; //bonus the fertalised version of the soil gets
		
		for (int loop = 0; loop < 16; loop ++){
			float hydroValue = (loop-idealHydration)*badHydration; 
			float hydrationPenalty = hydroValue*hydroValue; 
			farmlandModifier.put(new BlockAndMeta(Blocks.farmland, loop), baseFarmland-hydrationPenalty); 
			farmlandModifier.put(new BlockAndMeta(Crops.fertileFarmland, loop),  fertaliserBonus+baseFarmland-hydrationPenalty);
		}
		
		farmlandModifier.put(new BlockAndMeta(Blocks.sand, 0), -5F);
		farmlandModifier.put(new BlockAndMeta(Blocks.sand, 1), -5F);//red sand
		
		farmlandModifier.put(new BlockAndMeta(Blocks.dirt, 0), -1F);
		farmlandModifier.put(new BlockAndMeta(Blocks.dirt, 1), -1.5F);//coarse dirt
		farmlandModifier.put(new BlockAndMeta(Blocks.dirt, 2), 0.5F);//podzol
		farmlandModifier.put(new BlockAndMeta(Blocks.mycelium, 0), +1.5F);
		farmlandModifier.put(new BlockAndMeta(Blocks.netherrack, 0), 0F);

		for (int loop = 0; loop < 16; loop++){
			farmlandModifier.put(new BlockAndMeta(Blocks.log, loop), 1.5F);
			farmlandModifier.put(new BlockAndMeta(Blocks.log2, loop), 1.5F);
		}
		
		farmlandModifier.put(new BlockAndMeta(Blocks.grass, 0), -1F);	
	}

	@Override
	protected void loadBiomeHashMap() {
		biomeMods.put(Type.HOT, 1F);
		biomeMods.put(Type.COLD, -1F);
		biomeMods.put(Type.SPARSE, -0.2F);
		biomeMods.put(Type.DENSE, 0.2F);
		biomeMods.put(Type.WET, -1.5F);
		biomeMods.put(Type.DRY, -3F);
		biomeMods.put(Type.SAVANNA, -2F);
		biomeMods.put(Type.CONIFEROUS, +1F);
		biomeMods.put(Type.JUNGLE, 0F);
		biomeMods.put(Type.SPOOKY, 0F);
		biomeMods.put(Type.DEAD, -2F);
		biomeMods.put(Type.LUSH, 0F);
		biomeMods.put(Type.NETHER, -1F);
		biomeMods.put(Type.END, 0F);
		biomeMods.put(Type.MUSHROOM, 3F);
		biomeMods.put(Type.MAGICAL, 0F);
		biomeMods.put(Type.OCEAN, -4F);
		biomeMods.put(Type.RIVER, +1F);
		biomeMods.put(Type.MESA, +0F);
		biomeMods.put(Type.FOREST, 0.5F);
		biomeMods.put(Type.PLAINS, +0.5F);
		biomeMods.put(Type.MOUNTAIN, 0F);
		biomeMods.put(Type.HILLS, 0F);
		biomeMods.put(Type.SWAMP, 1.5F);
		biomeMods.put(Type.SANDY, -2F);
		biomeMods.put(Type.SNOWY, -2F);
		biomeMods.put(Type.WASTELAND, -1F);
		biomeMods.put(Type.BEACH, -1F);
	}
	@Override
	public int getCustomRenderType() {
		return 2;
	}
	@Override
	public float getRenderSizer(IBlockAccess world, int x, int y, int z) {
		// TODO Auto-generated method stub
		return (world.getBlockMetadata(x, y, z)+1F)/2F;
	}
}
