package wtfcrops.blocks.customcrops.cropbases;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockLog;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import wtfcore.WTFCore;
import wtfcrops.Crops;
import wtfcrops.WTFCrops;

public class CustomGourdBase extends CustomGenericCocoa{

	
	public int maxGrowth=2;
	public int cultivar;
	public CustomCropStem stem;

	public CustomGourdBase(int cultivar){
		this.cultivar=cultivar;
		this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
		this.setTickRandomly(true);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (!world.isRemote){
			CustomCropStem stem = null;
			int stemX=x;
			int stemZ=z;
			int meta = world.getBlockMetadata(x, y, z);
			int growthStage = func_149987_c(meta);
			int direction = getDirection(meta);
			switch (direction)
			{
			case 0:
				stemZ=z+1;
				break;
			case 1:
				stemX=x-1;
				break;
			case 2:
				stemZ=z-1;
				break;
			case 3:
				stemX=x+1;
				break;
			}

			if (growthStage < maxGrowth && shouldGrow(world, random, stemX, y, stemZ)){
				world.setBlockMetadataWithNotify(x, y, z, direction << 2 | getDirection(meta), 2);
			}
		}
	}

	public boolean shouldGrow(World world, Random random, int x, int y, int z){
		// THis must be overriden
		return false;
	}
	
	


	public int getRenderType()
	{
		return 0;
	}

	public void setBlockBoundsForItemRender() {
		if (this.cultivar == Crops.dwarf){
			this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
		}
		else {
			this.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
		}
	}

	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		if (meta == 15){
			this.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
			return;
		}
		if (meta == 14){
			this.setBlockBounds(0.25F, 0F, 0.25F, .75F, .5F, .75F);
			return;
		}
		int direction = getDirection(meta);
		int growthStage = func_149987_c(meta); //this seems to a be a bit shift of the meta, which sets the size, it should returns 0, 1, or 2
		float size = (0.5F + growthStage*0.25F)/2F;
		int k1 = 6 + growthStage * 2;
		int l1 = 6 + growthStage * 2;
		float f = (float)k1 / 2.0F;

		float minx = 0.5F-size;
		float miny = 0.5F-size; 
		float minz = 0.5F-size; 
		float maxx = 0.5F+size;
		float maxy = 0.5F+size; 
		float maxz = 0.5F+size;

		switch (direction)
		{
		case 0:
			minz = 1F-size*2;
			maxz =1F;
			break;
		case 1:
			minx = 0F;
			maxx = 0F+size*2;
			break;
		case 2:
			minz = 0F;
			maxz = 0F+size*2;
			break;
		case 3:
			minx=1F-size*2;;
			maxx=1F;
			break;
		}
		this.setBlockBounds(minx, miny, minz, maxx, maxy, maxz);
	}
	
    public boolean canBlockStay(World world, int x, int y, int z)
    {
    	if (world.getBlock(x,y,z).isSideSolid(world, x, y-1, z, ForgeDirection.UP)){
    		return true;
    	}
    	return true;
    }


	
	
}
