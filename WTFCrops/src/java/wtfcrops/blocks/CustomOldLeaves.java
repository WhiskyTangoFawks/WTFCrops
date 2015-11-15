package wtfcrops.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import wtfcore.WTFCore;

public class CustomOldLeaves extends BlockOldLeaf{

	   private static final String __OBFID = "CL_00000280";
	   public static final String[] fastnames = new String[] {"leaves_oak", "leaves_spruce", "leaves_birch", "leaves_jungle"}; 
	   public static final String[] fancynames = new String[] {"leaves_oak_opaque", "leaves_spruce_opaque", "leaves_birch_opaque", "leaves_jungle_opaque"};
	   public static IIcon[] fast = new IIcon[16];
	   public static IIcon[] fancy =  new IIcon[16];
	    
	    protected IIcon[][] field_150129_M = new IIcon[2][];
	 
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)	    {
		return null;
	}
    @Override
	public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_, Entity p_149670_5_)
    {
        p_149670_5_.motionX *= 0.6D;
        p_149670_5_.motionZ *= 0.6D;
    }
    
    @Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
    	if (field_150127_b == 0){
    		return fancy[meta];
    	}
    	else {
    		return fast[meta];
    	}
    	//return (meta & 3) == 1 ? this.field_150129_M[this.field_150127_b][1] : ((meta & 3) == 3 ? this.field_150129_M[this.field_150127_b][3] : ((meta & 3) == 2 ? this.field_150129_M[this.field_150127_b][2] : this.field_150129_M[this.field_150127_b][0]));
    }
    
    @Override
	public void registerBlockIcons(IIconRegister iconregister)
    {
    	for (int loop = 0; loop < 4; loop++){
    		fast[loop] = iconregister.registerIcon(fastnames[loop]);
    		fancy[loop] = iconregister.registerIcon(fancynames[loop]);
    	}
    	/*
    	for (int i = 0; i < field_150130_N.length; ++i)
        {
            this.field_150129_M[i] = new IIcon[field_150130_N[i].length];

            for (int j = 0; j < field_150130_N[i].length; ++j)
            {
                this.field_150129_M[i][j] = p_149651_1_.registerIcon(field_150130_N[i][j]);
            }
        }
        */
    }

}
