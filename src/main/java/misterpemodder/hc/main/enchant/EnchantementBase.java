package misterpemodder.hc.main.enchant;

import misterpemodder.hc.main.utils.StringUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EnchantementBase extends Enchantment {

	public EnchantementBase(ResourceLocation registryName, String unlocalizedName, Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
		this.setRegistryName(registryName);
		this.setName(unlocalizedName);
	}
	
	public boolean hasDescription() {
		String desc = this.getDescription();
		return desc != null && !desc.isEmpty();
	}
	
	@SideOnly(Side.CLIENT)
	public String getDescription() {
		String key = this.getName()+".desc";
		if(I18n.hasKey(key)) {
			return StringUtils.translate(key);
		}
		return "";
	}

}
