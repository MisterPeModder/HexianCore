package misterpemodder.hc.main.client.gui.tabs;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

/**
 * Handles the packet sent by client when on button clicking.
 */
@FunctionalInterface
public interface IButtonClickConsumer {

	/**
	 * @param buttonId Id of the button that has been clicked.
	 * @param world The current world
	 * @param pos Postiton of the TileEntity.
	 * @param te The TileEntity that triggered the event.
	 * @param extraData Additional NBT data.
	 * 
	 * @return True if the click event has been handled and other button click handlers should not be called.
	 * False otherwise.
	 */
	public boolean handleClick(int buttonId, WorldServer world, BlockPos pos, TileEntity te, NBTTagCompound extraData);
	
}
