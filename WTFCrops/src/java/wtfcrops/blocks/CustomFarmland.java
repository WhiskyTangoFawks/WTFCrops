package wtfcrops.blocks;

import java.util.Random;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import wtfcore.WTFCore;
import wtfcrops.WTFCrops;
import wtfcrops.renderers.RenderIDs;

public class CustomFarmland extends BlockFarmland{

	IIcon dry;
	IIcon wet;
	boolean fertalised;
	
	//make use of dirt and sand textures as a top texture, in order to further differentiate for the player what the water value is
	
	public CustomFarmland(boolean fertalised){
		super();
		setHardness(0.6F);
		setStepSound(soundTypeGravel);
		this.setCreativeTab(WTFCrops.WTFCrops);
		this.fertalised = fertalised;
	}
	
	/*
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer p_149699_5_)
    {
		updateTick(world, x,y,z, world.rand);
    }
	*/
	
	 @SideOnly(Side.CLIENT)
	    public IIcon getIcon(int side, int meta)
	    {
	        return side == 1 ? (meta < 6 ? this.dry : this.wet) : Blocks.dirt.getBlockTextureFromSide(side);
	    }
	   @SideOnly(Side.CLIENT)
	    public void registerBlockIcons(IIconRegister p_149651_1_)
	    {
	        this.wet = p_149651_1_.registerIcon("minecraft:farmland_wet");
	        this.dry = p_149651_1_.registerIcon("minecraft:farmland_dry");
	    }
	
	public void updateTick(World world, int x, int y, int z, Random random){
		//should block be de-farmlanded goes in the notify block change, along with a check that sets metadata to maximum if block above material=water
		//step one- get the water value for the block, including rain
		//step two- increment the metadata by half the value it is wrong, rounded up
		//if the water value is too high- killing the plant should be in the plant information- because different plants tolerate flooding differently
		
		int newMeta = getWaterValue(world, x, y, z);
		int oldMeta = world.getBlockMetadata(x, y, z);
		if (newMeta != oldMeta){
			int dif = newMeta - oldMeta;
			//changes by half the difference, rounded up- which means the most ticks req to update should be 4
			world.setBlockMetadataWithNotify(x, y, z, MathHelper.ceiling_double_int(oldMeta+dif/2), 3);
		}
	}
	
	public int getWaterValue(World world, int x, int y, int z){
		double waterValue = 0;
		int scanRad = 4;
		for (int xloop = -scanRad; xloop <=scanRad; xloop++){
			for (int zloop = -scanRad; zloop <= scanRad; zloop++){
				for (int yloop = 0; yloop < 2; yloop++){
					double radius = MathHelper.sqrt_double((xloop*xloop)+(zloop*zloop)); 					
					if (radius <= scanRad && world.getBlock(x+xloop, y+yloop, z+zloop).getMaterial()==Material.water){
						int meta = 8-world.getBlockMetadata(x+xloop, y+yloop, z+zloop);
						if (meta > 7){meta = 3;} //meta = 3, because if a water is falling you're going to have 2 of them by definition
						
						double heightMod = 1+yloop/2;
						double radiusMod = (scanRad/radius)*(scanRad/radius);
						//what I want is basically a normal distribution
						double waterMod = heightMod*radiusMod*(meta*meta);
						
						waterValue += waterMod;
						
					}
				}
			}
		}
		//if it's raining, add an extra source blocks worth of water
		if (world.canLightningStrikeAt(x, y + 1, z)){waterValue += 8;}
		int ret = MathHelper.floor_double(MathHelper.sqrt_double(waterValue));
		
		//increment up or down based on the biome type- wet or dry
		BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
		if(BiomeDictionary.isBiomeOfType(biome,Type.WET)){
			ret++;
		}
		else if (BiomeDictionary.isBiomeOfType(biome,Type.DRY)){
			ret--;
		}
		
		
		if (ret < 16){return ret;}
		else {return 15;}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass(){
		return 1;
	}
	@Override
	public int getRenderType() {
		return RenderIDs.customFarmlandRenderer;
	}
	@Override
	public boolean canRenderInPass(int pass){
		//Set the static var in the client proxy
		RenderIDs.renderPass = pass;
		//the block can render in both passes, so return true always
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        if (fertalised && random.nextInt(20) == 0)
        {
            world.spawnParticle("happyVillager", x + random.nextFloat(), y + 1.2F, z + random.nextFloat(), 0.0D, 0.0D, 0.0D);
        }
    }
	
}
