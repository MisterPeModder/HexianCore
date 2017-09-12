package misterpemodder.hc.main.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

public class ItemBase extends Item implements IHexianItem {
	
	protected IItemNames names;
	
	@Override
	public void registerRender() {
		ModelResourceLocation location = new ModelResourceLocation(Loader.instance().activeModContainer().getModId() + ":" + names.getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(this, 0, location);
	}
	
	public void registerOreDict() {
		String[] oreDictNames = this.names.getOreDictNames();
		if(oreDictNames.length == 0) {
			return;
		}
		for (String str : oreDictNames) {
			OreDictionary.registerOre(str, this);
		}
	}
	
	public ItemBase(IItemNames names) {
		this.names = names;
		setUnlocalizedName(names.getUnlocalizedName());
		setRegistryName(names.getRegistryName());
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return this.names.getRarity();
	}
}
