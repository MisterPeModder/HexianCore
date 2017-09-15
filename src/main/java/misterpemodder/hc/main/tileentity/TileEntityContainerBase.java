package misterpemodder.hc.main.tileentity;

import misterpemodder.hc.main.HexianCore;
import misterpemodder.hc.main.network.packet.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.items.IItemHandler;

public abstract class TileEntityContainerBase extends TileEntity {
	
	public abstract IItemHandler getInventory(); 
	public abstract void onInvOpen(EntityPlayer player);
	public abstract void onInvClose(EntityPlayer player);
	
	@Override
	public void markDirty() {
		super.markDirty();
		sync();
	}
	
	public void sync() {
		NBTTagCompound toSend = new NBTTagCompound();
		toSend.setLong("pos", this.pos.toLong());
		if(!this.hasWorld()) return;
		
		if(!this.world.isRemote) {
			NetworkRegistry.TargetPoint target = new TargetPoint(this.world.provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 64);
			toSend.setTag("tileEntity", this.serializeNBT());
			HexianCore.PACKET_HANDLER.sendToAllAround(PacketHandler.TE_UPDATE_HANDLER, toSend, target);
		} else {
			toSend.setInteger("world_dim_id", world.provider.getDimension());
			toSend.setTag("player_id", NBTUtil.createUUIDTag(Minecraft.getMinecraft().player.getUniqueID()));
			HexianCore.PACKET_HANDLER.sendToServer(PacketHandler.TE_UPDATE_REQUEST_HANDLER, toSend);
		}
	}
	
	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString("");
	}
	
}
