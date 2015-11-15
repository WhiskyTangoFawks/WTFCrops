package wtfcrops.blocks.customcrops;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.BiomeDictionary.Type;
import wtfcrops.Crops;
import wtfcrops.WTFCropsConfig;
import wtfcrops.blocks.customcrops.cropbases.CustomCrop;
import wtfcrops.utilities.BlockAndMeta;

public class CustomCarrot extends CustomCrop{

	protected IIcon[] texture;
	
	public CustomCarrot(int cultivar) {
		super(cultivar, Items.carrot, Items.carrot);
		this.baseGrowthRate = WTFCropsConfig.growthRateCarrot;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected String getTextureName()
	{
		return "minecraft:carrots";
	}

	@Override
	public ArrayList<ItemStack> getGoldenDrops(ArrayList<ItemStack> drops, float growthModifier){
		
		if (random.nextInt(5) < growthModifier){
			drops.add(new ItemStack(Items.golden_carrot, random.nextInt(2)+1));
		}
		else{
			drops.add(new ItemStack(seed, 1, getSeedMetaToDrop(Crops.simple, random, growthModifier)));
		}
		return drops;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (meta < 7)
		{
			if (meta == 6)
			{
				meta = 5;
			}

			return this.texture[meta >> 1];
		}
		else
		{
			return this.texture[3];
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_)
	{
		this.texture = new IIcon[4];

		for (int i = 0; i < this.texture.length; ++i)
		{
			this.texture[i] = p_149651_1_.registerIcon(this.getTextureName() + "_stage_" + i);
		}
	}


	@Override
	protected void loadFarmlandHashMap(){
		float baseFarmland = 1.5F;//this should be the base bonus you get for putting it on farmland- including the bonus for having perfect hydration
		float badHydration = .25F;//this gets multiplied by the hydration value of the plant- it tells you how sensitive the plant is to the wrong hydration	
		int idealHydration = 6; //the hydration value where the plant is happiest, 0-15, 0 is dry, 15 is wet
		float fertaliserBonus = 1f; //bonus the fertalised version of the soil gets
		
		for (int loop = 0; loop < 16; loop ++){
			float hydroValue = (loop-idealHydration)*badHydration; 
			float hydrationPenalty = hydroValue*hydroValue; 
			farmlandModifier.put(new BlockAndMeta(Blocks.farmland, loop), baseFarmland-hydrationPenalty); 
			farmlandModifier.put(new BlockAndMeta(Crops.fertileFarmland, loop),  fertaliserBonus+baseFarmland-hydrationPenalty);
		}
		
		farmlandModifier.put(new BlockAndMeta(Blocks.sand, 0), -4F);
		farmlandModifier.put(new BlockAndMeta(Blocks.sand, 1), -4F);//red sand
		
		farmlandModifier.put(new BlockAndMeta(Blocks.dirt, 0), -1.5F);
		farmlandModifier.put(new BlockAndMeta(Blocks.dirt, 1), -1.5F);//coarse dirt
		farmlandModifier.put(new BlockAndMeta(Blocks.dirt, 2), -1.5F);//podzol
		
		
		farmlandModifier.put(new BlockAndMeta(Blocks.grass, 0), -3F);
		farmlandModifier.put(new BlockAndMeta(Blocks.mycelium, 0), -10F);;
	}

	@Override
	protected void loadBiomeHashMap() {
		biomeMods.put(Type.HOT, -0.3F);
		biomeMods.put(Type.COLD, -.2F);
		biomeMods.put(Type.SPARSE, 0F);
		biomeMods.put(Type.DENSE, 0.2F);
		biomeMods.put(Type.SAVANNA, 0.2F);
		biomeMods.put(Type.CONIFEROUS, 1F);
		biomeMods.put(Type.JUNGLE, -.1F);
		biomeMods.put(Type.SPOOKY, 0F);
		biomeMods.put(Type.DEAD, -5F);
		biomeMods.put(Type.LUSH, -.1F);
		biomeMods.put(Type.NETHER, -5F);
		biomeMods.put(Type.END, 0F);
		biomeMods.put(Type.MUSHROOM, 0F);
		biomeMods.put(Type.MAGICAL, 0F);
		biomeMods.put(Type.OCEAN, -3F);
		biomeMods.put(Type.RIVER, 0F);
		biomeMods.put(Type.MESA, -.5F);
		biomeMods.put(Type.FOREST, +1F);
		biomeMods.put(Type.PLAINS, +0.2F);
		biomeMods.put(Type.MOUNTAIN, 0.5F);
		biomeMods.put(Type.HILLS, 0.2F);
		biomeMods.put(Type.SWAMP, -2F);
		biomeMods.put(Type.SANDY, -1F);
		biomeMods.put(Type.SNOWY, -0.5F);
		biomeMods.put(Type.WASTELAND, -1F);
		biomeMods.put(Type.BEACH, -1F);
	}


}
