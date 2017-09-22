package misterpemodder.hc.main.inventory.slot;

import com.google.common.base.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class SlotFiltered extends SlotDisableable {
	
	private Predicate<ItemStack> filter;
	
	public SlotFiltered(IItemHandler itemHandler, int index, int xPosition, int yPosition, boolean visible, Predicate<ItemStack> filter) {
		super(itemHandler, index, xPosition, yPosition, visible);
		this.filter = filter;
	}
	
	public SlotFiltered(IItemHandler itemHandler, int index, int xPosition, int yPosition, boolean visible, boolean enabled, Predicate<ItemStack> filter) {
		super(itemHandler, index, xPosition, yPosition, visible, enabled);
		this.filter = filter;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return filter.apply(stack)? super.isItemValid(stack) : false;
	}

}
