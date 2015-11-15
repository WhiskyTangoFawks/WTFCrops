package wtfcrops.renderers;


import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;


public class CustomFarmlandRenderer  implements ISimpleBlockRenderingHandler{

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		if (world.getBlockMetadata(x,y,z)<11){
			if (RenderIDs.renderPass==0){
				renderer.renderStandardBlock(block, x, y, z);
			}
			else {
				return false;
			}
		}
		else {
			
			if (RenderIDs.renderPass==0){
				renderer.renderStandardBlock(block, x, y, z);
			}
			else{
				renderer.renderStandardBlock(Blocks.water, x, y, z);
			}
		}
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getRenderId() {
		// TODO Auto-generated method stub
		return RenderIDs.customFarmlandRenderer;
	}


}
