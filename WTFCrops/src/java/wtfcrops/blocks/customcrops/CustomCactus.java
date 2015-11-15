package wtfcrops.blocks.customcrops;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import wtfcrops.Crops;
import wtfcrops.blocks.customcrops.cropbases.CustomCropWithoutItem;
import wtfcrops.utilities.BlockAndMeta;

public class CustomCactus extends CustomCropWithoutItem{

	int maxHeight;
	boolean spreads = false;
	
	public CustomCactus(int cultivar) {
		super(cultivar, Crops.cropCactus, Crops.itemCactus, Crops.itemCactus);
		switch (cultivar){
		case Crops.wild://Wild: grows at 3x standard rate, drops: 1-3 seeds, 0/0/1 wheat
			spreads = true;
			maxHeight = 2;
			maxGrowth = 3;break;
		case Crops.simple://simple: grows at standard rate: 1-3 seeds, 1.5 wheat
			spreads = false;
			maxHeight = 3;
			maxGrowth = 7;break;
		case Crops.giant://giant grows at 1/3 standard rate, negative modifiers x2, 2x 1-2 seeds, 2-4 wheat
			spreads = false;
			maxHeight = 5;
			maxGrowth = 12;
		case Crops.dwarf://dwarf: grows at standard rate, negative modifiers 1/2, 1-2 seeds, 1.5 wheat
			spreads = false;
			maxHeight = 1;
			maxGrowth = 5;
		case Crops.golden://golden: grows at 1/10th standard rate, 10x as much food or golden version
			this.setLightLevel(0.2F);
			spreads = false;
			maxHeight = 4;
			maxGrowth = 15;
		}
	}

