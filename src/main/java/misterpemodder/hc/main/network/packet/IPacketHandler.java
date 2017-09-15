package misterpemodder.hc.main.network.packet;

import misterpemodder.hc.main.network.IPacketDataHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IPacketHandler {
	
	void sendTo(IPacketDataHandler handler, NBTTagCompound data, EntityPlayerMP player);
	
	void sendToAll(IPacketDataHandler handler, NBTTagCompound data);
	
	void sendToAllAround(IPacketDataHandler handler, NBTTagCompound data, TargetPoint target);
	
	void sendToDimension(IPacketDataHandler handler, NBTTagCompound data, int dimId);
	
	@SideOnly(Side.CLIENT)
	void sendToServer(IPacketDataHandler handler, NBTTagCompound data);

}
