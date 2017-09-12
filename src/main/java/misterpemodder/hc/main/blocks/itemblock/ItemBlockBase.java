package misterpemodder.hc.main.blocks.itemblock;

import misterpemodder.hc.main.blocks.IHexianBlock;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockBase extends ItemBlock {
	
	protected IHexianBlock block;
	
	public <B extends Block & IHexianBlock> ItemBlockBase(B block) {
		super(block);
		this.block = block;
		this.setHasSubtypes(false);
		this.setMaxDamage(0);
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return block.getNames().getRarity();
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return super.hasEffect(stack) || block.hasEffect(stack);
	}
	
}
