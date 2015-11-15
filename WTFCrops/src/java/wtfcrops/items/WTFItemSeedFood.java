package wtfcrops.items;

import java.util.List;

import clashsoft.cslib.minecraft.item.CSItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import wtfcore.WTFCore;
import wtfcrops.Crops;
import wtfcrops.WTFCrops;
import wtfcrops.blocks.customcrops.cropbases.CustomCrop;

public class WTFItemSeedFood extends ItemFood implements IPlantable{

	protected Block[] cropCultivars;
	protected String texture;
	boolean isGolden;
	boolean hasGolden;
	
	public WTFItemSeedFood(int healAmount, float saturationModifier, Block[] cropCultivars, String texture, boolean isGolden, boolean hasGolden) {
		super(healAmount, saturationModifier, false);
		this.isGolden = isGolden;
		this.hasGolden = hasGolden;
		if (!isGolden){setHasSubtypes(true);}
		this.cropCultivars = cropCultivars;
		this.texture = texture;
		this.setCreativeTab(WTFCrops.WTFCrops);
	}
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float px, float py, float pz){
		if (side != 1)
		{
			return false;
		}
		else if (player.canPlayerEdit(x, y, z, side, itemstack) && player.canPlayerEdit(x, y + 1, z, side, itemstack))
		{
			int metadata = itemstack.getItemDamage();
			if (isGolden){metadata = Crops.golden;}
			if (side == 1 && cropCultivars[metadata].canPlaceBlockAt(world, x, y, z)){
				world.setBlock(x, y+1, z, cropCultivars[metadata]);	  
				--itemstack.stackSize;
				return true;
			}	
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
		if (this.hasGolden){
		
		if (!isGolden){    	
			for (int loop = 0; loop < cropCultivars.length-1; loop ++) {
				list.add(new ItemStack(item, 1, loop));
			}
		}
		else {
			list.add(new ItemStack(item, 1, 0));
		}
		}
		else {
			for (int loop = 0; loop < cropCultivars.length; loop ++) {
				list.add(new ItemStack(item, 1, loop));
			}
		}
    }


    @Override
    public String getUnlocalizedName(ItemStack stack) {
    	if (!isGolden){
    		return "item."+Crops.cultivars[stack.getItemDamage()] + "_" + this.getUnlocalizedName().substring(5);
    	}
    	else{
    		return this.getUnlocalizedName();
    	}
    }
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
		return EnumPlantType.Crop;
	}
	@Override
	public Block getPlant(IBlockAccess world, int x, int y, int z) {
		return null;
	}
	@Override
	public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
		return 0;
	}

}
