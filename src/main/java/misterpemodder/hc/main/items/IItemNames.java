package misterpemodder.hc.main.items;

import net.minecraft.item.EnumRarity;

/**
 * Contains all the ids of an item.
 */
public interface IItemNames {
	
	public default String getUnlocalizedName() {
		return "itemGeneric";
	}
	
	public default String getRegistryName() {
		return "generic_item";
	}
	
	public default String[] getOreDictNames() {
		return new String[0];
	}
	
	public default EnumRarity getRarity() {
		return EnumRarity.EPIC;
	}

}
