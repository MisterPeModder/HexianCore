package misterpemodder.hc.main.blocks.properties;

import net.minecraft.block.material.MapColor;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.IStringSerializable;

public interface IBlockVariant extends IStringSerializable{

	public String getUnlocalizedName();
	public int getMeta();
	public MapColor getMapColor();
	public default EnumRarity getRarity() {
		return EnumRarity.COMMON;
	}

}
