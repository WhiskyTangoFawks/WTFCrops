package wtfcrops.utilities;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class BlockAndMeta {

	public Block block;
	public int meta;
	
	public BlockAndMeta(Block block, int meta){
		this.block=block;
		this.meta = meta;
	}
	public BlockAndMeta(World world, int x, int y, int z){
		block = world.getBlock(x, y, z);
		meta = world.getBlockMetadata(x, y, z);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((block == null) ? 0 : block.hashCode());
		result = prime * result + meta;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlockAndMeta other = (BlockAndMeta) obj;
		if (block == null) {
			if (other.block != null)
				return false;
		} else if (!block.equals(other.block))
			return false;
		if (meta != other.meta)
			return false;
		return true;
	}
}
