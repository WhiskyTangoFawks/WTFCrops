package wtfcrops;

import static net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE;

import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;


import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraftforge.oredict.OreDictionary;
import wtfcrops.blocks.CustomFarmland;
import wtfcrops.blocks.CustomNewLeaves;
import wtfcrops.blocks.CustomOldLeaves;
import wtfcrops.blocks.customcrops.CustomCactus;
import wtfcrops.blocks.customcrops.CustomCarrot;
import wtfcrops.blocks.customcrops.CustomJackOLantern;
import wtfcrops.blocks.customcrops.CustomMelonBlock;
import wtfcrops.blocks.customcrops.CustomMelonStalk;
import wtfcrops.blocks.customcrops.CustomMelonStem;
import wtfcrops.blocks.customcrops.CustomMushroomBrown;
import wtfcrops.blocks.customcrops.CustomMushroomRed;
import wtfcrops.blocks.customcrops.CustomNetherWart;
import wtfcrops.blocks.customcrops.CustomPotato;
import wtfcrops.blocks.customcrops.CustomPumpkinBlock;
import wtfcrops.blocks.customcrops.CustomPumpkinStalk;
import wtfcrops.blocks.customcrops.CustomPumpkinStem;
import wtfcrops.blocks.customcrops.CustomReed;
import wtfcrops.blocks.customcrops.CustomWheat;
import wtfcrops.blocks.customcrops.cropbases.CustomCrop;
import wtfcrops.items.ItemGourd;
import wtfcrops.items.BlockItem;
import wtfcrops.items.CustomFoodItem;
import wtfcrops.items.Metadata;
import wtfcrops.items.WTFItemSeed;
import wtfcrops.items.WTFItemSeedFood;
import wtfcrops.utilities.RecipeChanger;
import wtfcrops.utilities.Replacer;



public class Crops {
	public static final int wild = 0;
	public static final int simple = 1;
	public static final int giant = 2;
	public static final int dwarf = 3;
	public static final int golden = 4;
	public static final String[] cultivars = {"wild", "simple", "giant", "dwarf", "golden"};

	public static Block[] cropWheat = new Block[cultivars.length];
	public static Block[] cropCarrot =  new Block[cultivars.length];
	public static Block[] cropPotato =  new Block[cultivars.length];
	public static Block[] cropPumpkinStem = new Block[cultivars.length];
	public static Block[] blockPumpkin = new Block[cultivars.length];
	public static Block[] cropMelonStem = new Block[cultivars.length];
	public static Block[] blockMelon = new Block[cultivars.length];
	public static Block[] cropMushroombrown = new Block[cultivars.length];
	public static Block[] cropMushroomred = new Block[cultivars.length];
	public static Block[] cropSugarCane = new Block[cultivars.length];
	public static Block[] cropNetherWart = new Block[cultivars.length];
	public static Block[] cropCactus = new Block[cultivars.length];
	
	public static Item redMushroom;
	public static Item brownMushroom;
	public static Item sugarCane;
	public static Item itemPumpkin;
	public static Item itemMelon;
	public static Item itemCactus;
	
	public static Block coarseDirt;
	public static Block fertileFarmland;
	public static Block customFarmland;

	public static Block pumpkinStalk;
	public static Block melonStalk;
	
	public static Block dwarfJackOLantern;
	
	public static HashSet<String> names = new HashSet<String>();
	public static void setLocalName(Block block){
		String local =  WordUtils.capitalize(block.getUnlocalizedName().substring(5).replace("_", " "));
		names.add(block.getUnlocalizedName()+".name="+local);
	}
	public static void setLocalName(Item item){
		
		for (int loop = 0; loop < 5; loop++){
			String local =  WordUtils.capitalize(item.getUnlocalizedName(new ItemStack(item, 1, loop)).substring(5).replace("_", " "));
			String unlocal = item.getUnlocalizedName(new ItemStack(item, 1, loop));
			names.add(unlocal+".name="+local);
		}
	}
	
