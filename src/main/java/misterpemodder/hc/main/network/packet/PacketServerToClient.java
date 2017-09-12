package misterpemodder.hc.main.network.packet;

import misterpemodder.hc.main.network.IPacketDataHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketServerToClient extends AbstractPacket {
	
	public PacketServerToClient(){}
	
	public PacketServerToClient(IPacketDataHandler dataHandler, NBTTagCompound data) {
		super(dataHandler, data);
	}
	
	public static class PacketServerToClientHandler implements IMessageHandler<PacketServerToClient, IMessage> {
		
		public PacketServerToClientHandler(){}

		@Override
		public IMessage onMessage(PacketServerToClient message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
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
