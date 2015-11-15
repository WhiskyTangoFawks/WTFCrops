package wtfcrops.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import wtfcore.WTFCore;
import wtfcrops.Crops;

public class BlockItem extends WTFItemSeed {

	public BlockItem(Block[] cropCultivars, String texture) {
		super(cropCultivars, texture);
	}
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float px, float py, float pz){
		if (side != 1)
		{
			WTFCore.log.info("side does not equal 1");
			return false;
		}
		//else if (player.canPlayerEdit(x, y, z, side, itemstack) && player.canPlayerEdit(x, y + 1, z, side, itemstack))
		//{
			int metadata = itemstack.getItemDamage();
			if (side == 1 && cropCultivars[metadata].canPlaceBlockAt(world, x, y, z)){
				world.setBlock(x, y+1, z, cropCultivars[metadata]);	  
				--itemstack.stackSize;
				return true;
			}	
		//}
		return false;

	}

	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconregister)
    {
        this.itemIcon = iconregister.registerIcon(texture);
    }
	
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta)
    {
        return this.cropCultivars[meta].getIcon(0, 0);
    }
    public int getMetadata(int p_77647_1_)
    {
        return p_77647_1_;
    }
    @SideOnly(Side.CLIENT)
    public int getSpriteNumber()
    {
        return this.cropCultivars[0].getItemIconName() != null ? 1 : 0;
    }
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
    	return "item."+Crops.cultivars[stack.getItemDamage()] + "_" + this.getUnlocalizedName().substring(5);
    }
   
}
