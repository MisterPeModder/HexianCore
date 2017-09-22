package misterpemodder.hc.main.inventory.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotDisableable extends SlotItemHandler implements IDisableableSlot {
	
	private boolean enabled;
	private boolean visible;

	public SlotDisableable(IItemHandler itemHandler, int index, int xPosition, int yPosition, boolean visible) {
		this(itemHandler, index, xPosition, yPosition, visible, visible);
	}
	
	public SlotDisableable(IItemHandler itemHandler, int index, int xPosition, int yPosition, boolean visible, boolean enabled) {
		super(itemHandler, index, xPosition, yPosition);
		this.enabled = enabled;
		this.visible = visible;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return enabled? super.isItemValid(stack) : false;
	}
	
    @Override
    public ItemStack getStack() {
        return enabled? super.getStack() : ItemStack.EMPTY;
    }
    
    @Override
    public void putStack(ItemStack stack) {
    	if(enabled) super.putStack(stack);
    }
    
    @Override
    public int getSlotStackLimit() {
    	return enabled? super.getSlotStackLimit() : 0;
    }
    
    @Override
    public int getItemStackLimit(ItemStack stack) {
    	return enabled? super.getItemStackLimit(stack) : 0;
    }
    
    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
    	return enabled? super.canTakeStack(playerIn) : false;
    }
    
    @Override
    public ItemStack decrStackSize(int amount) {
    	return enabled? super.decrStackSize(amount) : ItemStack.EMPTY;
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
