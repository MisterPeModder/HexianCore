package misterpemodder.hc.main.inventory.elements;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Represents a gui element that requires its data to be synced with the server.
 * Use ContainerBase::addContainerElements to add new elements to your container.
 */
public interface ISyncedContainerElement{

	/**
	 * @return Should the container element data be send to client?
	 */
	public boolean shouldSendDataToClient();
	
	/**
	 * Writes the data to be sent to the client.
	 * 
	 * @param data - the nbt data to be sent.
	 * @return the NBTTagCompound.
	 */
	public NBTTagCompound writeData(NBTTagCompound data);
	
	/**
	 * Recieves the data sent by server.
	 * 
	 * @param data - the nbt data.
	 */
	@SideOnly(Side.CLIENT)
	public void procData(NBTTagCompound data);
	
}
