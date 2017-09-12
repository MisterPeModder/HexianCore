package misterpemodder.hc.main.items.properties;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.item.EnumRarity;

public abstract class ItemVariant {
	
	private String name;
	private String unlocalizedName;
	private String[] oreDictNames;
	private EnumRarity rarity;
	
	public ItemVariant(String name, String unlocalizedName, String... oreDictNames) {
		this(name, unlocalizedName, null, oreDictNames);
	}
	
	public ItemVariant(String name, String unlocalizedName, @Nullable EnumRarity rarity, String... oreDictNames) {
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.oreDictNames = oreDictNames;
		this.rarity = rarity;
	}
	
	public abstract <T extends ItemVariant> List<T> getVariants();
	
	public String getName() {
		return this.name;
	}
	
	public String getUnlocalizedName() {
		return this.unlocalizedName;
	}
	
	public int getMeta() {
		int meta = this.getVariants().indexOf(this);
		return meta < 0? 0 : meta;
	}

	public String[] getOreDictNames() {
		return this.oreDictNames;
	}
	
	public EnumRarity getRarity() {
		return this.rarity == null? EnumRarity.COMMON : rarity;
	}

}
