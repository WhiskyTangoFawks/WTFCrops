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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import wtfcrops.Crops;
import wtfcrops.renderers.ICropRenderInfo;
import wtfcrops.renderers.RenderIDs;

public class CustomCropStem extends CustomCrop implements ICropRenderInfo{

	public IIcon field_149876_b;
	public Block[] gourd;
	public Block stalk;

	public CustomCropStem(int cultivar, Block[] gourd, Block stalk, Item seed, Item food) {
		super(cultivar, seed, food);
		//this.setCreativeTab(WTFCrops.WTFCrops);
		this.gourd = gourd;
		this.stalk = stalk;
		float f = 0.125F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister)
	{
		this.blockIcon = iconregister.registerIcon(this.getTextureName() + "_disconnected");
		this.field_149876_b = iconregister.registerIcon(this.getTextureName() + "_connected");
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if(!world.isRemote){
			this.checkAndDropBlock(world, x, y, z);

			if (world.getBlockLightValue(x, y + 1, z) >= 9) {
				int meta = world.getBlockMetadata(x, y, z);
				if (meta < 7) {
					//ShouldGrow contains the method to kill the plant in poor conditions
					if (shouldGrow(world, random, x, y, z)) {
						growCrop(world, x, y, z, meta);
					}
				}
				else {
					if (this.cultivar ==Crops.giant){
						growStalk(world, x,y,z);
					}
					else if (getState(world, x, y, z) == -1 && shouldGrow(world, random, x, y, z)){
						growGourd(world, x, y, z);
					}
				}
			}
		}
	}

	public void growStalk(World world, int x, int y, int z){
		world.setBlock(x, y, z, stalk, 2, 2);
		world.setBlock(x, y+1, z, stalk, 1, 2);
		world.setBlock(x, y+1, z, stalk, 0, 2);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune){
		//Stems no longer drop seeds- you have to get them from the plant
		return new ArrayList<ItemStack>();
	}

	public void growGourd(World world, int x, int y, int z){
		//this only sets a gourd in the world, the code to increment it's growth is in the gourd class itself
		float growthMod = getGrowthModifier(world, x, y, z);
		int meta = -1;
		if (getState(world, x, y, z) == -1)	{


			switch (world.rand.nextInt(4)){
			case 0: x--;meta=3;break;
			case 1: x++;meta=1;break;
			case 2: z++;meta=2;break;
			case 3: z--;meta=0;break;
			}

			if (world.isAirBlock(x, y, z)){
				world.setBlock(x, y, z, this.gourd[this.getSeedMetaToDrop(cultivar, random, growthMod)], meta, 2);
			}
		}
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
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_)
	{
		return this.getRenderColor(p_149720_1_.getBlockMetadata(p_149720_2_, p_149720_3_, p_149720_4_));
	}


	@Override
	public void setBlockBoundsForItemRender()
	{
		float f = 0.125F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
	{
		this.maxY = (p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_) * 2 + 2) / 16.0F;
		float f = 0.125F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, (float)this.maxY, 0.5F + f);
	}

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType()
	{
		return RenderIDs.customCropRenderer;
	}

	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_)
	{
		return this.blockIcon;
	}

	/**
	 * Returns the current state of the stem. Returns -1 if the stem is not fully grown, or a value between 0 and 3
	 * based on the direction the stem is facing.
	 */


	public int getState(IBlockAccess world, int x, int y, int z)
	{
		//this method mustbe overridden in the extending class
		return -1;
	}



	@Override
	public float getLightModifier(World world, int x, int y, int z){
		int lightValue = world.getBlockLightValue(x, y, z);
		return (lightValue - 7)/7;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getStemIcon()
	{
		return this.field_149876_b;
	}

	@Override
	public int getCustomRenderType() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public float getRenderSizer(IBlockAccess world, int x, int y, int z) {
		// TODO Auto-generated method stub
		return 3F;
	}


}