	public static void register() {
		
	 	
		
		fertileFarmland = new CustomFarmland(true).setBlockName("fertalised_farmland");
		GameRegistry.registerBlock(fertileFarmland, "fertalised_farmland");
		setLocalName(fertileFarmland);
		
		customFarmland = new CustomFarmland(false).setBlockName("farmland");
		Replacer.replaceBlock(Blocks.farmland, customFarmland, (ItemBlock)Item.getItemFromBlock(customFarmland));
		setLocalName(customFarmland);

		dwarfJackOLantern = new CustomJackOLantern().setBlockName("dwarf_Jack_O_Lantern");
		GameRegistry.registerBlock(dwarfJackOLantern, "dwarf_Jack_O_Lantern");
		setLocalName(dwarfJackOLantern);

		Item metadataWhacker = new Metadata().setUnlocalizedName("whacker");
		GameRegistry.registerItem(metadataWhacker, "whacker");
		
		
		registerWheat();
		registerCarrot();
		registerPotato();
		registerPumpkinStem();
		registerPumpkinBlock();
		registerMelon();
		registerMelonBlock();
		registerMushroomBrown();
		registerMushroomRed();
		registerSugarCane();
		registerNetherWart();
		//registerCactus();
		
		if (WTFCropsConfig.replaceLeaves){
			replaceLeaves();
		}
		
		pumpkinStalk = new CustomPumpkinStalk(WTFCropsConfig.growthRatePumpkin, (CustomCrop) cropPumpkinStem[giant]).setBlockName("pumpkin_stalk");
		GameRegistry.registerBlock(pumpkinStalk, "pumpkin_stalk");
		setLocalName(pumpkinStalk);
		
		melonStalk = new CustomMelonStalk(WTFCropsConfig.growthRateMelon, (CustomCrop) cropMelonStem[giant]).setBlockName("melon_stalk");
		GameRegistry.registerBlock(melonStalk, "melon_stalk");
		setLocalName(melonStalk);
		
		//Block pumpkinBlockCocoa = new CustomCocoa().setBlockName("customcocoapumpkin");
		//GameRegistry.registerBlock(pumpkinBlockCocoa, "customcocoapumpkin");

	}



	public static void registerWheat(){
		String name = "wheat";
		for (int loop = 0; loop < cultivars.length; loop++){
			String blockName = cultivars[loop]+"_"+name;
			cropWheat[loop] =  new CustomWheat(loop).setBlockName(blockName);
			if (loop != 1){
				GameRegistry.registerBlock(cropWheat[loop], blockName);
				setLocalName(cropWheat[loop]);
			}
			else{
				Replacer.replaceBlock(Blocks.wheat, cropWheat[loop], (ItemBlock)Item.getItemFromBlock(cropWheat[loop]));
				setLocalName(cropWheat[loop]);
			}
		}	
		Item seeds = new WTFItemSeed(cropWheat, "minecraft:seeds_wheat").setUnlocalizedName(name+"_seeds");
		Replacer.replaceItem(Items.wheat_seeds, seeds);
		setLocalName(seeds);
	}
	
	public static void registerCarrot(){
		String name = "carrot";
		for (int loop = 0; loop < cultivars.length; loop++){
			String blockName = cultivars[loop]+"_"+name;
			cropCarrot[loop] =  new CustomCarrot(loop).setBlockName(blockName);
			if (loop != 1){
				GameRegistry.registerBlock(cropCarrot[loop], blockName);
				setLocalName(cropCarrot[loop]);
			}
			else{
				Replacer.replaceBlock(Blocks.carrots, cropCarrot[loop], (ItemBlock)Item.getItemFromBlock(cropCarrot[loop]));
				setLocalName(cropCarrot[loop]);
			}
		}	
		Item carrot = new WTFItemSeedFood(4, 0.6F, cropCarrot, "minecraft:carrot", false, true).setUnlocalizedName(name);
		Item goldenCarrot = new WTFItemSeedFood(6, 1.2F, cropCarrot, "minecraft:carrot_golden", true, true).setUnlocalizedName(cultivars[4]+"_"+name).setPotionEffect(PotionHelper.goldenCarrotEffect);
		Replacer.replaceItem(Items.carrot, carrot);
		Replacer.replaceItem(Items.golden_carrot, goldenCarrot);
		setLocalName(carrot);
		setLocalName(goldenCarrot);
		
		OreDictionary.registerOre("foodCarrot", new ItemStack(Items.carrot, 1, WILDCARD_VALUE));
		RecipeChanger.changedItems.add(carrot);
	}
	
