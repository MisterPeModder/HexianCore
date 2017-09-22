package misterpemodder.hc.main.client.gui.tabs;

import misterpemodder.hc.main.inventory.ContainerBase;
import misterpemodder.hc.main.inventory.slot.IHidableSlot;
import misterpemodder.hc.main.inventory.slot.SlotDisableable;
import misterpemodder.hc.main.tileentity.TileEntityContainerBase;
import misterpemodder.hc.main.utils.ResourceLocationHC;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class TabPlayerInventory<C extends ContainerBase<TE>, TE extends TileEntityContainerBase> extends TabBase<C, TE>{

	public TabPlayerInventory() {
		super(TabPos.BOTTOM_RIGHT);
	}
	
	@Override
	public String getTabID() {
		return TabBase.PLAYER_INV_TAB_ID;
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		guiContainer.getFontRenderer().drawString(guiContainer.container.getPlayerInv().getInventoryPlayer().getDisplayName().getUnformattedText(), 26, guiContainer.getBottomPartPos()-guiContainer.getGuiTop()+7, 4210752);
	}
	
	public boolean shouldDisplaySlot(IHidableSlot slot) {
		return slot instanceof SlotDisableable && ((SlotDisableable)slot).getItemHandler() == this.guiContainer.container.getPlayerInv();
	}

	@Override
	public String getUnlocalizedName() {
		return "gui.tab.playerInv.name";
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemStack(Blocks.CHEST);
	}

	@Override
	public TabTexture getTabTexture() {
		return new TabTexture(new ResourceLocationHC("textures/gui/container/player_inventory.png"));
	}

}
