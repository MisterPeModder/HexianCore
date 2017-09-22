package misterpemodder.hc.main.utils;

import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import misterpemodder.hc.main.HCRefs;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * An implementation of {@link IGuiHandler} that facilitates the opening of GUIs.
 */
public final class GuiHelper implements IGuiHandler {
	
	/**
	 * The maximum amount of mods that can be registered
	 * by {@link GuiHelper#registerGuiElement(IGuiElement)} or
	 * 2<sup>9
	 */
	public static final int MAX_MODS_NUMBER = 0x200;
	/**
	 * The number of bits used to store mod id.
	 */
	public static final int SIZE = 9;
	
	private static final List<Object> MODS = NonNullList.create();
	private static final ListMultimap<Integer, IGuiElement> ELEMENTS = ArrayListMultimap.create();
	

	/**
	 * Registers an {@link IGuiElement}.
	 * 
	 * @param modInstance - The instance of your mod.
	 * @param element - The element to be registered.
	 */
	public static void registerGuiElement(Object modInstance, IGuiElement element) {
		registerGuiElementInternal(checkModInstance(modInstance), element);
	}
	
	/**
	 * Registers multiple {@link IGuiElement IGuiElements}.
	 * 
	 * @param modInstance - The instance of your mod.
	 * @param elements - The elements to be registered.
	 */
	public static void registerGuiElements(Object modInstance, IGuiElement... elements) {
		int modId = checkModInstance(modInstance);
		if(modId < 0) return;
		
		for(IGuiElement element : elements) {
			registerGuiElementInternal(modId, element);
		}
	}
	
	private static int checkModInstance(Object modInstance) {
		int num = -1;
		
		if(modInstance == null)
			HCRefs.LOGGER.error("A mod attempted to register a IGuiElement with a null mod instance!");
		else if (!Loader.instance().getReversedModObjectList().containsKey(modInstance))
			HCRefs.LOGGER.error("A mod attempted to register a IGuiElement with an invalid mod instance!");
		else if (MODS.size() >= MAX_MODS_NUMBER)
			HCRefs.LOGGER.error("The maximum number of mods limit for IGuiElement registration has been reached!");
		else {
			if(!MODS.contains(modInstance))
				MODS.add(modInstance);
			num = MODS.indexOf(modInstance);
		}
		
		return MathHelper.clamp(num, -1, MAX_MODS_NUMBER - 1);
	}
	
	private static void registerGuiElementInternal(int modId, IGuiElement element) {
		if(modId < 0 || modId >= MAX_MODS_NUMBER) return;
		
		if(!ELEMENTS.containsEntry(modId, element)) {
			ELEMENTS.put(modId, element);
		}
	}
	
	/**
	 * Provides a {@link Container} instance on the server side 
	 * and a {@link GuiContainer} instance on the client side.
	 */
	public interface IGuiElement {
		
		public Container getServerGuiElement(EntityPlayer player, World world, int x, int y, int z);
		
		public GuiScreen getClientGuiElement(EntityPlayer player, World world, int x, int y, int z);
		
	}
	
	public static IGuiElement getGuiElement(int id) {
		int elemId = id >> SIZE;
		
		List<IGuiElement> elements = ELEMENTS.get(id & (MAX_MODS_NUMBER - 1));
		if(elemId >= 0 && elemId < elements.size())
			return elements.get(elemId);
		return null;
	}
	
	/**
	 * Opens the gui supplied by the gui element.
	 * 
	 * @param player - The player that has opened the gui.
	 * @param element - The gui element to be shown.
	 * @param world - The world.
	 * @param pos - The position.
	 */
	public static void openGui(EntityPlayer player, IGuiElement element, World world, BlockPos pos) {
		openGui(player, element, world, pos.getX(), pos.getY(), pos.getZ());
	}
	
	/**
	 * Opens the gui supplied by the gui element.
	 * 
	 * @param player - The player that has opened the gui.
	 * @param element - The gui element to be shown.
	 * @param world - The world.
	 * @param x - The x coordinate.
	 * @param y - The y coordinate.
	 * @param z - The z coordinate.
	 */
	public static void openGui(EntityPlayer player, IGuiElement element, World world, int x, int y, int z) {

		for(Integer modId : ELEMENTS.keySet()) {
			if(modId < 0 || modId >= MAX_MODS_NUMBER)
				continue;
			List<IGuiElement> elements = ELEMENTS.get(modId);
			if(elements.contains(element)) {
				player.openGui(MODS.get(modId), (elements.indexOf(element) << SIZE) | modId, world, x, y, z);
				break;
			}
		}
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		IGuiElement guiElement = getGuiElement(id);
		return guiElement == null? null : guiElement.getServerGuiElement(player, world, x, y, z);
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		IGuiElement guiElement = getGuiElement(id);
		return guiElement == null? null : guiElement.getClientGuiElement(player, world, x, y, z);
	}
	
}
