package wtfcrops.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import wtfcrops.Crops;
import wtfcrops.WTFCrops;

public class CustomFoodItem extends ItemFood{

	public String texture;

	public CustomFoodItem(int p_i45339_1_, float p_i45339_2_, boolean p_i45339_3_, String texture) {
		super(p_i45339_1_, p_i45339_2_, p_i45339_3_);
		this.texture=texture;
		this.setCreativeTab(WTFCrops.WTFCrops);
		// TODO Auto-generated constructor stub
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconregister)
	{
		this.itemIcon = iconregister.registerIcon(texture);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int loop = 0; loop < 4; loop ++) {
			list.add(new ItemStack(item, 1, loop));
		}
	}
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        //return "item." + this.getUnlocalizedName()+"_"+Crops.cultivars[stack.getItemDamage()];
    	return Crops.cultivars[stack.getItemDamage()] + "_" + this.getUnlocalizedName();
    }
   
}
