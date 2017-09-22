package misterpemodder.hc.main.capabilty.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.IItemHandler;

/**
 * Represents a part of a bigger {@link IItemHandler}.
 * Mostly used for inventories that have a slider.
 */
public class ItemHandlerPart implements IItemHandler {
	
	protected final IItemHandler mainHandler;
	public final int maxSize;
	private int startIndex;
	
	public ItemHandlerPart(IItemHandler mainHandler, int maxSize) {
		this(mainHandler, maxSize, 0);
	}
	
	public ItemHandlerPart(IItemHandler mainHandler, int maxSize, int startIndex) {
		this.mainHandler = mainHandler;
		this.maxSize = Math.max(0, maxSize);
		this.setStartIndex(startIndex);
	}
	
	public int getStartIndex() {
		return this.startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = MathHelper.clamp(startIndex, 0, this.mainHandler.getSlots() - 1);
	}

	@Override
	public int getSlots() {
		return Math.min(this.mainHandler.getSlots(), maxSize);
	}
	
	private int getSlotIndex(int relativeIndex) {
		int index = this.startIndex + relativeIndex;
		if(relativeIndex >= getSlots() || index >= this.mainHandler.getSlots())
			return -1;
		return index;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		int index = getSlotIndex(slot);
		return index == -1? null : this.mainHandler.getStackInSlot(index);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		int index = getSlotIndex(slot);
		return index == -1? stack : this.mainHandler.insertItem(index, stack, simulate);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		int index = getSlotIndex(slot);
		return index == -1? ItemStack.EMPTY : this.mainHandler.extractItem(index, amount, simulate);
	}

	@Override
	public int getSlotLimit(int slot) {
		int index = getSlotIndex(slot);
		return index == -1? 0 : this.mainHandler.getSlotLimit(index);
	}

}
