package wtfcrops.blocks.customcrops.cropbases;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import wtfcrops.WTFCrops;

public class CustomStalkBase extends BlockBush{

	protected CustomCrop parentCrop;
	public int baseGrowthRate;
	public IIcon top;

	public CustomStalkBase(int baseGrowthRate, CustomCrop parentCrop) {
		super(Material.wood);
		this.setHardness(0.8F);
		this.setCreativeTab(WTFCrops.WTFCrops);
		//this.setStepSound(Type.)
		this.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
		this.setTickRandomly(true);
		this.baseGrowthRate = baseGrowthRate;
		this.parentCrop = parentCrop;
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		return  world.getBlock(x, y - 1, z) == this || this.parentCrop.canPlaceBlockOn(world.getBlock(x, y - 1, z));
	}


	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (!world.isRemote){
			this.checkAndDropBlock(world, x, y, z);

			//check if it's the top block
			int meta = world.getBlockMetadata(x, y, z);
			if (world.isAirBlock(x,y+1,z)) {//is this the top block

				if (meta < 8 && shouldGrow(world, random, x, y, z)) { //if less than 8, should it increment the metadata
					growCrop(world, x, y, z, meta);
				}
				if (meta > 3 && shouldGrow(world, random, x, y, z)){
					world.setBlock(x, y+1, z, this, 0, 2);
				}
			}
			else if (meta > 5  && world.getBlock(x, y+1,  z)==this && world.getBlock(x, y+2,z)==this) {
				growGourd(world,x,y,z);
			}
		}
	}

	protected void growGourd(World world, int x, int y, int z) {
		//overriden in extending class
	}
	public boolean shouldGrow(World world, Random random, int x, int y, int z){
		float growthModifier = getGrowthModifier(world, x, y, z);
		//Growth is determined by whether a random number is beneath the config set threshold * cultivar modifier
		//so, making randomGrowth range smaller, increases the chance of plant growth
		int randomGrowth = MathHelper.ceiling_float_int(235/growthModifier);
		//WTFCore.log.info("Random growth rate " + randomGrowth));
		if (random.nextInt(randomGrowth) < baseGrowthRate*parentCrop.getCultivarModifier()){
			return true;
		}
		//if a random float is less than 2x the growth modifier, kill the plant
		//else if (random.nextFloat() > growthModifier*2){
		//	world.setBlockToAir(x,y,z);
		//}
		return false;
	}

	public float getGrowthModifier(World world, int x, int y, int z){
		int ytop=y+1;
		int ybase=y-1;
		while(world.getBlock(x, ytop, z)==this){
			ytop++;
		}
		while(world.getBlock(x, ybase, z)==this){
			ybase--;
		}
		float modifier = 1;
		modifier += parentCrop.getBiomeModifier(world, x, z);
		modifier += parentCrop.getFarmlandModifier(world, x, ybase, z);
		modifier += parentCrop.getLightModifier(world, x, ytop, z);
		//elevation
		//Scarecrow- get distance to nearest scarecrow
		//if IsRaioning
		//if isSnowing

		if (modifier < 1){
			modifier = 1/(modifier-1) * -1;
		}
		//WTFCore.log.info("growth modifier " + modifier);
		return modifier;
	}

	@Override
	protected void checkAndDropBlock(World world, int x, int y, int z)
	{
		if (!this.canBlockStay(world, x, y, z))
		{
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlockToAir(x, y, z);
		}
	}

	public void growCrop(World world, int x, int y, int z, int meta){
		world.setBlockMetadataWithNotify(x, y, z, meta+1, 2);
	}


	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_)
	{
		return this.getRenderColor(p_149720_1_.getBlockMetadata(p_149720_2_, p_149720_3_, p_149720_4_));
	}
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int p_149741_1_)
	{
		int j = p_149741_1_ * 32;
		int k = 255 - p_149741_1_ * 8;
		int l = p_149741_1_ * 4;
		return j << 16 | k << 8 | l;
	}
	@Override
	public int getRenderType()
	{
		return 0;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (side < 2) {return this.top;}
		return  this.blockIcon;
	}
}
