package wtfcrops.items;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import wtfcore.WTFCore;
import wtfcrops.WTFCrops;
import wtfcrops.blocks.customcrops.cropbases.CustomCrop;

public class Metadata extends ItemSeeds{

Random random = new Random();
	
	public Metadata() {
		super(Blocks.pumpkin, Blocks.pumpkin);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float px, float py, float pz){
		
		/*
		if (world.getBlock(x, y, z) instanceof CustomCrop){
			CustomCrop crop = (CustomCrop) world.getBlock(x, y, z);
			crop.updateTick(world, x, y, z, random);
		}
		else {  */
		int meta = world.getBlockMetadata(x, y, z)+1;
		if (meta > 14){meta = 0;}
		
		world.setBlockMetadataWithNotify(x, y, z, meta, 3);
		WTFCore.log.info("metadata set " + world.getBlockMetadata(x, y, z) + "for " + world.getBlock(x,y,z).getUnlocalizedName());
		//}
		return true;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconregister)
    {
        this.itemIcon = iconregister.registerIcon("minecraft:stick");
    }
	
}
