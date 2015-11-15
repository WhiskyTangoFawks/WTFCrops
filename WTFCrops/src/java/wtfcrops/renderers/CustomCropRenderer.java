package wtfcrops.renderers;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import wtfcrops.blocks.customcrops.cropbases.CustomCropStem;

public class CustomCropRenderer implements ISimpleBlockRenderingHandler{

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		
		ICropRenderInfo renderinfo = (ICropRenderInfo)world.getBlock(x, y, z);
		switch(renderinfo.getCustomRenderType()){
		case 0: //standard crops
			renderBlockCrops(renderer, block, x, y, z, renderinfo.getRenderSizer(world, x, y, z));
			return true;
		case 1: // stems
			renderBlockStem(renderer, block, x, y, z);
			return true;
		case 2: // crossed squares	
			renderCrossedSquares(renderer, block, x, y, z, renderinfo.getRenderSizer(world, x, y, z));
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getRenderId() {
		// TODO Auto-generated method stub
		return RenderIDs.customCropRenderer;
	}
	
    public boolean renderBlockCrops(RenderBlocks renderer, Block block, int x, int y, int z, float size)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        renderBlockCropsImpl(renderer, block, renderer.blockAccess.getBlockMetadata(x, y, z), (double)x, (double)((float)y - 0.0625F), (double)z, size);
        return true;
    }
    public void renderBlockCropsImpl(RenderBlocks renderer, Block block, int meta, double x, double y, double z, float size)
    {
        Tessellator tessellator = Tessellator.instance;
        IIcon iicon = renderer.getBlockIconFromSideAndMetadata(block, 0, meta);

        if (renderer.hasOverrideBlockTexture())
        {
            iicon = renderer.overrideBlockTexture;
        }

        double d3 = (double)iicon.getMinU();
        double d4 = (double)iicon.getMinV();
        double d5 = (double)iicon.getMaxU();
        double d6 = (double)iicon.getMaxV();
        double d7 = x + 0.5D - 0.25D;
        double d8 = x + 0.5D + 0.25D;
        double d9 = z + 0.5D - 0.5D;
        double d10 = z + 0.5D + 0.5D;
        tessellator.addVertexWithUV(d7, y + size, d9, d3, d4);
        tessellator.addVertexWithUV(d7, y + 0.0D, d9, d3, d6);
        tessellator.addVertexWithUV(d7, y + 0.0D, d10, d5, d6);
        tessellator.addVertexWithUV(d7, y + size, d10, d5, d4);
        tessellator.addVertexWithUV(d7, y + size, d10, d3, d4);
        tessellator.addVertexWithUV(d7, y + 0.0D, d10, d3, d6);
        tessellator.addVertexWithUV(d7, y + 0.0D, d9, d5, d6);
        tessellator.addVertexWithUV(d7, y + size, d9, d5, d4);
        tessellator.addVertexWithUV(d8, y + size, d10, d3, d4);
        tessellator.addVertexWithUV(d8, y + 0.0D, d10, d3, d6);
        tessellator.addVertexWithUV(d8, y + 0.0D, d9, d5, d6);
        tessellator.addVertexWithUV(d8, y + size, d9, d5, d4);
        tessellator.addVertexWithUV(d8, y + size, d9, d3, d4);
        tessellator.addVertexWithUV(d8, y + 0.0D, d9, d3, d6);
        tessellator.addVertexWithUV(d8, y + 0.0D, d10, d5, d6);
        tessellator.addVertexWithUV(d8, y + size, d10, d5, d4);
        d7 = x + 0.5D - 0.5D;
        d8 = x + 0.5D + 0.5D;
        d9 = z + 0.5D - 0.25D;
        d10 = z + 0.5D + 0.25D;
        tessellator.addVertexWithUV(d7, y + size, d9, d3, d4);
        tessellator.addVertexWithUV(d7, y + 0.0D, d9, d3, d6);
        tessellator.addVertexWithUV(d8, y + 0.0D, d9, d5, d6);
        tessellator.addVertexWithUV(d8, y + size, d9, d5, d4);
        tessellator.addVertexWithUV(d8, y + size, d9, d3, d4);
        tessellator.addVertexWithUV(d8, y + 0.0D, d9, d3, d6);
        tessellator.addVertexWithUV(d7, y + 0.0D, d9, d5, d6);
        tessellator.addVertexWithUV(d7, y + size, d9, d5, d4);
        tessellator.addVertexWithUV(d8, y + size, d10, d3, d4);
        tessellator.addVertexWithUV(d8, y + 0.0D, d10, d3, d6);
        tessellator.addVertexWithUV(d7, y + 0.0D, d10, d5, d6);
        tessellator.addVertexWithUV(d7, y + size, d10, d5, d4);
        tessellator.addVertexWithUV(d7, y + size, d10, d3, d4);
        tessellator.addVertexWithUV(d7, y + 0.0D, d10, d3, d6);
        tessellator.addVertexWithUV(d8, y + 0.0D, d10, d5, d6);
        tessellator.addVertexWithUV(d8, y + size, d10, d5, d4);
    }
	
	public boolean renderCrossedSquares(RenderBlocks renderer, Block block, int x, int y, int z, Float size)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
        int l = block.colorMultiplier(renderer.blockAccess, x, y, z);
        float f = (float)(l >> 16 & 255) / 255.0F;
        float f1 = (float)(l >> 8 & 255) / 255.0F;
        float f2 = (float)(l & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }

        tessellator.setColorOpaque_F(f, f1, f2);
        double d1 = (double)x;
        double d2 = (double)y;
        double d0 = (double)z;
        long i1;

