package misterpemodder.hc.main.inventory.elements;

import java.awt.Point;

import mezz.jei.api.gui.IDrawable;
import misterpemodder.hc.main.compat.jei.JEICompat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "mezz.jei.api.gui.IDrawable", modid = JEICompat.MOD_ID)
public abstract class ContainerElementBar implements ISyncedContainerElement, IDrawable {
	
	private final ResourceLocation texture;
	
	
	protected int barFillAmount = 0;
	protected boolean drawBackground = true;
	protected boolean drawForeground = true;
	
	/**
	 * For use inside HexianCore tabs
	 * 
	 * @param texture The texture file of this bar, the image must have a size of 256x256.
	 */
	protected ContainerElementBar(ResourceLocation texture) {
		this.texture = texture;
	}
	
	/**
	 * For use as a JEI {@link IDrawable}.
	 * 
	 * @param texture The texture file of this bar, the image must have a size of 256x256.
	 * @param fillPercent A percentage representing how much this bar should be filled. 
	 * @param shouldDrawMinimum If the bar fill amount is very low, should a pixel-wide foreground be drawn anyway?
	 * @param drawBackground Should the background bar be drawn?
	 * @param drawForeground Should the foreground bar be drawn?
	 */
	protected ContainerElementBar(ResourceLocation texture, int fillPercent, boolean shouldDrawMinimum, boolean drawBackground, boolean drawForeground) {
		this(texture);
		int i = (int) ((MathHelper.clamp(fillPercent, 0, 100)/100f) * (getBarSize()-2));
		this.barFillAmount = shouldDrawMinimum && i == 0? 1 : i; 
		
		this.drawBackground = drawBackground;
		this.drawForeground = drawForeground;
	}
	
	public void setFillPercent(int fillPercent, boolean shouldDrawMinimum) {
		int i = (int) ((MathHelper.clamp(fillPercent, 0, 100)/100f) * (getBarSize()-2));
		this.barFillAmount = shouldDrawMinimum && i == 0? 1 : i; 
	}
	
	@Override
	public void draw(Minecraft minecraft) {
		this.draw(minecraft, 0, 0);
	}
	
	public abstract int getBarWidth();
	
	public abstract int getBarHeight();
	
	@Override
	public final int getWidth() {
		return getBarWidth();
	}
	
	@Override
	public final int getHeight() {
		return getBarHeight();
	}

	@Override
	public void draw(Minecraft minecraft, int xOffset, int yOffset) {
		this.drawBar(xOffset, yOffset, this.barFillAmount);
	}
	
	public void drawBar(int x, int y, int fillAmount) {
		GlStateManager.enableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		
		Point backgroundUV = getBackgroundUV();
		Point foregroundUV = getForegroundUV();
		
		if(drawBackground) Gui.drawModalRectWithCustomSizedTexture(x, y, backgroundUV.x, backgroundUV.y, getBarWidth(), getBarHeight(), 256, 256);
		if(drawForeground) Gui.drawModalRectWithCustomSizedTexture(x, y, foregroundUV.x, foregroundUV.y, fillAmount, getBarHeight(), 256, 256);
		
		GlStateManager.disableBlend();
	}
	
	protected abstract Point getBackgroundUV();
	
	protected abstract Point getForegroundUV();
	
	@Override
	public boolean shouldSendDataToClient() {
		return false;
	}
	
	@Override
	public void procData(NBTTagCompound data) {}
	
	@Override
	public NBTTagCompound writeData(NBTTagCompound data) {
		return new NBTTagCompound();
	}
	
	protected int getBarSize() {
		return getBarWidth();
	}

}
