package misterpemodder.hc.main.compat.jei;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

@JEIPlugin
public class JEIPluginHC implements IModPlugin {

	private static IJeiRuntime jeiRuntime;
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {}

	@Override
	public void registerIngredients(IModIngredientRegistration registry) {}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {}

	@Override
	public void register(IModRegistry registry) {}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		JEIPluginHC.jeiRuntime = jeiRuntime;
	}
	
	public static boolean hasJEIRuntime() {
		return JEIPluginHC.jeiRuntime != null;
	}
	
	public static IJeiRuntime getJEIRuntime() {
		return JEIPluginHC.jeiRuntime;
	}
	
}
