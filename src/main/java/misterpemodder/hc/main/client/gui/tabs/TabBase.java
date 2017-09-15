package misterpemodder.hc.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.MutablePair;

import misterpemodder.hc.main.HexianCore;
import misterpemodder.hc.main.client.gui.GuiContainerBase;
import misterpemodder.hc.main.client.gui.RecipeClickableAreaHC;
import misterpemodder.hc.main.inventory.ContainerBase;
import misterpemodder.hc.main.inventory.slot.IHidableSlot;
import misterpemodder.hc.main.tileentity.TileEntityContainerBase;
import misterpemodder.hc.main.utils.ResourceLocationHC;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The inventory tab class.
 *
 * @param <C> The Container
 * @param <TE> The TileEntity
 */
public abstract class TabBase<C extends ContainerBase<TE>, TE extends TileEntityContainerBase> {
	
	//tab ids
	public static final String INFO_TAB_ID = "hc.info";
	public static final String IO_TAB_ID = "hc.io";
	public static final String MISC_TAB_ID = "hc.misc";
	public static final String SECURITY_TAB_ID = "hc.security";
	public static final String REDSTONE_TAB_ID = "hc.redstone";
	public static final String PLAYER_INV_TAB_ID = "hc.playerInv";
	public static final String ARMOR_INV_TAB_ID = "hc.armorInv";
	
	public static final int WIDTH = 32;
	public static final int HEIGHT = 28;
	public static final ResourceLocation DEFAULT_TAB_LOCATION = new ResourceLocationHC("textures/gui/container/tabs.png");
	
	protected TabPos pos;
	protected GuiContainerBase<C, TE> guiContainer;
	protected List<GuiButton> buttons;
	
	protected TabBase(TabPos pos) {
		this.pos = pos;
		buttons = new ArrayList<>();
	}
	
	/**
	 * Returns the unique id of this tab.
	 * It is recommended to format your tab id like this:
	 * {@code <modId>.<tabId>}. If this tab is a main tab then it should be formated like this:
	 * {@code <modId>.main.<tabId>}	 
	 * 
	 * @return The uid.
	 */
	public abstract String getTabID();
	
	public abstract String getUnlocalizedName();
	
	/**
	 * @return The ItemStack to be used as the tab's icon.
	 */
	public abstract ItemStack getItemStack();
	
	public abstract TabTexture getTabTexture();
	
	/**
	 * This methods determines if the given {@link IHidableSlot} should be displayed.
	 * 
	 * @param slot - The hidable slot.
	 * @return True if this slot should be displayed, false otherwise.
	 */
	public abstract boolean shouldDisplaySlot(IHidableSlot slot);
	
	public MutablePair<TabBase<C, TE>, TabBase<C, TE>> forceTabConfig() {
		return this.guiContainer.getSelectedTabs();
	}
	
	public TabPos getTabPos() {
		return this.pos;
	}
	
	public void setGuiContainer(GuiContainerBase<C, TE> guiContainer) {
		this.guiContainer = guiContainer;
	}
	
	public Point getPos() {
		TabPos tp = getTabPos();
		TabTexture texture = getTabTexture();
		int px = tp == TabPos.TOP_RIGHT || tp == TabPos.BOTTOM_RIGHT? texture.dim.width-GuiContainerBase.TAB_OFFSET : GuiContainerBase.TAB_OFFSET-TabBase.WIDTH;
		int py = tp == TabPos.BOTTOM_LEFT || tp == TabPos.BOTTOM_RIGHT? guiContainer.getBottomPartPos()-this.guiContainer.getGuiTop() : 0;
		
		for(TabBase<C, TE> t : guiContainer.getRegisteredTabs()) {
			if(t == this) break;
			if(t.getTabPos() == this.getTabPos()) {
				py += HEIGHT + 1;
			}
		}
		
		return new Point(px, py);
	}
	
	public C getContainer() {
		return this.guiContainer.container;
	}
	
	/**
	 * Use this method to initialize your buttons.
	 * 
	 * @param topX - The x coordinate of the top-left corner of this tab.
	 * @param topY - The y coordinate of the top-left corner of this tab.
	 */
	public void initButtons(int topX, int topY) {}
	
	public void onGuiClosed() {}
	
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {}
	
	public boolean keyTyped(char typedChar, int keyCode) throws IOException {
		return false;
	}
	
	public List<GuiButton> getButtonsList() {
		return this.buttons;
	}
	
	public <T extends GuiButton> void onButtonClicked(T button) {}
	
	@SideOnly(Side.CLIENT)
	public static void sendButtonPacket(String tabId, int buttonId, WorldClient world, BlockPos pos, NBTTagCompound data) {
		NBTTagCompound toSend = new NBTTagCompound();
		
		toSend.setLong("pos", pos.toLong());
		toSend.setInteger("world_dim_id", world.provider.getDimension());
		toSend.setString("tab_id", tabId);
		toSend.setInteger("button_id", buttonId);
		toSend.setTag("info", data);
		HexianCore.PACKET_HANDLER.sendToServer(ButtonClickHandler.BUTTON_CLICK_HANDLER, toSend);
	}
	
	public void updateButtons() {}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {}
	
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {}
	
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {}
	
	public boolean hasRecipeClickableAreas() {
		return false;
	}
	
	public @Nullable RecipeClickableAreaHC[] getRecipeClickableAreas() {
		return null;
	}
	
	protected final TE getTileEntity() {
		return this.guiContainer.container.getTileEntity();
	}
		
	public static class TabTexture {
		public final ResourceLocation tabTexture;
		public final ResourceLocation screenTexture;
		public final Point enabledCoords;
		public final Point disabledCoords;
		public final Dimension dim;
		public final Dimension textureSize;
		
		public TabTexture(ResourceLocation tabTexture, Point enabledCoords, Point disabledCoords, ResourceLocation screenTexture, Dimension dim) {
			this(tabTexture, enabledCoords, disabledCoords, screenTexture, dim, null);
		}
		
		public TabTexture(ResourceLocation tabTexture, Point enabledCoords, Point disabledCoords, ResourceLocation screenTexture, Dimension dim, Dimension textureSize) {
			this.tabTexture = tabTexture;
			this.screenTexture = screenTexture;
			this.enabledCoords = enabledCoords;
			this.disabledCoords = disabledCoords;
			this.dim = dim;
			this.textureSize = textureSize == null? new Dimension(256, 128) : textureSize;
		}
	}
	
	public static enum TabPos {
		TOP_RIGHT,
		TOP_LEFT,
		BOTTOM_RIGHT,
		BOTTOM_LEFT
	}
	
}
