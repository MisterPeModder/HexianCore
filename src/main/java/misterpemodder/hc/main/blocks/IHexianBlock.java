package misterpemodder.hc.main.blocks;

import misterpemodder.hc.main.blocks.properties.IBlockNames;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;

public interface IHexianBlock {
	
	default void registerItemRender() {
		ModelResourceLocation location = new ModelResourceLocation(((Block)this).getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(this.getItemBlock(), 0, location);
	}
	default void registerOreDict(){}
	
	default boolean hasOwnItemBlock() {
		return true;
	}
	
	default ItemBlock getItemBlock() {
		return null;
	}
	
	default IBlockNames getNames() {
		return new IBlockNames() {};
	}
	
	default boolean hasEffect(ItemStack stack) {
		return false;
	}
	
}
