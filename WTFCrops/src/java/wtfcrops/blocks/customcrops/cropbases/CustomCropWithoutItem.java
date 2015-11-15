package wtfcrops.blocks.customcrops.cropbases;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import wtfcore.WTFCore;
import wtfcrops.WTFCropsConfig;
import wtfcrops.renderers.ICropRenderInfo;

public class CustomCropWithoutItem extends CustomCrop implements ICropRenderInfo{

	public Block[] crop;

	public CustomCropWithoutItem(int cultivar, Block[] crop, Item seed, Item food){
		super(cultivar, seed, food);
		//this.setCreativeTab(WTFCrops.WTFCrops);
		
		this.maxGrowth=7; // this should be overriden in the child class
		this.crop = crop;
	}
	public boolean shouldGrow(World world, Random random, int x, int y, int z){
		float growthModifier = getGrowthModifier(world, x, y, z);
		//Growth is determined by whether a random number is beneath the config set threshold * cultivar modifier
		//so, making randomGrowth range smaller, increases the chance of plant growth
		int randomGrowth = MathHelper.ceiling_float_int(500/growthModifier);
		//WTFCore.log.info("Random growth rate " + randomGrowth));
		if (random.nextInt(randomGrowth) < baseGrowthRate*getCultivarModifier()){
			return true;
		}
		//if a random float is less than 2x the growth modifier, kill the plant
		else if (WTFCropsConfig.killPlants && random.nextFloat() > growthModifier*3){
			world.setBlockToAir(x,y,z);
		}
		return false;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (!world.isRemote){

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
					spawnNearby(world, random, x, y, z, meta);
				}
			}
		}
	}

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

		ChunkPosition chunkposition = arraylist.get(world.rand.nextInt(arraylist.size()));
		Block blockToSet = crop[getSeedMetaToDrop(cultivar, random, getGrowthModifier(world, x, y, z))];
		world.setBlock(chunkposition.chunkPosX, chunkposition.chunkPosY, chunkposition.chunkPosZ, blockToSet, 0, 2);
		//replace with type getter
	}


	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_){
		this.blockIcon = p_149651_1_.registerIcon(this.getTextureName());
	}    
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_){
		return this.blockIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z){
		return this.food;
	}

	@Override
	public int getCustomRenderType() {
		return 2;
	}

	@Override
	public float getRenderSizer(IBlockAccess world, int x, int y, int z) {
		return 1F;
	}


}