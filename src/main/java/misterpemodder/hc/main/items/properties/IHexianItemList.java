package misterpemodder.hc.main.items.properties;

import misterpemodder.hc.main.items.IHexianItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public interface IHexianItemList {

	public Item getItem();
	public IHexianItem getHexianItem();
	public CreativeTabs getCreativeTab();
	
}
