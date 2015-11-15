package wtfcrops.utilities;

import java.util.Iterator;
import java.util.List;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;

public class RecipeIterator {

	public static void iterateRecipes(){
		List recipeList = CraftingManager.getInstance().getRecipeList();
		Iterator iterator = recipeList.iterator();
		Object object = iterator.next();
		if (object instanceof ShapedRecipes){
			ShapedRecipes recipe = (ShapedRecipes)object;
			//for (int loop = 0; loop < recipe.recipeItems.length
		}
	}
	
}
