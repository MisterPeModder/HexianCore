package misterpemodder.hc.main.network.packet;

import misterpemodder.hc.main.network.IPacketDataHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketClientToServer extends AbstractPacket {
	
	public PacketClientToServer(){}
	
	public PacketClientToServer(IPacketDataHandler dataHandler, NBTTagCompound data) {
		super(dataHandler, data);
	}
	
	public static class PacketClientToServerHandler implements IMessageHandler<PacketClientToServer, IMessage> {
		
		public PacketClientToServerHandler(){}

		@Override
		public IMessage onMessage(PacketClientToServer message, MessageContext ctx) {
			FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable() {
				public void run() {
					if(message.data != null || message.dataHandler != null) {
						message.dataHandler.procData(message.data);
					}
				}
			});
			return null;
		}
		
	}

}
