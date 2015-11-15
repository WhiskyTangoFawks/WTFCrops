package wtfcrops.blocks.customcrops;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import wtfcore.WTFCore;
import wtfcrops.Crops;
import wtfcrops.blocks.customcrops.cropbases.CustomMushroomBase;

public class CustomMushroomRed extends CustomMushroomBase{

	public CustomMushroomRed(int cultivar) {
		super(cultivar, Crops.cropMushroomred, Crops.redMushroom);
	}
	@Override
	@SideOnly(Side.CLIENT)
	protected String getTextureName()
	{
		return "minecraft:mushroom_red";
	}
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune){
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(Crops.redMushroom, 1, cultivar));
		return drops;
	}	
	
	@Override
	protected void checkAndDropBlock(World world, int x, int y, int z)
	{
		if (!this.canBlockStay(world, x, y, z))
		{
			if (world.isAirBlock(x, y-1, z)){
				this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
				world.setBlockToAir(x, y, z);
			}
		}
	}
}
