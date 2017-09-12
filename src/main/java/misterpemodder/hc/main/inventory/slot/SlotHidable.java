package misterpemodder.hc.main.inventory.slot;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotHidable extends SlotItemHandler implements IHidableSlot{
	
	private boolean enabled;

	public SlotHidable(IItemHandler itemHandler, int index, int xPosition, int yPosition, boolean enabled) {
		super(itemHandler, index, xPosition, yPosition);
		this.enabled = enabled;
	}
	
	@Override
	public boolean canBeHovered() {
		return enabled;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
