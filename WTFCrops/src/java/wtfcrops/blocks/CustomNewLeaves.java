package wtfcrops.blocks;

import net.minecraft.block.BlockNewLeaf;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class CustomNewLeaves extends BlockNewLeaf{

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)	    {
		return null;
	}
	
    public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_, Entity p_149670_5_)
    {
        p_149670_5_.motionX *= 0.6D;
        p_149670_5_.motionZ *= 0.6D;
    }
	
}
