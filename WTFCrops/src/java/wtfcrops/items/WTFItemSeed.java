package wtfcrops.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import wtfcore.WTFCore;
import wtfcrops.Crops;
import wtfcrops.WTFCrops;
import wtfcrops.blocks.customcrops.cropbases.CustomCrop;

public class WTFItemSeed extends ItemSeeds implements IPlantable{
	
	protected Block[] cropCultivars;
	protected String texture;

	public WTFItemSeed(Block[] cropCultivars, String texture){
		super(cropCultivars[0], cropCultivars[0]);
		
		setHasSubtypes(true);
		this.cropCultivars = cropCultivars;
		this.texture = texture;
		this.setCreativeTab(WTFCrops.WTFCrops);
	}
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float px, float py, float pz){
		int metadata = itemstack.getItemDamage();
		if (side == 1 && cropCultivars[metadata].canPlaceBlockAt(world, x, y, z)){
			world.setBlock(x, y+1, z, cropCultivars[metadata]);	  
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

    
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int loop = 0; loop < cropCultivars.length; loop ++) {
            list.add(new ItemStack(item, 1, loop));
        }
    }
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
    	return "item."+Crops.cultivars[stack.getItemDamage()] + "_" + this.getUnlocalizedName().substring(5);
    }
   
    

}
