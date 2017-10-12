package misterpemodder.hc.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;

import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import misterpemodder.hc.main.client.gui.RecipeClickableAreaHC;
import misterpemodder.hc.main.inventory.ContainerBase;
import misterpemodder.hc.main.inventory.slot.IHidableSlot;
import misterpemodder.hc.main.inventory.slot.SlotDisableable;
import misterpemodder.hc.main.tileentity.TileEntityContainerBase;
import misterpemodder.hc.main.utils.ResourceLocationHC;
import misterpemodder.hc.main.utils.StringUtils;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class TabArmorInventory<C extends ContainerBase<TE>, TE extends TileEntityContainerBase> extends TabBase<C, TE> {
	
	public TabArmorInventory() {
		super(TabPos.BOTTOM_RIGHT);
	}
	
	@Override
	public String getTabID() {
		return TabBase.ARMOR_INV_TAB_ID;
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		guiContainer.getFontRenderer().drawString(StringUtils.translate(getUnlocalizedName()), 10, guiContainer.getBottomPartPos()-guiContainer.getGuiTop()+5, 4210752);
	}
	
	@Override
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		int i = guiContainer.getGuiLeft();
	    int j = guiContainer.getGuiTop();
	    GuiInventory.drawEntityOnScreen(i + 35, j + 78 + guiContainer.container.BPART_OFFSET, 30, (float)(i + 35) - mouseX, (float)(j + 78 + guiContainer.container.BPART_OFFSET - 50) - mouseY, guiContainer.mc.player);
	}
	
	public boolean shouldDisplaySlot(IHidableSlot slot) {
		C container = getContainer();
		if(slot instanceof SlotItemHandler) {
			IItemHandler h = ((SlotDisableable)slot).getItemHandler();
			boolean flag1 = h == container.getPlayerOffandInv() || h == container.getPlayerArmorInv();
			boolean flag2 = h == container.baublesInv;
			return flag1 || flag2;
		}
		else if(slot instanceof Slot){
			IInventory i = ((Slot)slot).inventory;
			return i == container.craftResult || i == container.craftMatrix;
		} else {
			return false;
		}
	}

	@Override
	public String getUnlocalizedName() {
		return "gui.tab.playerArmorInv.name";
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemStack(Items.IRON_CHESTPLATE);
	}

	@Override
	public TabTexture getTabTexture() {
		String str = getContainer().isBaublesCompatEnabled()? "textures/gui/container/armor_inventory_baubles.png" : "textures/gui/container/armor_inventory.png";
		return new TabTexture(DEFAULT_TAB_LOCATION, new Point(0, 28), new Point(32, 28), new ResourceLocationHC(str), new Dimension(212, 100));
	}
	
	@Override
	public boolean hasRecipeClickableAreas() {
		return true;
	}
	
	@Override
	public RecipeClickableAreaHC[] getRecipeClickableAreas() {
		return new RecipeClickableAreaHC[]{new RecipeClickableAreaHC(guiContainer.getBottomPartPos()+72, guiContainer.getBottomPartPos()+90, guiContainer.getGuiLeft()+156, guiContainer.getGuiLeft()+172, VanillaRecipeCategoryUid.CRAFTING)};
	}
}