	private IIcon top;
	private IIcon bottom;


	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
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
				if (spreads){
					spawnNearby(world, random, x, y, z, meta);
				}
			}
		}
	}

	@Override
	public void growCrop(World world, int x, int y, int z, int meta){
		if (world.getBlockMetadata(x,y,z)<this.maxGrowth || !world.isAirBlock(x,  y+1, z)){
			world.setBlockMetadataWithNotify(x, y, z, meta+1, 2);
		}
		else{
			int loop = 1;
			while (world.getBlock(x, y-loop, z) == this){
				loop++;
			}
			if(loop < this.maxHeight){
				world.setBlock(x, y+1, z, this);
			}
		}
	}

	@Override
	public void spawnNearby(World world, Random random, int x, int y, int z, int meta){
		//use food.cultivars[cultivar] to get the block to place
		ArrayList<ChunkPosition> arraylist = new ArrayList<ChunkPosition>();
		for (int xloop = -1; xloop < 2; xloop++){
			for (int zloop = -1; zloop < 2; zloop++){
				for (int yloop = 2; yloop > -2 || world.isAirBlock(xloop, y+yloop, z); yloop--){
					if (canBlockStay(world, x+xloop, y+yloop+1, z+zloop)){
						arraylist.add(new ChunkPosition(x+xloop, y+yloop+1, z+zloop));
					}
				}
			}
		}
		ChunkPosition chunkposition = arraylist.get(random.nextInt(arraylist.size()));
		world.setBlock(chunkposition.chunkPosX, chunkposition.chunkPosY, chunkposition.chunkPosZ, crop[this.getSeedMetaToDrop(z, random, getGrowthModifier(world, x, y, z))]);
	}
	
	
	
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune){
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(seed, 1, cultivar));
		return drops;
	}	
	
	 /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        float f = 0.0625F;
        return AxisAlignedBB.getBoundingBox((double)((float)p_149668_2_ + f), (double)p_149668_3_, (double)((float)p_149668_4_ + f), (double)((float)(p_149668_2_ + 1) - f), (double)((float)(p_149668_3_ + 1) - f), (double)((float)(p_149668_4_ + 1) - f));
    }

    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        float f = 0.0625F;
        return AxisAlignedBB.getBoundingBox((double)((float)x + f), (double)y, (double)((float)z + f), (double)((float)(x + 1) - f), (double)(y + 1), (double)((float)(z + 1) - f));
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
       
    	return side == 1 ? this.top : (side == 0 ? this.bottom : this.blockIcon);
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 13;
    }
    
    public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_, Entity p_149670_5_)
    {
        p_149670_5_.attackEntityFrom(DamageSource.cactus, 1.0F);
    }

    @SideOnly(Side.CLIENT)
    protected String getTextureName()
    {
        return "minecraft:cactus";
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon(this.getTextureName() + "_side");
        this.top = p_149651_1_.registerIcon(this.getTextureName() + "_top");
        this.bottom = p_149651_1_.registerIcon(this.getTextureName() + "_bottom");
    }
    
	@Override
	protected void loadFarmlandHashMap(){
		float baseFarmland = 3F;//this should be the base bonus you get for putting it on farmland- including the bonus for having perfect hydration
		float badHydration = .75F;//this gets multiplied by the hydration value of the plant- it tells you how sensitive the plant is to the wrong hydration	
		int idealHydration = 0; //the hydration value where the plant is happiest, 0-15, 0 is dry, 15 is wet
		float fertaliserBonus = 1f; //bonus the fertalised version of the soil gets

		for (int loop = 0; loop < 16; loop ++){
			float hydroValue = (loop-idealHydration)*badHydration; 
			float hydrationPenalty = hydroValue*hydroValue; 
			farmlandModifier.put(new BlockAndMeta(Blocks.farmland, loop), baseFarmland-hydrationPenalty); 
			farmlandModifier.put(new BlockAndMeta(Crops.fertileFarmland, loop),  fertaliserBonus+baseFarmland-hydrationPenalty);
		}

		farmlandModifier.put(new BlockAndMeta(Blocks.sand, 0), 1F);
		farmlandModifier.put(new BlockAndMeta(Blocks.sand, 1), 1F);//red sand

		farmlandModifier.put(new BlockAndMeta(Blocks.grass, 0), -1.5F);
		farmlandModifier.put(new BlockAndMeta(Blocks.dirt, 0), -1F);
		farmlandModifier.put(new BlockAndMeta(Blocks.dirt, 1), -1F);//coarse dirt
		farmlandModifier.put(new BlockAndMeta(Blocks.dirt, 2), -2F);//podzol


		//farmlandModifier.put(new BlockAndMeta(Blocks.grass, 0), -3F);
	}

	@Override
	protected void loadBiomeHashMap() {
		biomeMods.put(Type.HOT, +1F);
		biomeMods.put(Type.COLD, -3F);
		biomeMods.put(Type.SPARSE, -0.2F);
		biomeMods.put(Type.DENSE, 0F);
		biomeMods.put(Type.SAVANNA, 0.5F);
		biomeMods.put(Type.CONIFEROUS, -1F);
		biomeMods.put(Type.JUNGLE, -2F);
		biomeMods.put(Type.SPOOKY, 0F);
		biomeMods.put(Type.DEAD, -1F);
		biomeMods.put(Type.LUSH, -1F);
		biomeMods.put(Type.NETHER, -2F);
		biomeMods.put(Type.END, 0F);
		biomeMods.put(Type.MUSHROOM, -2F);
		biomeMods.put(Type.MAGICAL, 0F);
		biomeMods.put(Type.OCEAN, 0F);
		biomeMods.put(Type.RIVER, -1F);
		biomeMods.put(Type.MESA, +1F);
		biomeMods.put(Type.FOREST, -0.2F);
		biomeMods.put(Type.PLAINS, 0F);
		biomeMods.put(Type.MOUNTAIN, -1F);
		biomeMods.put(Type.HILLS, -0.5F);
		biomeMods.put(Type.SWAMP, -1F);
		biomeMods.put(Type.SANDY, +1F);
		biomeMods.put(Type.SNOWY, -1F);
		biomeMods.put(Type.WASTELAND, 0F);
		biomeMods.put(Type.BEACH, +0.5F);
	}



}
