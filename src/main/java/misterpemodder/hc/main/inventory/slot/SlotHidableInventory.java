package misterpemodder.hc.main.inventory.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;

public class SlotHidableInventory extends Slot implements IHidableSlot{
	
	private boolean enabled;

	public SlotHidableInventory(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
		this.enabled = false;
	}
	
	@Override
	public boolean canBeHovered() {
		return enabled;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public static class SlotHidableCrafting extends SlotCrafting implements IHidableSlot {

		private boolean enabled;
		
		public SlotHidableCrafting(EntityPlayer player, InventoryCrafting craftingInventory, IInventory inventoryIn,int slotIndex, int xPosition, int yPosition) {
			super(player, craftingInventory, inventoryIn, slotIndex, xPosition, yPosition);
			this.enabled = false;
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

}
