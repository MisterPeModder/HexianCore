package misterpemodder.hc.main.network.packet;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import misterpemodder.hc.main.HCRefs;
import misterpemodder.hc.main.network.IPacketDataHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class AbstractPacket implements IMessage {

	protected IPacketDataHandler dataHandler;
	protected NBTTagCompound data;
	
	protected static final List<IPacketDataHandler> HANDLERS = new ArrayList<>();
	
	public AbstractPacket(){}
	
	public AbstractPacket(IPacketDataHandler dataHandler, NBTTagCompound data) {
		this.dataHandler = dataHandler;
		this.data = data;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer buffer = new PacketBuffer(buf);
		try {
			
			int handlerID = buffer.readInt();
			NBTTagCompound c = buffer.readCompoundTag();

			if(c.hasKey("data") && handlerID < HANDLERS.size() && handlerID >= 0) {
				this.dataHandler = HANDLERS.get(handlerID);
				this.data = c.getCompoundTag("data");
			}
			
		} catch (Exception e) {
			HCRefs.LOGGER.error("Something went wrong when trying to read packet!", e);
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer buffer = new PacketBuffer(buf);
		
		buffer.writeInt(HANDLERS.indexOf(dataHandler));
		
		NBTTagCompound c = new NBTTagCompound();
		c.setTag("data", data);
		buffer.writeCompoundTag(c);
	}

}
