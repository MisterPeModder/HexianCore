package misterpemodder.hc.main.blocks.properties;

import net.minecraft.item.EnumRarity;

/**
 * Contains all the ids of a block.
 */
public interface IBlockNames {
	
	public default String getUnlocalizedName() {
		return "blockGeneric";
	}
	
	public default String getRegistryName() {
		return "generic_block";
	}
	
	public default String[] getOreDictNames() {
		return new String[0];
	}
	
	public default EnumRarity getRarity() {
		return EnumRarity.EPIC;
	}

}
