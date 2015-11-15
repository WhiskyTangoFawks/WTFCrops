package wtfcrops.utilities;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import wtfcore.WTFCore;

public class RecipeChanger {

	public static HashSet<Item> changedItems = new HashSet<Item>();
	public static HashMap<Item, Item> swappedItems = new HashMap<Item, Item>();
	public static HashSet<Item> exceptions = new HashSet<Item>();

	public static void fixRecipes() throws NoSuchMethodException, SecurityException{
		WTFCore.log.info("RecipeChanger: beginning scan of recipes");
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();

		HashSet<IRecipe> newRecipes = new HashSet<IRecipe>(); 

		for (int loop = 0; loop < recipes.size(); loop++){
			Object obj = recipes.get(loop);

			if (obj instanceof ShapedRecipes)
			{
				ShapedRecipes recipe = (ShapedRecipes)obj;
				for (int loop2 = 0; loop2 < recipe.recipeItems.length; loop2++){
					ItemStack oldStack = recipe.recipeItems[loop2];
					if (oldStack != null && changedItems.contains(oldStack.getItem()) && !exceptions.contains(recipe.getRecipeOutput().getItem())){
						ItemStack newStack = new ItemStack(oldStack.getItem(), oldStack.stackSize, OreDictionary.WILDCARD_VALUE);
						recipe.recipeItems[loop2] = newStack;
						newRecipes.add(recipe);
						WTFCore.log.info("Changing " + oldStack.getItem().getUnlocalizedName() + " in shaped recipe for " + recipe.getRecipeOutput().getItem().getUnlocalizedName());
					}
					if (oldStack != null && swappedItems.containsKey(oldStack.getItem()) && !exceptions.contains(recipe.getRecipeOutput().getItem())){
						ItemStack newStack = new ItemStack(swappedItems.get(oldStack.getItem()), oldStack.stackSize, OreDictionary.WILDCARD_VALUE);
						recipe.recipeItems[loop2] = newStack;
						newRecipes.add(recipe);
						WTFCore.log.info("Changing " + oldStack.getItem().getUnlocalizedName() +" for "+ newStack.getItem().getUnlocalizedName()+ " in shaped recipe for " + recipe.getRecipeOutput().getItem().getUnlocalizedName());
					}
				}

			}
			else if (obj instanceof ShapelessRecipes)
			{
				ShapelessRecipes recipe = (ShapelessRecipes)obj;
				for (int loop2 = 0; loop2 < recipe.recipeItems.size(); loop2++){
					if (recipe.recipeItems.get(loop2) instanceof ItemStack){
						ItemStack oldStack = (ItemStack) recipe.recipeItems.get(loop2);
						if (oldStack != null && changedItems.contains(oldStack.getItem()) && !exceptions.contains(recipe.getRecipeOutput().getItem())){
							ItemStack newStack = new ItemStack(oldStack.getItem(), oldStack.stackSize, OreDictionary.WILDCARD_VALUE);
							recipe.recipeItems.set(loop2, newStack);
							newRecipes.add(recipe);
							WTFCore.log.info("Changing " + oldStack.getItem().getUnlocalizedName() + " in shapeless recipe for " + recipe.getRecipeOutput().getItem().getUnlocalizedName());
						}
						if (oldStack != null && swappedItems.containsKey(oldStack.getItem()) && !exceptions.contains(recipe.getRecipeOutput().getItem())){
							ItemStack newStack = new ItemStack(swappedItems.get(oldStack.getItem()), oldStack.stackSize, OreDictionary.WILDCARD_VALUE);
							recipe.recipeItems.set(loop2, newStack);
							newRecipes.add(recipe);
							WTFCore.log.info("Changing " + oldStack.getItem().getUnlocalizedName() + " for "+ newStack.getItem().getUnlocalizedName()+ " in shapeless recipe for " + recipe.getRecipeOutput().getItem().getUnlocalizedName());
						}
					}
					
				}

			}
			/*//I don't know if I need to iterate through these types- I guess I'll wait and see
			else if (obj instanceof ShapedOreRecipe){
				ShapedOreRecipe recipe = (ShapedOreRecipe)obj;
			}
			else if (obj instanceof ShapelessOreRecipe)	{
			}
			 */

		}
	}

}
