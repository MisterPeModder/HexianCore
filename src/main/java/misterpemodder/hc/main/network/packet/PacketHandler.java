package misterpemodder.hc.main.network.packet;

import java.util.UUID;

import com.google.common.collect.ImmutableList;

import misterpemodder.hc.main.HexianCore;
import misterpemodder.hc.main.inventory.ContainerBase;
import misterpemodder.hc.main.inventory.elements.ISyncedContainerElement;
import misterpemodder.hc.main.network.IPacketDataHandler;
import misterpemodder.hc.main.tileentity.TileEntityCustomChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class PacketHandler implements IPacketHandler {
	
	/**
	 * <p> Handler type: server to client
	 * 
	 * <p> NBT tags:
	 * <ul>
	 * 	<li>pos: BlockPos serialized into long
	 * 	<li>tileEntity: TileEntity
	 * </ul>
	 */
	public static final IPacketDataHandler TE_UPDATE_HANDLER = data -> {
		WorldClient world = Minecraft.getMinecraft().world;
		BlockPos pos = BlockPos.fromLong(data.getLong("pos"));
		TileEntity te = world.getTileEntity(pos);
		if(te == null) return;
		else {
			te.deserializeNBT(data.getCompoundTag("tileEntity"));
		}
	};
	
	/**
	 * <p> Handler type: client to server
	 * 
	 * <p> NBT tags:
	 * <ul>
	 * 	<li>pos: BlockPos serialized into long
	 * 	<li>world_dim_id: integer
	 * 	<li>player_id: UUID
	 * </ul>
	 */
	public static final IPacketDataHandler TE_UPDATE_REQUEST_HANDLER = data -> {
		WorldServer world = DimensionManager.getWorld(data.getInteger("world_dim_id"));
		UUID playerUUID = NBTUtil.getUUIDFromTag(data.getCompoundTag("player_id"));
		
		EntityPlayer player = world.getPlayerEntityByUUID(playerUUID);
		
		BlockPos pos = BlockPos.fromLong(data.getLong("pos"));
		TileEntity te = world.getTileEntity(pos);
		
		if(te != null && player != null && player instanceof EntityPlayerMP) {
			NBTTagCompound toSend = new NBTTagCompound();
			toSend.setLong("pos", pos.toLong());
			toSend.setTag("tileEntity", te.serializeNBT());
			
			HexianCore.PACKET_HANDLER.sendTo(PacketHandler.TE_UPDATE_HANDLER, toSend, (EntityPlayerMP)player);
		}
	};
	
	/**
	 * <p> Handler type: server to client
	 * 
	 * <p> NBT tags:
	 * <ul>
	 * 	<li>pos: BlockPos serialized into long
	 * 	<li>numPlayersUsing: integer
	 * </ul>
	 */
	public static final IPacketDataHandler CHEST_UPDATE_HANDLER = data -> {
		WorldClient world = Minecraft.getMinecraft().world;
		BlockPos pos = BlockPos.fromLong(data.getLong("pos"));
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityCustomChest) {
			TileEntityCustomChest t = (TileEntityCustomChest) te;
			if (data.hasKey("numPlayersUsing"))
				t.numPlayersUsing = data.getInteger("numPlayersUsing");
		}
	};
	
	/**
	 * <p> Handler type: server to client
	 * 
	 * <p> NBT tags:
	 * <ul>
	 * 	<li>element_id: integer
	 * 	<li>element_data: NBTTagCompound
	 * </ul>
	 */
	public static final IPacketDataHandler SYNCED_CONTAINER_ELEMENTS = data -> {
		Container c = Minecraft.getMinecraft().player.openContainer;
		
		if(c instanceof ContainerBase) {
			if(data.hasKey("element_id", Constants.NBT.TAG_INT)) {
				ImmutableList<ISyncedContainerElement> elements = ((ContainerBase<?>) c).containerElements;
				int id = data.getInteger("element_id");
				if(id >= 0 && id < elements.size()) {
					ISyncedContainerElement element = elements.get(id);
					NBTTagCompound edata = data.hasKey("element_data")? data.getCompoundTag("element_data"):new NBTTagCompound();
					element.procData(edata);
				}
			}
		}
	};
	
	
	protected abstract SimpleNetworkWrapper getNetworkWrapper();
	
	
	public static void registerPacketHandlers(IPacketDataHandler... handlers) {
		if(handlers.length > 0)
			for(IPacketDataHandler handler : handlers) {
				AbstractPacket.HANDLERS.add(handler);	
			}
	}
	
	@Override
	public void sendTo(IPacketDataHandler handler, NBTTagCompound data, EntityPlayerMP player) {
		getNetworkWrapper().sendTo(new PacketServerToClient(handler, data), player);
	}
	
	@Override
	public void sendToAll(IPacketDataHandler handler, NBTTagCompound data) {
		getNetworkWrapper().sendToAll(new PacketServerToClient(handler, data));
	}
	
	@Override
	public void sendToAllAround(IPacketDataHandler handler, NBTTagCompound data, TargetPoint target) {
		getNetworkWrapper().sendToAllAround(new PacketServerToClient(handler, data), target);
	}
	
	@Override
	public void sendToDimension(IPacketDataHandler handler, NBTTagCompound data, int dimId) {
		getNetworkWrapper().sendToDimension(new PacketServerToClient(handler, data), dimId);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void sendToServer(IPacketDataHandler handler, NBTTagCompound data) {
		getNetworkWrapper().sendToServer(new PacketClientToServer(handler, data));
	}
	
}