	public static void registerPotato(){
		String name = "potato";	
		for (int loop = 0; loop < cultivars.length; loop++){
			String blockName = cultivars[loop]+"_"+name;
			cropPotato[loop] =  new CustomPotato(loop).setBlockName(blockName);
			if (loop != 1){
				GameRegistry.registerBlock(cropPotato[loop], blockName);
				setLocalName(cropPotato[loop]);
			}
			else{
				Replacer.replaceBlock(Blocks.potatoes, cropPotato[loop],  (ItemBlock)Item.getItemFromBlock(cropPotato[loop]));
				setLocalName(cropPotato[loop]);
			}
		}	
		Item potato = new WTFItemSeedFood(1, 0.3F, cropPotato, "minecraft:potato", false, false).setUnlocalizedName(name);
		Replacer.replaceItem(Items.potato, potato);
		setLocalName(potato);
		OreDictionary.registerOre("foodPotato", new ItemStack(Items.potato, 1, WILDCARD_VALUE));
		RecipeChanger.changedItems.add(potato);
		
	}
	
	public static void registerPumpkinStem(){
		String name = "pumpkin_stem";

		for (int loop = 0; loop < cultivars.length; loop++){
			String blockName = cultivars[loop]+"_"+name;
			cropPumpkinStem[loop] =  new CustomPumpkinStem(loop).setBlockName(blockName);
			if (loop != 0){
				GameRegistry.registerBlock(cropPumpkinStem[loop], blockName);
				setLocalName(cropPumpkinStem[loop]);
			}
			else{
				Replacer.replaceBlock(Blocks.pumpkin_stem, cropPumpkinStem[loop], (ItemBlock)Item.getItemFromBlock(cropPumpkinStem[loop]));
				setLocalName(cropPumpkinStem[loop]);
			}
		}	
		Item seed = new WTFItemSeed(cropPumpkinStem, "minecraft:seeds_pumpkin").setUnlocalizedName("pumpkin_seeds");
		Replacer.replaceItem(Items.pumpkin_seeds, seed);
		RecipeChanger.exceptions.add(Items.pumpkin_seeds);
		setLocalName(seed);
	}
	public static void registerPumpkinBlock(){
		String name = "pumpkin";

		for (int loop = 0; loop < cultivars.length; loop++){
			String blockName = cultivars[loop]+"_"+name;//+"_block";
			blockPumpkin[loop] =  new CustomPumpkinBlock(loop).setBlockName(blockName);
			if (loop != 0){
				GameRegistry.registerBlock(blockPumpkin[loop], blockName);
				setLocalName(blockPumpkin[loop]);
			}
			else {
				itemPumpkin = new ItemGourd(blockPumpkin, "minecraft:pumpkin").setUnlocalizedName(name);
				GameRegistry.registerItem(itemPumpkin, name);
				Replacer.replaceBlock(Blocks.pumpkin, blockPumpkin[loop], (ItemBlock)Item.getItemFromBlock(blockPumpkin[loop]));
				setLocalName(blockPumpkin[loop]);
				setLocalName(itemPumpkin);
			}
		}
		blockPumpkin[golden] = Blocks.lit_pumpkin;
		OreDictionary.registerOre("cropPumpkin", new ItemStack(itemPumpkin, 1, WILDCARD_VALUE));
		RecipeChanger.swappedItems.put(Item.getItemFromBlock(Blocks.pumpkin), itemPumpkin);
		
	}
	
