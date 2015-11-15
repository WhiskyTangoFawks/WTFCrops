package wtfcrops.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.IPlantable;

public class BlockItemSeedFood extends WTFItemSeedFood implements IPlantable{

	public BlockItemSeedFood(int healAmount, float saturationModifier, Block[] cropCultivars, String texture,
			boolean isGolden, boolean hasGolden) {
		super(healAmount, saturationModifier, cropCultivars, texture, isGolden, hasGolden);
		this.setMaxDamage(0);
		
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
    
}
