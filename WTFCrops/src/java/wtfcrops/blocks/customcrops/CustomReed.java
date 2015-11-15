package wtfcrops.blocks.customcrops;

import java.util.ArrayList;
import java.util.Random;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.BiomeDictionary.Type;
import wtfcrops.Crops;
import wtfcrops.WTFCropsConfig;
import wtfcrops.blocks.customcrops.cropbases.CustomCropWithoutItem;
import wtfcrops.utilities.BlockAndMeta;

public class CustomReed extends CustomCropWithoutItem{

	int maxHeight;
	boolean spreads = false;

	public CustomReed(int cultivar) {
		super(cultivar, Crops.cropSugarCane, Items.reeds, Items.reeds);
		this.baseGrowthRate = WTFCropsConfig.growthRateSugar;
		float f = 0.375F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
		this.maxGrowth = 7;

		switch (cultivar){
		case Crops.wild://Wild: grows at 3x standard rate, drops: 1-3 seeds, 0/0/1 wheat
			spreads = true;
			maxHeight = 2;
			maxGrowth = 3;break;
		case Crops.simple://simple: grows at standard rate: 1-3 seeds, 1.5 wheat
			spreads = false;
			maxHeight = 3;
			maxGrowth = 7;break;
		case Crops.giant://giant grows at 1/3 standard rate, negative modifiers x2, 2x 1-2 seeds, 2-4 wheat
			spreads = false;
			maxHeight = 5;
			maxGrowth = 12;
		case Crops.dwarf://dwarf: grows at standard rate, negative modifiers 1/2, 1-2 seeds, 1.5 wheat
			spreads = false;
			maxHeight = 1;
			maxGrowth = 5;
		case Crops.golden://golden: grows at 1/10th standard rate, 10x as much food or golden version
			this.setLightLevel(0.2F);
			spreads = false;
			maxHeight = 4;
			maxGrowth = 15;
		}
	}

	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_)
	{
		return this.blockIcon;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		this.checkAndDropBlock(world, x, y, z);

		if (world.getBlockLightValue(x, y + 1, z) >= 9) {
			int meta = world.getBlockMetadata(x, y, z);
			if (meta < maxGrowth+1) {
				//ShouldGrow contains the method to kill the plant in poor conditions
				if (shouldGrow(world, random, x, y, z)) {
					growCrop(world, x, y, z, meta);
				}
			}
			else {
				//spread sideways goes here
				if (spreads){
					spawnNearby(world, random, x, y, z, meta);
				}
			}
		}
	}

	@Override
	public void growCrop(World world, int x, int y, int z, int meta){
		if (world.getBlockMetadata(x,y,z)<this.maxGrowth || !world.isAirBlock(x,  y+1, z)){
			world.setBlockMetadataWithNotify(x, y, z, meta+1, 2);
		}
		else{
			int loop = 1;
			while (world.getBlock(x, y-loop, z) == this){
				loop++;
			}
			if(loop < this.maxHeight){
				world.setBlock(x, y+1, z, this);
			}
		}
	}

	@Override
	public void spawnNearby(World world, Random random, int x, int y, int z, int meta){
		//use food.cultivars[cultivar] to get the block to place
		ArrayList<ChunkPosition> arraylist = new ArrayList<ChunkPosition>();
		for (int xloop = -1; xloop < 2; xloop++){
			for (int zloop = -1; zloop < 2; zloop++){
				for (int yloop = 2; yloop > -2 || world.isAirBlock(xloop, y+yloop, z); yloop--){
					if (canBlockStay(world, x+xloop, y+yloop+1, z+zloop)){
						arraylist.add(new ChunkPosition(x+xloop, y+yloop+1, z+zloop));
					}
				}
			}
		}
		ChunkPosition chunkposition = arraylist.get(random.nextInt(arraylist.size()));
		world.setBlock(chunkposition.chunkPosX, chunkposition.chunkPosY, chunkposition.chunkPosZ, crop[this.getSeedMetaToDrop(z, random, getGrowthModifier(world, x, y, z))]);
	}
	
	
	
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune){
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(seed, 1, cultivar));
		return drops;
	}	

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_){
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected String getTextureName()
	{
		return "minecraft:reeds";
	}
	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_)
	{
		return p_149720_1_.getBiomeGenForCoords(p_149720_2_, p_149720_4_).getBiomeGrassColor(p_149720_2_, p_149720_3_, p_149720_4_);
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{
		return EnumPlantType.Beach;
	}

	@Override
	public Block getPlant(IBlockAccess world, int x, int y, int z)
	{
		return this;
	}

	@Override
	public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z);
	}

	@Override
	protected void loadFarmlandHashMap(){
		float baseFarmland = 3F;//this should be the base bonus you get for putting it on farmland- including the bonus for having perfect hydration
		float badHydration = .75F;//this gets multiplied by the hydration value of the plant- it tells you how sensitive the plant is to the wrong hydration	
		int idealHydration = 15; //the hydration value where the plant is happiest, 0-15, 0 is dry, 15 is wet
		float fertaliserBonus = 1f; //bonus the fertalised version of the soil gets

		for (int loop = 0; loop < 16; loop ++){
			float hydroValue = (loop-idealHydration)*badHydration; 
			float hydrationPenalty = hydroValue*hydroValue; 
			farmlandModifier.put(new BlockAndMeta(Blocks.farmland, loop), baseFarmland-hydrationPenalty); 
			farmlandModifier.put(new BlockAndMeta(Crops.fertileFarmland, loop),  fertaliserBonus+baseFarmland-hydrationPenalty);
		}

		farmlandModifier.put(new BlockAndMeta(Blocks.sand, 0), 0F);
		farmlandModifier.put(new BlockAndMeta(Blocks.sand, 1), -0.25F);//red sand

		farmlandModifier.put(new BlockAndMeta(Blocks.dirt, 0), -2F);
		farmlandModifier.put(new BlockAndMeta(Blocks.dirt, 1), -2F);//coarse dirt
		farmlandModifier.put(new BlockAndMeta(Blocks.dirt, 2), -2F);//podzol


		//farmlandModifier.put(new BlockAndMeta(Blocks.grass, 0), -3F);
	}

	@Override
	protected void loadBiomeHashMap() {
		biomeMods.put(Type.HOT, +1F);
		biomeMods.put(Type.COLD, -3F);
		biomeMods.put(Type.SPARSE, -0.2F);
		biomeMods.put(Type.DENSE, 0.2F);
		biomeMods.put(Type.SAVANNA, -0.5F);
		biomeMods.put(Type.CONIFEROUS, -1F);
		biomeMods.put(Type.JUNGLE, 0F);
		biomeMods.put(Type.SPOOKY, 0F);
		biomeMods.put(Type.DEAD, -5F);
		biomeMods.put(Type.LUSH, +1F);
		biomeMods.put(Type.NETHER, -5F);
		biomeMods.put(Type.END, 0F);
		biomeMods.put(Type.MUSHROOM, 0F);
		biomeMods.put(Type.MAGICAL, 0F);
		biomeMods.put(Type.OCEAN, 0F);
		biomeMods.put(Type.RIVER, +2F);
		biomeMods.put(Type.MESA, -1F);
		biomeMods.put(Type.FOREST, -0.2F);
		biomeMods.put(Type.PLAINS, -1F);
		biomeMods.put(Type.MOUNTAIN, -3F);
		biomeMods.put(Type.HILLS, -2F);
		biomeMods.put(Type.SWAMP, 0F);
		biomeMods.put(Type.SANDY, 0F);
		biomeMods.put(Type.SNOWY, -5F);
		biomeMods.put(Type.WASTELAND, -2F);
		biomeMods.put(Type.BEACH, +1F);
	}
}
