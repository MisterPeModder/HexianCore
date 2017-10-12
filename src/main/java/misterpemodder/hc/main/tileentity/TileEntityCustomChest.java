package misterpemodder.hc.main.tileentity;

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

import misterpemodder.hc.api.block.ILockable;
import misterpemodder.hc.main.HexianCore;
import misterpemodder.hc.main.apiimpl.capability.owner.CapabilityOwner;
import misterpemodder.hc.main.apiimpl.capability.owner.OwnerHandlerUUID;
import misterpemodder.hc.main.blocks.BlockCustomChest;
import misterpemodder.hc.main.blocks.properties.IWorldNameableModifiable;
import misterpemodder.hc.main.capabilty.item.ItemStackHandlerLockable;
import misterpemodder.hc.main.capabilty.item.SyncedItemHandler;
import misterpemodder.hc.main.network.packet.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Shared code for TMO Titanium chests and AC Adujstable chests.
 */
public abstract class TileEntityCustomChest extends TileEntityContainerBase implements ITickable, IWorldNameableModifiable, ILockable{

	protected boolean locked = false;
	public static final int MAX_UPDATE_TIME = 200;
	public int ticksSinceUpdate;
	
	protected ItemStackHandler inventory;
	protected ItemStackHandler lock;
	protected OwnerHandlerUUID ownerHandler = new OwnerHandlerUUID();
	protected String customName;
	
	public int numPlayersUsing;
    public float prevLidAngle;
    public float lidAngle;
    
    public TileEntityCustomChest() {
    	super();
    	this.inventory = new SyncedItemHandler(this, getInventorySize());
    	this.lock = new ItemStackHandlerLockable(this, 1);
	}
    
    public abstract int getInventorySize();
    
	@Override
    public void onInvOpen(EntityPlayer player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }
            if(!player.getEntityWorld().isRemote)
            	this.syncPlayerUsingNum(1);
        }
    }
	
	@Override
    public void onInvClose(EntityPlayer player) {
        if (!player.isSpectator() && this.getBlockType() instanceof BlockCustomChest) {
        	if(!player.getEntityWorld().isRemote)
        		this.syncPlayerUsingNum(-1);
        }
    }
    
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("inventory", inventory.serializeNBT());
		compound.setTag("lock", lock.serializeNBT());
		compound.setTag("owner", ownerHandler.serializeNBT());
		compound.setBoolean("locked", locked);
		
		if (this.hasCustomName()) {
			compound.setString("customName", customName);
        }
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		this.lock.deserializeNBT(compound.getCompoundTag("lock"));
		this.ownerHandler.deserializeNBT(compound.getCompoundTag("owner"));
		this.locked = compound.getBoolean("locked");
		
		if(compound.hasKey("customName")) {
			this.customName = compound.getString("customName");
		}
		super.readFromNBT(compound);
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		this.lidAngle = 0;
		this.prevLidAngle = 0;
		this.sync();
	}
	
	@Override
	public ItemStackHandler getInventory() {
		return this.inventory;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityOwner.OWNER_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(this.hasCapability(capability, facing)) {
			return capability == ITEM_HANDLER_CAPABILITY? ITEM_HANDLER_CAPABILITY.cast(inventory) : CapabilityOwner.OWNER_HANDLER_CAPABILITY.cast(ownerHandler);
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	public boolean canRenderBreaking() {
	     return true;
	}
	
	public void syncPlayerUsingNum(int num) {
		if(!this.world.isRemote) {
			this.numPlayersUsing += num;
			NetworkRegistry.TargetPoint target = new TargetPoint(this.world.provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 64);
			NBTTagCompound toSend = new NBTTagCompound();
			toSend.setLong("pos", this.pos.toLong());
			toSend.setInteger("numPlayersUsing", this.numPlayersUsing);
			HexianCore.PACKET_HANDLER.sendToAllAround(PacketHandler.CHEST_UPDATE_HANDLER, toSend, target);
		}
	}
	
	@Override
	public void update() {
		if (this.world.isRemote) {
			if(ticksSinceUpdate >= MAX_UPDATE_TIME) {
				ticksSinceUpdate = 0;
				this.sync();
			} else {
				ticksSinceUpdate++;
			}
			
			//Chest lid animation
			this.prevLidAngle = this.lidAngle;

			if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {

				Minecraft.getMinecraft().player.playSound(SoundEvents.BLOCK_CHEST_OPEN, 0.5F,
						this.world.rand.nextFloat() * 0.1F + 0.9F);
			}

			if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
				float f2 = this.lidAngle;

				if (this.numPlayersUsing > 0) {
					this.lidAngle += 0.1F;
				} else {
					this.lidAngle -= 0.1F;
				}

				if (this.lidAngle > 1.0F) {
					this.lidAngle = 1.0F;
				}

				if (this.lidAngle < 0.5F && f2 >= 0.5F) {
					Minecraft.getMinecraft().player.playSound(SoundEvents.BLOCK_CHEST_CLOSE, 0.5F,this.world.rand.nextFloat() * 0.1F + 0.9F);
				}

				if (this.lidAngle < 0.0F) {
					this.lidAngle = 0.0F;
				}
			}
		}
	}
	
	@Override
	public boolean hasCustomName() {
		return this.customName != null && !this.customName.isEmpty();
    }
	
	@Override
    public void setCustomName(String name) {
        this.customName = name;
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : this.getDisplayName().getUnformattedText();
    }
    
	@Override
	public void setLocked(boolean locked) {
		this.locked = locked;
		this.markDirty();
		this.sync();
	}
    
    @Override
    public boolean isLocked() {
    	return this.locked;
    }
    
	@Override
	public ItemStackHandler getLockItemHandler() {
		return this.lock;
	}
	
}
