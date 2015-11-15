package wtfcrops.blocks.customcrops;

import java.util.HashSet;

import com.google.common.collect.Sets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import wtfcrops.Crops;
import wtfcrops.blocks.customcrops.cropbases.CustomCrop;
import wtfcrops.blocks.customcrops.cropbases.CustomStalkBase;

public class CustomPumpkinStalk extends CustomStalkBase{

	public CustomPumpkinStalk(int baseGrowthRate, CustomCrop parentCrop) {
		super(baseGrowthRate, parentCrop);
		// TODO Auto-generated constructor stub
	}

	protected void growGourd(World world, int x, int y, int z) {
		//check to see what, if anything, is attached already
		//grow a gourd or leaves
		//grow mostly simple ones, with a small chance for another giant one
		
		world.setBlock(x-1, y, z, Crops.blockPumpkin[this.parentCrop.getSeedMetaToDrop(1, parentCrop.random, this.getGrowthModifier(world, x, y, z))]);
		
	}
	

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister)
	{
		this.blockIcon = iconregister.registerIcon("minecraft:log_acacia");
		this.top = iconregister.registerIcon("minecraft:log_acacia_top");
		
	}
	
}
