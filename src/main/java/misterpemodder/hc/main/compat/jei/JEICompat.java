package misterpemodder.hc.main.compat.jei;

import net.minecraftforge.fml.common.Loader;

public final class JEICompat {

	public static final String MOD_ID = "jei";
	public static final String CRAFTING_CATEGORY = "minecraft.crafting";
	
	private static boolean init = false;
	private static boolean isModLoaded = false;
	
	public static boolean isModLoaded() {
		if(!init) {
			init = true;
			return isModLoaded = Loader.isModLoaded(MOD_ID);
		}
		return isModLoaded;
	}
	
}
