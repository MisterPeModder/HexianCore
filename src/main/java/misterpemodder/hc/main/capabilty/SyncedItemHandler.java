package misterpemodder.hc.main.capabilty;

import misterpemodder.hc.main.tileentity.TileEntityContainerBase;
import net.minecraftforge.items.ItemStackHandler;

public class SyncedItemHandler extends ItemStackHandler {
	
	protected TileEntityContainerBase te;
	
	public SyncedItemHandler(TileEntityContainerBase te, int size) {
		super(size);
		this.te = te;
	}
	
	@Override
	protected void onContentsChanged(int slot) {
		super.onContentsChanged(slot);
		te.sync();
		te.markDirty();
	}
}