package wtfcrops.blocks.customcrops;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import wtfcrops.Crops;
import wtfcrops.blocks.customcrops.cropbases.CustomCropStem;
import wtfcrops.blocks.customcrops.cropbases.CustomGourdBase;

public class CustomMelonBlock extends CustomGourdBase{

	Random random = new Random();
	IIcon side;
	IIcon top;

	public CustomMelonBlock(int cultivar) {
		super(cultivar);
		//this.setCreativeTab(WTFCrops.WTFCrops);
		this.stem = (CustomCropStem) Crops.cropMelonStem[cultivar];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister)
	{
		this.side = iconregister.registerIcon("minecraft:melon_side");
		this.top = iconregister.registerIcon("minecraft:melon_top");

	} 

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (side < 2) {return this.top;}
		return  this.side;
	}

	@Override
	public boolean shouldGrow(World world, Random random, int x, int y, int z){

		int ybase=y;
		int ytop=y;
		while (world.getBlock(x, ybase-1, z)instanceof CustomMelonStalk){
			ybase--;
		}
		while (world.getBlock(x, ytop+1, z)instanceof CustomMelonStalk){
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

	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		int j1 = func_149987_c(world.getBlockMetadata(x, y, z));
		if (j1 < this.maxGrowth){

			int l = getDirection(world.getBlockMetadata(x, y, z));
			x += Direction.offsetX[l];
			z += Direction.offsetZ[l];
			Block block = world.getBlock(x, y, z);
			return block == Crops.melonStalk || block instanceof CustomMelonStem;	
		}
		return true;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune)
	{
		ArrayList<ItemStack> dropped = new ArrayList<ItemStack>();
		int size = func_149987_c(meta) + 1;
		if (size == maxGrowth){
			size = size+random.nextInt(size);
		}
		
		switch (cultivar){
		//change the number of drops for each
		//I have to add seeds, and remove the recipe for melon slices into seeds, because it completely shortcuts my thing
		//or I can replace melon slices with a custom, metadata sensitive food item- which probably is the best match for vanilla behaviour
		case Crops.wild://Wild: grows at 3x standard rate, drops: 1-3 seeds, 0/0/1 wheat
			for (int loop = 0; loop < size; loop++)
			{     
				if (random.nextBoolean()){
					dropped.add(new ItemStack(Items.melon, 1, stem.getSeedMetaToDrop(cultivar, random, stem.getGrowthModifier(world, x, y, z))));
				}
				else {
					dropped.add(new ItemStack(Items.melon_seeds, 1, stem.getSeedMetaToDrop(cultivar, random, stem.getGrowthModifier(world, x, y, z))));
				}
			}
			break;
		case Crops.simple://simple: grows at standard rate: 1-3 seeds, 1.5 wheat
			for (int loop = 0; loop < size; loop++)
			{     
				dropped.add(new ItemStack(Items.melon, 1, stem.getSeedMetaToDrop(cultivar, random, stem.getGrowthModifier(world, x, y, z))));
			}
			break;

		case Crops.giant://giant grows at 1/3 standard rate, negative modifiers x2, 2x 1-2 seeds, 2-4 wheat
			for (int loop = 0; loop < size; loop++)
			{     
				dropped.add(new ItemStack(Items.melon, 1, stem.getSeedMetaToDrop(cultivar, random, stem.getGrowthModifier(world, x, y, z))));
			}
			break;

		case Crops.dwarf://dwarf: grows at standard rate, negative modifiers 1/2, 1-2 seeds, 1.5 wheat
			for (int loop = 0; loop < size; loop++)
			{     
				dropped.add(new ItemStack(Items.melon, 1, stem.getSeedMetaToDrop(cultivar, random, stem.getGrowthModifier(world, x, y, z))));
			}
			break;

		case Crops.golden://golden: grows at 1/10th standard rate, 10x as much food or golden version
			if (random.nextBoolean()){
				dropped.add(new ItemStack(Items.speckled_melon, 1, 0));
			}
			else {
				dropped.add(new ItemStack(Items.melon_seeds, 1, stem.getSeedMetaToDrop(cultivar, random, stem.getGrowthModifier(world, x, y, z))));
			}
			for (int loop = 0; loop < size; loop++)
			{     
				dropped.add(new ItemStack(Items.melon, 1, 1));
			}
			break;
		}
		return dropped;
	
	}

}
