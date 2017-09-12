package misterpemodder.hc.main.utils;

import javax.annotation.Nullable;

import misterpemodder.hc.main.AbstractMod;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

/**
 * This class provides various utilities related to mods and ModContainers
 */
public final class ModUtils {
	
	/**
	 * A shortcut method for {@link Loader}{@code .instance().activeModContainer()}
	 */
	public static ModContainer activeModContainer() {
		return Loader.instance().activeModContainer();
	}
	
	/**
	 * Checks if the active mod container contains an AbstractMod instance.
	 */
	public static boolean isHexianMod() {
		return isHexianMod(activeModContainer());
	}
	
	/**
	 * Checks if this mod container contains an AbstractMod instance.
	 */
	public static boolean isHexianMod(ModContainer mod) {
		return mod.getMod() instanceof AbstractMod;
	}
	
	/**
	 * Returns the active mod instance.
	 * @return the active mod instance if the current mod extends AbstractMod,
	 * null otherwise.
	 */
	@Nullable
	public static AbstractMod getCurrentModInstance() {
		ModContainer mc = activeModContainer();
		return isHexianMod(mc)? (AbstractMod) mc.getMod() : null;
	}
	
	/**
	 * Retrieves the AbstractMod instance from a mod container.
	 * Note: this method does NOT check if the given ModContainer instance conatains an AbstractMod,
	 * if you use this method, please check with {@link ModUtils}.isHexianMod(container) before.
	 */
	public static AbstractMod getHexianMod(ModContainer container) {
		return ((AbstractMod)container.getMod());
	}
	
	/**
	 * Retrieves the AbstractMod instance from a mod container.
	 * This method is the safe version of {@link ModUtils}.getHexianMod(container).
	 */
	@Nullable
	public static AbstractMod getHexianModChecked(ModContainer container) {
		if(ModUtils.isHexianMod(container))
			return ModUtils.getHexianMod(container);
		return null;
	}
	
}
