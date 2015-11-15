package wtfcrops.renderers;

import net.minecraft.world.IBlockAccess;

public interface ICropRenderInfo {

	public int getCustomRenderType();
	public float getRenderSizer(IBlockAccess world, int x, int y, int z);
	
	
}
