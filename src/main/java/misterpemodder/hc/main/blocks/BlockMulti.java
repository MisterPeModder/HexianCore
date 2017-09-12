package misterpemodder.hc.main.blocks;

import java.util.ArrayList;
import java.util.List;

import misterpemodder.hc.main.blocks.itemblock.ItemBlockMulti;
import misterpemodder.hc.main.blocks.properties.IBlockNames;
import misterpemodder.hc.main.blocks.properties.IBlockValues;
import misterpemodder.hc.main.blocks.properties.IBlockVariant;
import misterpemodder.hc.main.utils.ModUtils;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public abstract class BlockMulti<V extends Enum<V> & IBlockVariant> extends BlockBase {
	
	protected V[] variants;
	private String suffix;
	

	public BlockMulti(IBlockNames names, IBlockValues values, String suffix, CreativeTabs tab) {
		super(names, values, tab);
		this.variants = getVariants();
		this.suffix = suffix;
		
		this.itemBlock = new ItemBlockMulti(this);
		itemBlock.setRegistryName(this.getRegistryName());
	}
	
	public abstract V[] getVariants();
	
	public abstract IProperty<V> getPropertyVariant();
	
	@Override
	protected List<IProperty<?>> getProperties() {
		ArrayList<IProperty<?>> list = new ArrayList<>();
		list.addAll(super.getProperties());
		list.add(getPropertyVariant());
		return list;
	}
	
	public String getSuffix() {
		return suffix;
	}

	public V getVariant(int meta) {
		for(V variant : this.variants) {
			if(meta == variant.getMeta()) {
				return variant;
			}
		}
		return variants[0];
	}
	
	public V getVariant(IBlockState state) {
		return this.getVariant(this.getMetaFromState(state));
	}
	
	public IBlockState getStateFromVariant(IBlockVariant variant) {
		return this.getStateFromMeta(variant.getMeta());
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
		for(IBlockVariant type : variants) {
			 list.add(new ItemStack(itemIn, 1, type.getMeta()));
		}
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(world.getBlockState(pos)));
	}
	
	public boolean isValidVariant(int meta) {
		boolean valid = false;
		for(IBlockVariant type : variants) {
			if(type.getMeta() == meta) {
				valid = true;
				break;
			}
		}
		return valid;
	}
	
	@Override
	public int damageDropped(IBlockState state) {
	    return getMetaFromState(state);
	}
	
	@Override
	public void registerItemRender() {
		int vNum = variants.length;
		for(int i=0; i<vNum; i++) {
			String name = variants[i].getName();
			ModelResourceLocation location = new ModelResourceLocation(ModUtils.activeModContainer().getModId() + ":" + name + suffix , name);
			ModelLoader.setCustomModelResourceLocation(this.getItemBlock(),  variants[i].getMeta(), location);
		}
	}
	
	protected static int getMaxMeta(IBlockVariant[] blockVariants) {
		int m = 0;
		for(IBlockVariant variant : blockVariants) {
			m = m < variant.getMeta()? variant.getMeta() : m;
		}
		return m;
	}

}
