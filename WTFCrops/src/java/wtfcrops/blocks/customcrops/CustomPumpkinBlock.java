package wtfcrops.blocks.customcrops;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import wtfcrops.Crops;
import wtfcrops.WTFCrops;
import wtfcrops.blocks.customcrops.cropbases.CustomCropStem;
import wtfcrops.blocks.customcrops.cropbases.CustomGourdBase;

public class CustomPumpkinBlock extends CustomGourdBase{

	public IIcon orangeSide;
	public IIcon orangeTop;

	
	public CustomPumpkinBlock(int cultivar) {
		super(cultivar);
		//this.setCreativeTab(WTFCrops.WTFCrops);
		this.stem = (CustomCropStem) Crops.cropPumpkinStem[cultivar];
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister)
	{
		this.orangeSide = iconregister.registerIcon("minecraft:pumpkin_side");
		this.orangeTop = iconregister.registerIcon("minecraft:pumpkin_top");

	} 
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (side < 2) {return this.orangeTop;}
		return  this.orangeSide;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int meta)
	{
		if (meta  < 2){
			int j = meta * 32;
			int k = 255 - meta * 8;
			int l = meta * 4;
			return j << 16 | k << 8 | l;
		}
		return 16777215;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, int x, int y, int z)
	{
		//need a sidestep for 14/15
		int meta = world.getBlockMetadata(x, y, z);
		int growthStage = 2;
		if (meta <14){
			growthStage = func_149987_c(meta);	
		}
		return this.getRenderColor(growthStage);
	}
	
	public boolean shouldGrow(World world, Random random, int x, int y, int z){

		int ybase=y;
		int ytop=y;
		while (world.getBlock(x, ybase-1, z)instanceof CustomPumpkinStalk){
			ybase--;
		}
		while (world.getBlock(x, ytop+1, z)instanceof CustomPumpkinStalk){
			ytop++;
		}
		float growthModifier = stem.getSplitGrowthModifier(world, x, ybase, ytop, z);
		int randomGrowth = MathHelper.ceiling_float_int(235/growthModifier);
		if (random.nextInt(randomGrowth) < stem.baseGrowthRate*stem.getCultivarModifier()){
			return true;
		}
		//if a random float is less than 2x the growth modifier, kill the plant
		else if (random.nextFloat() > growthModifier*2){
			world.setBlockToAir(x,y,z);
		}
		return false;
	}
	
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		int j1 = func_149987_c(world.getBlockMetadata(x, y, z));
		if (j1 < this.maxGrowth){

			int l = getDirection(world.getBlockMetadata(x, y, z));
			x += Direction.offsetX[l];
			z += Direction.offsetZ[l];
			Block block = world.getBlock(x, y, z);
			return block == Crops.pumpkinStalk || block instanceof CustomPumpkinStem;	
		}
		return true;
	}
    
	@Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int p_149690_5_, int fortune)
    {
        ArrayList<ItemStack> dropped = new ArrayList<ItemStack>();
        int j1 = func_149987_c(p_149690_5_);

        if (j1 >= this.maxGrowth)
        {     
        	dropped.add(new ItemStack(Crops.itemPumpkin, 1, cultivar));
        }
        return dropped;
    }
	
}