	public static void registerMelon(){
		String name = "melon_stem";
		
		for (int loop = 0; loop < cultivars.length; loop++){
			String blockName = cultivars[loop]+"_"+name;
			cropMelonStem[loop] = new CustomMelonStem(loop).setBlockName(blockName);
			if (loop != 0){
				GameRegistry.registerBlock(cropMelonStem[loop], blockName);
				setLocalName(cropMelonStem[loop]);
			}
			else{
				Replacer.replaceBlock(Blocks.melon_stem, cropMelonStem[loop], (ItemBlock)Item.getItemFromBlock(cropMelonStem[loop]));
				setLocalName(cropMelonStem[loop]);
			}
		}	
	
		Item seed = new WTFItemSeed(cropMelonStem, "minecraft:seeds_melon").setUnlocalizedName("melon_seeds");
		Replacer.replaceItem(Items.melon_seeds, seed);
		RecipeChanger.exceptions.add(Items.melon_seeds);
		setLocalName(seed);
	}
	public static void registerMelonBlock(){
		String name = "melon";
		Item melonSlice = new CustomFoodItem(2, 0.3F, false, "minecraft:melon").setUnlocalizedName("melon _slice");
		Replacer.replaceItem(Items.melon, melonSlice);
		RecipeChanger.changedItems.add(Items.melon);
		setLocalName(melonSlice);

		for (int loop = 0; loop < cultivars.length; loop++){
			String blockName = cultivars[loop]+"_"+name;
			blockMelon[loop] =  new CustomMelonBlock(loop).setBlockName(blockName);
			if (loop != 0){
				GameRegistry.registerBlock(blockMelon[loop], blockName);
				setLocalName(blockMelon[loop]);
			}
			else{
				itemMelon = new ItemGourd(blockMelon, "minecraft:melon").setUnlocalizedName(name);
				Replacer.replaceBlock(Blocks.melon_block, blockMelon[loop], (ItemBlock)Item.getItemFromBlock(blockMelon[loop]));
				GameRegistry.registerItem(itemMelon, name);
				//ItemBlock olditemBlock = (ItemBlock) Item.getItemFromBlock(Blocks.melon_block);
				//Replacer.replaceItem(olditemBlock, itemMelon);
				setLocalName(blockMelon[loop]);
				setLocalName(itemMelon);
			}
		}
		OreDictionary.registerOre("cropMelon", new ItemStack(itemMelon, 1, WILDCARD_VALUE));
		RecipeChanger.swappedItems.put(Item.getItemFromBlock(Blocks.melon_block), itemMelon);
	}
	
