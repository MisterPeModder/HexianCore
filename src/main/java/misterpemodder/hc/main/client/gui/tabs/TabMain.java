package misterpemodder.hc.main.client.gui.tabs;

import misterpemodder.hc.main.inventory.ContainerBase;
import misterpemodder.hc.main.inventory.slot.IHidableSlot;
import misterpemodder.hc.main.inventory.slot.SlotHidable;
import misterpemodder.hc.main.tileentity.TileEntityContainerBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IWorldNameable;

/**
 * The main inventory tab class.
 *
 * @param <C> The Container
 * @param <TE> The TileEntity
 */
public abstract class TabMain<C extends ContainerBase<TE>, TE extends TileEntityContainerBase> extends TabBase<C, TE> {
	
	protected TabMain() {
		super(TabPos.TOP_RIGHT);
	}
	
	@Override
	public ItemStack getItemStack() {
		return new ItemStack(getTileEntity().getBlockType());
	}

	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		TileEntityContainerBase te = guiContainer.container.getTileEntity();
		
		String dispName = "";
		
		if(te instanceof IWorldNameable) {
			dispName = ((IWorldNameable)te).getName();
		} else {
			dispName = te.getDisplayName().getUnformattedText();
		}
		guiContainer.getFontRenderer().drawString(dispName, getTitleX(), getTitleY(), 4210752);
	}
	
	protected int getTitleX() {
		return 8;
	}
	
	protected int getTitleY() {
		return 6;
	}
	
	public boolean shouldDisplaySlot(IHidableSlot slot) {
		return slot instanceof SlotHidable && ((SlotHidable)slot).getItemHandler() == this.guiContainer.container.getTileEntity().getInventory();
	}
	
	@Override
	public String getUnlocalizedName() {
		return "gui.tab.main.name";
	}

}
