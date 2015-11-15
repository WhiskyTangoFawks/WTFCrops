package wtfcrops.blocks.customcrops;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPumpkin;
import wtfcrops.WTFCrops;

public class CustomJackOLantern extends BlockPumpkin{

	public CustomJackOLantern() {
		super(true);
		this.setBlockBounds(0.25F, 0F, 0.25F, 0.75F, 0.5F, 0.75F);
		setHardness(1.0F);
		setStepSound(soundTypeWood);
		this.setCreativeTab(WTFCrops.WTFCrops);
		
	}

    @SideOnly(Side.CLIENT)
    protected String getTextureName()
    {
        return "minecraft:pumpkin";
    }

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
    
 
}
