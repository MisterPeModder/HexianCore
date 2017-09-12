package misterpemodder.hc.main.blocks.itemblock;

import misterpemodder.hc.main.HCRefs;
import misterpemodder.hc.main.blocks.BlockMulti;
import misterpemodder.hc.main.blocks.properties.IBlockVariant;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockMulti extends ItemBlockBase {
	
	protected int[] variantsMeta;
	
	public <V extends Enum<V> & IBlockVariant> ItemBlockMulti(BlockMulti<V> block) {
		super(block);
		this.variantsMeta = getVariantsMeta(block.getVariants());
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}
	
	private static <V extends Enum<V> & IBlockVariant> int[] getVariantsMeta(V[] variants) {
		int len = variants.length;
		int[] v = new int[len];
		for(int i=0;i<len;i++) {
			v[i] = variants[i].getMeta();
		}
		return v;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
		for(int i = 0, len = variantsMeta.length; i<len; i++) {
			subItems.add(new ItemStack(itemIn, 1, variantsMeta[i]));
		}
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public String getUnlocalizedName(ItemStack stack) {
		int meta = stack.getMetadata();
		String type = ((IBlockVariant) ((BlockMulti)this.getBlock()).getVariant(meta)).getUnlocalizedName();
		String name = Block.getBlockFromItem(stack.getItem()).getUnlocalizedName()+'.'+type;
		return ((BlockMulti) getBlock()).isValidVariant(meta)? name:HCRefs.DEFAULT_ITEM_NAME;
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		int meta = stack.getMetadata();
		EnumRarity r = ((IBlockVariant) ((BlockMulti<?>)this.getBlock()).getVariant(meta)).getRarity();
		return r == EnumRarity.COMMON? super.getRarity(stack) : r;
	}
	
}
