package misterpemodder.hc.main.capabilty.item;

import misterpemodder.hc.api.block.ILockable;
import misterpemodder.hc.api.item.IItemLock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerLockable extends ItemStackHandler {
	
	private ILockable lockable;
	
	public ItemStackHandlerLockable(ILockable lockable) {
		this(lockable, 1);
	}
	
	public ItemStackHandlerLockable(ILockable lockable, int size) {
		super(size);
		this.lockable = lockable;
	}
	
	@Override
	protected void onContentsChanged(int slot) {
		super.onContentsChanged(slot);
		ItemStack stack = getStackInSlot(slot);
		if(stack.getItem() instanceof IItemLock){
			lockable.setLocked(!((IItemLock)stack.getItem()).isBroken(stack));
		} else {
			lockable.setLocked(false);
		}
	}
	
}
