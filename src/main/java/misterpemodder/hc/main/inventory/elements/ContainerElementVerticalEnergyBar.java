package misterpemodder.hc.main.inventory.elements;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import misterpemodder.hc.main.utils.ResourceLocationHC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerElementVerticalEnergyBar extends ContainerElementBar {
	
	public static final ResourceLocation TEXTURE = new ResourceLocationHC("textures/gui/container/bars.png");
	
	private final IEnergyStorage energy;
	
	private int storedEnergy;
	private int capacity;
	
	private final Supplier<Integer> getEnergyRate;

	public ContainerElementVerticalEnergyBar(IEnergyStorage energy) {
		this(energy, () -> 0);
	}
	
	public ContainerElementVerticalEnergyBar(IEnergyStorage energy, Supplier<Integer> getEnergyRate) {
		super(TEXTURE);
		this.energy = energy;
		this.storedEnergy = energy.getEnergyStored();
		this.capacity = energy.getMaxEnergyStored();
		this.getEnergyRate = getEnergyRate;
	}
	
	public ContainerElementVerticalEnergyBar(int energy, int capacity, boolean shouldDrawMinimum, boolean drawBackground, boolean drawForeground) {
		super(TEXTURE, (energy*100)/(capacity>0? capacity : 1), shouldDrawMinimum, drawBackground, drawForeground);
		this.energy = null;
		this.storedEnergy = energy;
		this.capacity = capacity;
		this.getEnergyRate = () -> 0;
	}
	
	@Override
	protected int getBarSize() {
		return 82;
	}

	@Override
	protected Point getBackgroundUV() {
		return new Point(220, 0);
	}

	@Override
	protected Point getForegroundUV() {
		return new Point(238, 82);
	}
	
	@Override
	public void draw(Minecraft minecraft, int xOffset, int yOffset) {
		this.drawBar(xOffset, yOffset, this.barFillAmount);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBar(int x, int y, int fillAmount) {
		GlStateManager.enableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
		
		Point backgroundUV = getBackgroundUV();
		Point foregroundUV = getForegroundUV();
		
		int w = getBarWidth();
		int h = getBarHeight();
		if(drawBackground) Gui.drawModalRectWithCustomSizedTexture(x, y, backgroundUV.x, backgroundUV.y, w, h, 256, 256);
		if(drawForeground) Gui.drawModalRectWithCustomSizedTexture(x, y+(h-fillAmount-1), foregroundUV.x, (h-fillAmount-1), w, h-2, 256, 256);

		GlStateManager.disableBlend();
	}
	
	@SideOnly(Side.CLIENT)
	public void drawEnergyBar(int x, int y) {
		this.setFillPercent((storedEnergy*100)/(capacity>0? capacity : 1), false);
		this.drawBar(x, y, barFillAmount);
	}
	
	@SideOnly(Side.CLIENT)
	public List<String> getDisplayText() {
		List<String> list = new ArrayList<>();
		list.add(String.format("%d/%d RF", this.storedEnergy, this.capacity));
		int diff = getEnergyRate.get();
		if(diff != 0) {
			String d = diff > 0? TextFormatting.GREEN+"+" : TextFormatting.RED+"";
			list.add(d+diff+" RF/t");
		}
		
		return list;
	}
	
	@Override
	public boolean shouldSendDataToClient() {
		if(energy != null && (storedEnergy != energy.getEnergyStored() || capacity != energy.getMaxEnergyStored())) {
			storedEnergy = energy.getEnergyStored();
			capacity = energy.getMaxEnergyStored();
			return true;
		}
		
		return false;
	}
	
	@Override
	public NBTTagCompound writeData(NBTTagCompound data) {
		data.setInteger("energy", storedEnergy);
		data.setInteger("capacity", capacity);
		return data;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void procData(NBTTagCompound data) {
		this.storedEnergy = data.getInteger("energy");
		this.capacity = data.getInteger("capacity");
	}

	@Override
	public int getBarWidth() {
		return 18;
	}

	@Override
	public int getBarHeight() {
		return 82;
	}
}
