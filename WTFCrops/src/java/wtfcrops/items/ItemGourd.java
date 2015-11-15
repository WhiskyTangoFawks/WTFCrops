package wtfcrops.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import wtfcrops.Crops;
import wtfcrops.WTFCrops;

public class ItemGourd extends WTFItemSeed{


	public ItemGourd(Block[] cropCultivars, String texture) {
		super(cropCultivars, texture);
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float px, float py, float pz){
		int metadata = itemstack.getItemDamage();
		if (side == 1){//cropCultivars[metadata].canPlaceBlockAt(world, x, y, z)){
			if (itemstack.getItemDamage() == Crops.dwarf){
				world.setBlock(x, y+1, z, cropCultivars[metadata], 14, 2);	  
				--itemstack.stackSize;
				return true;	
			}
			world.setBlock(x, y+1, z, cropCultivars[metadata], 15, 2);	  
			--itemstack.stackSize;
			return true;
		}	
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
        return this.cropCultivars[meta].getIcon(3, 0);
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
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		
		for (int loop = 0; loop < cropCultivars.length; loop ++) {
			if (this.cropCultivars[loop] != Blocks.lit_pumpkin){
				list.add(new ItemStack(item, 1, loop));
			}
		}
	}
	
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
    	return "item."+Crops.cultivars[stack.getItemDamage()] + "_" + this.getUnlocalizedName().substring(5);
    }

}
