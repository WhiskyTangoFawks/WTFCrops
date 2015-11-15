package wtfcrops.blocks.customcrops;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import wtfcore.WTFCore;
import wtfcrops.Crops;
import wtfcrops.blocks.customcrops.cropbases.CustomMushroomBase;

public class CustomMushroomBrown extends CustomMushroomBase{

	public CustomMushroomBrown(int cultivar) {
		super(cultivar, Crops.cropMushroombrown, Crops.brownMushroom);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected String getTextureName()
	{
		return "minecraft:mushroom_brown";
	}
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune){
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(Crops.brownMushroom, 1, cultivar));
		return drops;
	}	
}
