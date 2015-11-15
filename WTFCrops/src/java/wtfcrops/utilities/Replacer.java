package wtfcrops.utilities;

import clashsoft.cslib.minecraft.block.CSBlocks;
import clashsoft.cslib.minecraft.item.CSItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class Replacer {

	public static void replaceBlock(Block oldBlock, Block newBlock, ItemBlock itemblock){
		CSBlocks.replaceBlock(oldBlock, newBlock, itemblock);
	}
	public static void replaceItem(Item oldItem, Item newItem){
		CSItems.replaceItem(oldItem, newItem);
	}
	
}
