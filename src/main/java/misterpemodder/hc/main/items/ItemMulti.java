package misterpemodder.hc.main.items;

import java.util.List;

import misterpemodder.hc.main.items.properties.ItemVariant;
import misterpemodder.hc.main.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemMulti<V extends ItemVariant> extends ItemBase {
	
	protected List<V> variants;
	protected String suffix;

	public ItemMulti(IItemNames names, List<V> variants, String suffix) {
		super(names);
		setHasSubtypes(true);
		this.getHasSubtypes();
		this.suffix = suffix;
		this.variants = variants;
	}
	
	@Override
	public void registerRender() {
		int vNum = variants.size();
		for(int i=0; i<vNum; i++) {
			String name = variants.get(i).getName();
			ModelResourceLocation location = new ModelResourceLocation(ModUtils.activeModContainer().getModId() + ":" + name + suffix , name);
			ModelLoader.setCustomModelResourceLocation(this,  variants.get(i).getMeta(), location);
		}
	};
	
	public V getVariant(int meta) {
		for(V variant : this.variants) {
			if(meta == variant.getMeta()) {
				return variant;
			}
		}
		return variants.get(0);
	}
	
	public V getVariant(ItemStack stack) {
		return this.getVariant(stack.getMetadata());
	}
	
	public List<V> getVariants() {
		return this.variants;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int meta = stack.getMetadata();
		String type = getVariant(meta).getUnlocalizedName();
		String name = super.getUnlocalizedName()+'.'+type;
		return name;
	}
	
	@Override
	public void registerOreDict() {
		for(V variant : variants) {
			String[] oreDictNames = variant.getOreDictNames();
			if(oreDictNames.length != 0) {
				for (String str : oreDictNames) {
					OreDictionary.registerOre(str, new ItemStack(this, 1, variant.getMeta()));
				}
			}
		}

	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
		for(int i = 0, len = variants.size(); i<len; i++) {
			subItems.add(new ItemStack(itemIn, 1, i));
		}
	}
	
	public EnumRarity getRarity(ItemStack stack) {
        return this.getVariant(stack).getRarity();
    }

}
