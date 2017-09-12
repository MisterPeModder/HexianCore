package misterpemodder.hc.main.compat.craftingtweaks;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import misterpemodder.hc.main.inventory.ContainerBase;
import misterpemodder.hc.main.utils.ModUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public final class CraftingTweaksCompat {
	
	public static final String MOD_ID = "craftingtweaks";
	public static Class<?> guiTweakButtonClass = null;
	
	private static boolean init = false;
	private static boolean isModLoaded = false;
	private static final Logger LOGGER = LogManager.getLogger("Hexian Core CraftingTweaks compat");
	private static final Map<Class<? extends ContainerBase<?>>, Integer> CONTAINERS = new HashMap<>();
	
	public static boolean isModLoaded() {
		if(!init) {
			init = true;
			return isModLoaded = Loader.isModLoaded(MOD_ID);
		}
		return isModLoaded;
	}
	
	public static void init() {
		
		if(isModLoaded()) {
			LOGGER.info("Found CraftingTweaks: Registering containers...");
			
			try {
				guiTweakButtonClass = Class.forName("net.blay09.mods.craftingtweaks.client.GuiTweakButton");
			} catch (ClassNotFoundException e) {
				LOGGER.info("Tweak Button class not found, Crafting Tweaks buttons wont be hidden inside containers!");
			}
			
			for(Class<? extends ContainerBase<?>> clazz : CONTAINERS.keySet()) {
				NBTTagCompound tagCompound = new NBTTagCompound();
				tagCompound.setString("ContainerClass", clazz.getName());

				tagCompound.setInteger("GridSlotNumber", CONTAINERS.get(clazz));
				tagCompound.setInteger("GridSize", 9);
				tagCompound.setBoolean("HideButtons", false);
				tagCompound.setString("AlignToGrid", "left");

				FMLInterModComms.sendMessage("craftingtweaks", "RegisterProvider", tagCompound);
			}
			
		} else {
			LOGGER.info("CraftingTweaks not found: integration not loading");
		}
	}
	
	/**
	 * Registers a container that support the CraftingTweaks buttons.
	 * This method may only be called during the preInit phase.
	 * 
	 * @param clazz The container class
	 * @param gridSlotId The first slot index of the crafting grid.
	 */
	public static void registerContainer(Class<? extends ContainerBase<?>> clazz, int gridSlotId) {
		if(isModLoaded()) {
			if(!Loader.instance().hasReachedState(LoaderState.INITIALIZATION)) {
				CONTAINERS.put(clazz, gridSlotId);
			} else {
				ModContainer mod = ModUtils.activeModContainer();
				LOGGER.error("Mod %s (%s) attempted to register the container class %s too late!", mod.getModId(), mod.getName(), clazz.getCanonicalName());
			}	
		}
	}

}