	public static void registerMushroomBrown(){
		String name = "brown_mushroom";
		for (int loop = 0; loop < cultivars.length; loop++){
			String blockName = cultivars[loop]+"_"+name;// + "block";
			cropMushroombrown[loop] =  new CustomMushroomBrown(loop).setBlockName(blockName);
			if (loop != 0){
				GameRegistry.registerBlock(cropMushroombrown[loop], blockName);
				setLocalName(cropMushroombrown[loop]);
			}
			else{
				Replacer.replaceBlock(Blocks.brown_mushroom, cropMushroombrown[loop],(ItemBlock)Item.getItemFromBlock(cropMushroombrown[loop]));
				setLocalName(cropMushroombrown[loop]);
			}
		}	
		brownMushroom = new BlockItem(cropMushroombrown, "minecraft:mushroom_brown").setUnlocalizedName(name);
		GameRegistry.registerItem(brownMushroom, name);
		//ItemBlock olditemBlock = (ItemBlock) Item.getItemFromBlock(Blocks.brown_mushroom);
		//Replacer.replaceItem(olditemBlock, brownMushroom);
		setLocalName(brownMushroom);
		
		OreDictionary.registerOre("cropBrownMushroom", new ItemStack(brownMushroom, 1, WILDCARD_VALUE));
		//RecipeChanger.changedItems.add(Item.getItemFromBlock(Blocks.brown_mushroom));
		RecipeChanger.swappedItems.put(Item.getItemFromBlock(cropMushroombrown[0]), brownMushroom);

	}
	public static void registerMushroomRed(){
		String name = "red_mushroom";
		for (int loop = 0; loop < cultivars.length; loop++){
			String blockName = cultivars[loop]+"_"+name;//+"block";
			cropMushroomred[loop] =  new CustomMushroomRed(loop).setBlockName(blockName);
			if (loop != 0){
				GameRegistry.registerBlock(cropMushroomred[loop], blockName);
				setLocalName(cropMushroomred[loop]);
			}
			else{
				Replacer.replaceBlock(Blocks.red_mushroom, cropMushroomred[loop],(ItemBlock)Item.getItemFromBlock(cropMushroomred[loop]));
				setLocalName(cropMushroomred[loop]);
			}
		}	
		redMushroom = new BlockItem(cropMushroomred, "minecraft:mushroom_red").setUnlocalizedName(name);
		GameRegistry.registerItem(redMushroom, name);
		//ItemBlock olditemBlock = (ItemBlock) Item.getItemFromBlock(Blocks.red_mushroom);
		//Replacer.replaceItem(olditemBlock, redMushroom);
		setLocalName(redMushroom);

		OreDictionary.registerOre("cropRedMushroom", new ItemStack(redMushroom, 1, WILDCARD_VALUE));
		//RecipeChanger.changedItems.add(Item.getItemFromBlock(Blocks.red_mushroom));
		RecipeChanger.swappedItems.put(Item.getItemFromBlock(cropMushroomred[0]), redMushroom);

	}
	public static void registerSugarCane(){
		String name = "sugar_cane";
		for (int loop = 0; loop < cultivars.length; loop++){
			String blockName = cultivars[loop]+"_"+name;
			cropSugarCane[loop] =  new CustomReed(loop).setBlockName(blockName);
			if (loop != 0){
				GameRegistry.registerBlock(cropSugarCane[loop], blockName);
				setLocalName(cropSugarCane[loop]);
			}
			else{
				Replacer.replaceBlock(Blocks.reeds, cropSugarCane[loop], (ItemBlock)Item.getItemFromBlock(cropSugarCane[loop]));
				setLocalName(cropSugarCane[loop]);
			}
		}	
		sugarCane = new WTFItemSeed(cropSugarCane, "minecraft:reeds").setUnlocalizedName(name);
		Replacer.replaceItem(Items.reeds, sugarCane);
		setLocalName(sugarCane);
		RecipeChanger.changedItems.add(Items.reeds);	
		RecipeChanger.exceptions.add(Items.sugar);
	
	}
	public static void registerNetherWart(){
		String name = "nether_wart";
		for (int loop = 0; loop < cultivars.length; loop++){
			String blockName = cultivars[loop]+"_"+name;
			cropNetherWart[loop] =  new CustomNetherWart(loop, WTFCropsConfig.growthRateNetherWart).setBlockName(blockName);
			if (loop != 0){
				GameRegistry.registerBlock(cropNetherWart[loop], blockName);
				setLocalName(cropNetherWart[loop]);
			}
			else{
				Replacer.replaceBlock(Blocks.nether_wart, cropNetherWart[loop], (ItemBlock)Item.getItemFromBlock(cropNetherWart[loop]));
				setLocalName(cropNetherWart[loop]);
			}
		}	
		Item NetherWart = new WTFItemSeed(cropNetherWart, "minecraft:nether_wart").setUnlocalizedName(name);
		RecipeChanger.changedItems.add(Items.nether_wart);
		Replacer.replaceItem(Items.nether_wart, NetherWart);
		setLocalName(NetherWart);
	}
	
	public static void registerCactus(){
		String name = "cactus";
		for (int loop = 0; loop < cultivars.length; loop++){
			String blockName = cultivars[loop]+"_"+name;
			cropCactus[loop] = new CustomCactus(loop).setBlockName(blockName);
			if (loop != 0){
				GameRegistry.registerBlock(cropCactus[loop], blockName);
				setLocalName(cropCactus[loop]);
			}
			else{
				Replacer.replaceBlock(Blocks.cactus, cropCactus[loop], (ItemBlock)Item.getItemFromBlock(cropCactus[loop]));
				setLocalName(cropCactus[loop]);
			}
		}	
		itemCactus = new WTFItemSeed(cropCactus, "minecraft:cactus").setUnlocalizedName(name);
		GameRegistry.registerItem(itemCactus, name);
		setLocalName(itemCactus);
		RecipeChanger.swappedItems.put(Item.getItemFromBlock(Blocks.cactus), itemCactus);
		
	
	}
	
	private static void replaceLeaves() {
		Block oldLeaves = new CustomOldLeaves().setBlockName("leavesOld");
		Block newLeaves = new CustomNewLeaves().setBlockName("leavesNew");
		Replacer.replaceBlock(Blocks.leaves, oldLeaves, (ItemBlock)Item.getItemFromBlock(oldLeaves));
		Replacer.replaceBlock(Blocks.leaves2, newLeaves, (ItemBlock)Item.getItemFromBlock(newLeaves));
	}
}
