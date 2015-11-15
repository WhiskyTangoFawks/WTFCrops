package wtfcrops.blocks.customcrops;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import wtfcrops.Crops;
import wtfcrops.WTFCropsConfig;
import wtfcrops.blocks.customcrops.cropbases.CustomMushroomBase;
import wtfcrops.renderers.ICropRenderInfo;
import wtfcrops.renderers.RenderIDs;
import wtfcrops.utilities.BlockAndMeta;


public class CustomNetherWart extends CustomMushroomBase implements ICropRenderInfo{

	IIcon[] textures;
	
	public CustomNetherWart(int cultivar, int growthRate) {
		super(cultivar, Crops.cropNetherWart, Items.nether_wart);
		this.baseGrowthRate = WTFCropsConfig.growthRateNetherWart;
		float f = 0.5F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune){
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		if (metadata >= 7)//this is using 7 instead of max growth- if I use max growth later I need to change this
		{
			float growthModifier = getGrowthModifier(world, x, y, z);
			//adds the replacement seeds
			drops.add(new ItemStack(seed, 1, getSeedMetaToDrop(cultivar, random, growthModifier)));
			
			switch (cultivar){
			case Crops.wild://Wild: grows at 3x standard rate, drops: 1-3 seeds, 0/0/1 wheat
				drops = this.getWildDrops(drops, growthModifier);
				break;
			case Crops.simple://simple: grows at standard rate: 1-3 seeds, 1 wheat
				drops = this.getSimpleDrops(drops, growthModifier);
				break;
			case Crops.giant://giant grows at 1/3 standard rate, negative modifiers x2, 2x 1-2 seeds, 2-4 wheat
				drops = this.getGiantDrops(drops, growthModifier);
				break;
			case Crops.dwarf://dwarf: grows at standard rate, negative modifiers 1/2, 1-2 seeds, 1 wheat
				drops = this.getDwarfDrops(drops, growthModifier);
				break;
			case Crops.golden://golden: grows at 1/10th standard rate, 10x as much food or golden version
				drops = this.getGoldenDrops(drops, growthModifier);
				break;
			}
		}
		return drops;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected String getTextureName()
	{
		return "minecraft:nether_wart";
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)	{}//overridden to do nothing
	
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return meta >= 3 ? this.textures[2] : (meta > 0 ? this.textures[1] : this.textures[0]);
    }
    

    @Override
	public int getRenderType()
    {
        return RenderIDs.customCropRenderer;
    }
    
    @Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
        this.textures = new IIcon[3];

        for (int i = 0; i < this.textures.length; ++i)
        {
            this.textures[i] = iconregister.registerIcon("minecraft:nether_wart_stage_" + i);
        }
    }
   
    @Override
	public float getLightModifier(World world, int x, int y, int z){
    	//currently,it gets no bonus from light, but I coudl change this later
    	return 0F;
	}
       
	@Override
	protected void loadFarmlandHashMap(){
		//farmlandModifier.put(Blocks.farmland, +0F);
		farmlandModifier.put(new BlockAndMeta(Blocks.sand, 0), -2F);
		farmlandModifier.put(new BlockAndMeta(Blocks.sand, 1), -2F);
		//farmlandModifier.put(Blocks.dirt, -1F);
		//farmlandModifier.put(coarse dirt, 0F);
		//farmlandModifier.put(Crops.fertileFarmland, 1F);
		//farmlandModifier.put(Blocks.grass, -3F);
		farmlandModifier.put(new BlockAndMeta(Blocks.soul_sand, -2), 1F);

	}

	@Override
	protected void loadBiomeHashMap() {
		biomeMods.put(Type.HOT, +1F);
		biomeMods.put(Type.COLD, -4F);
		biomeMods.put(Type.SPARSE, -0.2F);
		biomeMods.put(Type.DENSE, -2F);
		biomeMods.put(Type.WET, -15F);
		biomeMods.put(Type.DRY, +1F);
		biomeMods.put(Type.SAVANNA, -2F);
		biomeMods.put(Type.CONIFEROUS, -2F);
		biomeMods.put(Type.JUNGLE, -2F);
		biomeMods.put(Type.SPOOKY, -2F);
		biomeMods.put(Type.DEAD, +1F);
		biomeMods.put(Type.LUSH, -1F);
		biomeMods.put(Type.NETHER, 2F);
		biomeMods.put(Type.END, +1F);
		biomeMods.put(Type.MUSHROOM, +1F);
		biomeMods.put(Type.MAGICAL, +1F);
		biomeMods.put(Type.OCEAN, -20F);
		biomeMods.put(Type.RIVER, -20F);
		biomeMods.put(Type.MESA, -0.5F);
		biomeMods.put(Type.FOREST, -0.2F);
		biomeMods.put(Type.PLAINS, +1F);
		biomeMods.put(Type.MOUNTAIN, -0.5F);
		biomeMods.put(Type.HILLS, -0.2F);
		biomeMods.put(Type.SWAMP, -15F);
		biomeMods.put(Type.SANDY, 0F);
		biomeMods.put(Type.SNOWY, -25F);
		biomeMods.put(Type.WASTELAND, +1F);
		biomeMods.put(Type.BEACH, -20F);
	}
	
	@Override
	public int getCustomRenderType() {
		return 0;
	}

	@Override
	public float getRenderSizer(IBlockAccess world, int x, int y, int z) {
		if (this.cultivar == Crops.dwarf){
			return 0.5F;
		}
		else if (this.cultivar == Crops.giant){
			return 1.5F;
		}
		return 1F;
	}


	
}
