package misterpemodder.hc.main.inventory.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;

public class SlotHidableInventory extends Slot implements IDisableableSlot{
	
	private boolean enabled;
	private boolean visible;

	public SlotHidableInventory(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		this(inventoryIn, index, xPosition, yPosition, true);
	}
	
	public SlotHidableInventory(IInventory inventoryIn, int index, int xPosition, int yPosition, boolean enabled) {
		super(inventoryIn, index, xPosition, yPosition);
		this.visible = false;
		this.enabled = enabled;
	}
	
	@Override
	public boolean canBeHovered() {
		return enabled && visible;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	@Override
	public void setEnabled(boolean enable) {
		this.enabled = enable;
	}
	
	public static class SlotHidableCrafting extends SlotCrafting implements IDisableableSlot {

		private boolean enabled;
		private boolean visible;
		
		public SlotHidableCrafting(EntityPlayer player, InventoryCrafting craftingInventory, IInventory inventoryIn,int slotIndex, int xPosition, int yPosition) {
			this(player, craftingInventory, inventoryIn, slotIndex, xPosition, yPosition, true);
		}
		
		public SlotHidableCrafting(EntityPlayer player, InventoryCrafting craftingInventory, IInventory inventoryIn,int slotIndex, int xPosition, int yPosition, boolean enabled) {
			super(player, craftingInventory, inventoryIn, slotIndex, xPosition, yPosition);
			this.visible = false;
			this.enabled = enabled;
		}
		
		@Override
		public boolean canBeHovered() {
			return enabled && visible;
		}

		@Override
		public void setVisible(boolean visible) {
			this.visible = visible;
		}
		
		@Override
		public void setEnabled(boolean enable) {
			this.enabled = enable;
		}
		
	}

}
