package misterpemodder.hc.main.network;

import net.minecraft.nbt.NBTTagCompound;

@FunctionalInterface
public interface IPacketDataHandler {

	public void procData(NBTTagCompound data);
	
}