        if (block == Blocks.tallgrass)
        {
            i1 = (long)(x * 3129871) ^ (long)z * 116129781L ^ (long)y;
            i1 = i1 * i1 * 42317861L + i1 * 11L;
            d1 += ((double)((float)(i1 >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
            d2 += ((double)((float)(i1 >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
            d0 += ((double)((float)(i1 >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
        }
        else if (block == Blocks.red_flower || block == Blocks.yellow_flower)
        {
            i1 = (long)(x * 3129871) ^ (long)z * 116129781L ^ (long)y;
            i1 = i1 * i1 * 42317861L + i1 * 11L;
            d1 += ((double)((float)(i1 >> 16 & 15L) / 15.0F) - 0.5D) * 0.3D;
            d0 += ((double)((float)(i1 >> 24 & 15L) / 15.0F) - 0.5D) * 0.3D;
        }

        IIcon iicon = renderer.getBlockIconFromSideAndMetadata(block, 0, renderer.blockAccess.getBlockMetadata(x, y, z));
        renderer.drawCrossedSquares(iicon, d1, d2, d0, size);
        return true;
    }

	public boolean renderBlockStem(RenderBlocks renderer, Block block, int x, int y, int z)
    {
        CustomCropStem blockstem = (CustomCropStem)block;
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(blockstem.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
        int l = blockstem.colorMultiplier(renderer.blockAccess, x, y, z);
        float f = (float)(l >> 16 & 255) / 255.0F;
        float f1 = (float)(l >> 8 & 255) / 255.0F;
        float f2 = (float)(l & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }

        tessellator.setColorOpaque_F(f, f1, f2);
        blockstem.setBlockBoundsBasedOnState(renderer.blockAccess, x, y, z);
        int i1 = blockstem.getState(renderer.blockAccess, x, y, z);

        if (i1 < 0)
        {
            renderer.renderBlockStemSmall(blockstem, renderer.blockAccess.getBlockMetadata(x, y, z), renderer.renderMaxY, (double)x, (double)((float)y - 0.0625F), (double)z);
        }
        else
        {
        	renderer.renderBlockStemSmall(blockstem, renderer.blockAccess.getBlockMetadata(x, y, z), 0.5D, (double)x, (double)((float)y - 0.0625F), (double)z);
        	renderBlockStemBig(renderer, blockstem, renderer.blockAccess.getBlockMetadata(x, y, z), i1, renderer.renderMaxY, (double)x, (double)((float)y - 0.0625F), (double)z);
        }

        return true;
    }
	
	 public void renderBlockStemBig(RenderBlocks renderer, CustomCropStem p_147740_1_, int p_147740_2_, int p_147740_3_, double p_147740_4_, double p_147740_6_, double p_147740_8_, double p_147740_10_)
	    {
	        Tessellator tessellator = Tessellator.instance;
	        IIcon iicon = p_147740_1_.getStemIcon();

	        if (renderer.hasOverrideBlockTexture())
	        {
	            iicon = renderer.overrideBlockTexture;
	        }

	        double d4 = (double)iicon.getMinU();
	        double d5 = (double)iicon.getMinV();
	        double d6 = (double)iicon.getMaxU();
	        double d7 = (double)iicon.getMaxV();
	        double d8 = p_147740_6_ + 0.5D - 0.5D;
	        double d9 = p_147740_6_ + 0.5D + 0.5D;
	        double d10 = p_147740_10_ + 0.5D - 0.5D;
	        double d11 = p_147740_10_ + 0.5D + 0.5D;
	        double d12 = p_147740_6_ + 0.5D;
	        double d13 = p_147740_10_ + 0.5D;

	        if ((p_147740_3_ + 1) / 2 % 2 == 1)
	        {
	            double d14 = d6;
	            d6 = d4;
	            d4 = d14;
	        }

	        if (p_147740_3_ < 2)
	        {
	            tessellator.addVertexWithUV(d8, p_147740_8_ + p_147740_4_, d13, d4, d5);
	            tessellator.addVertexWithUV(d8, p_147740_8_ + 0.0D, d13, d4, d7);
	            tessellator.addVertexWithUV(d9, p_147740_8_ + 0.0D, d13, d6, d7);
	            tessellator.addVertexWithUV(d9, p_147740_8_ + p_147740_4_, d13, d6, d5);
	            tessellator.addVertexWithUV(d9, p_147740_8_ + p_147740_4_, d13, d6, d5);
	            tessellator.addVertexWithUV(d9, p_147740_8_ + 0.0D, d13, d6, d7);
	            tessellator.addVertexWithUV(d8, p_147740_8_ + 0.0D, d13, d4, d7);
	            tessellator.addVertexWithUV(d8, p_147740_8_ + p_147740_4_, d13, d4, d5);
	        }
	        else
	        {
	            tessellator.addVertexWithUV(d12, p_147740_8_ + p_147740_4_, d11, d4, d5);
	            tessellator.addVertexWithUV(d12, p_147740_8_ + 0.0D, d11, d4, d7);
	            tessellator.addVertexWithUV(d12, p_147740_8_ + 0.0D, d10, d6, d7);
	            tessellator.addVertexWithUV(d12, p_147740_8_ + p_147740_4_, d10, d6, d5);
	            tessellator.addVertexWithUV(d12, p_147740_8_ + p_147740_4_, d10, d6, d5);
	            tessellator.addVertexWithUV(d12, p_147740_8_ + 0.0D, d10, d6, d7);
	            tessellator.addVertexWithUV(d12, p_147740_8_ + 0.0D, d11, d4, d7);
	            tessellator.addVertexWithUV(d12, p_147740_8_ + p_147740_4_, d11, d4, d5);
	        }
	    }
	
}
