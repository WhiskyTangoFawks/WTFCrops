package wtfcrops;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import wtfcore.utilities.LangWriter;
import wtfcrops.renderers.RenderIDs;
import wtfcrops.utilities.RecipeChanger;


@Mod(modid =WTFCrops.modid, name = "WhiskyTangoFox's Crops", version = "1.0", dependencies = "required-after:WTFCore;required-after:cslib")

public class WTFCrops {

	public static  final String modid ="WTFCrops";

	@Instance(modid)
	public static WTFCrops instance;

	//@SidedProxy(clientSide="cavebiomes.proxy.CBClientProxy", serverSide="cavebiomes.proxy.CommonProxy")
	//public static CommonProxy proxy;

	public static String alphaMaskDomain = "wtfcrops/blocks/alphamasks/";
	public static String overlayDomain =   "wtfcrops/blocks/overlays/";

	public static CreativeTabs WTFCrops = new CreativeTabs("WTF's Crops")
	{
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem()
		{
			return Items.pumpkin_seeds;
		}
	};


	@EventHandler
	public void PreInit(FMLPreInitializationEvent preEvent)
	{
		WTFCropsConfig.customConfig();
		Crops.register();
		RenderIDs.register();
	}

	@EventHandler public void load(FMLInitializationEvent event)
	{

	}

	@EventHandler
	public void PostInit(FMLPostInitializationEvent postEvent){

		try {
			RecipeChanger.fixRecipes();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		//Post-recipe fixing recipes to add
		
		GameRegistry.addShapelessRecipe(new ItemStack(Items.glowstone_dust, 1), new Object[] {new ItemStack(Crops.sugarCane, 1, Crops.golden)});
		GameRegistry.addShapelessRecipe(new ItemStack(Crops.dwarfJackOLantern, 1), new Object[] {new ItemStack(Crops.itemPumpkin, 1, Crops.dwarf), new ItemStack(Blocks.torch, 1)});
		
		for (int loop = 1; loop < 4; loop++){
			//sugar
			GameRegistry.addShapelessRecipe(new ItemStack(Items.sugar, 1), new Object[] {new ItemStack(Crops.sugarCane, 1, loop)});
			//custom melon item
			GameRegistry.addShapelessRecipe(new ItemStack(Items.melon_seeds, 1, loop), new Object[] {new ItemStack(Items.melon, 1, loop)});
			//Pumpkin Seeds
			
		}
		GameRegistry.addShapelessRecipe(new ItemStack(Items.pumpkin_seeds, 4, 0), new Object[] {new ItemStack(Crops.itemPumpkin, 1, 0)});
		GameRegistry.addShapelessRecipe(new ItemStack(Items.pumpkin_seeds, 3, 1), new Object[] {new ItemStack(Crops.itemPumpkin, 1, 1)});
		GameRegistry.addShapelessRecipe(new ItemStack(Items.pumpkin_seeds, 2, 2), new Object[] {new ItemStack(Crops.itemPumpkin, 1, 2)});
		GameRegistry.addShapelessRecipe(new ItemStack(Items.pumpkin_seeds, 2, 3), new Object[] {new ItemStack(Crops.itemPumpkin, 1, 3)});
		
		LangWriter.genLangFile(Crops.names, "WTFCrops_en_US.lang");
	}
}
