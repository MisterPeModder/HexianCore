package misterpemodder.hc.main.utils;

import org.apache.logging.log4j.Logger;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import misterpemodder.hc.main.HexianCore;
import misterpemodder.hc.main.blocks.IBlockTileEntity;
import misterpemodder.hc.main.blocks.IHexianBlock;
import misterpemodder.hc.main.blocks.properties.IHexianBlockList;
import misterpemodder.hc.main.items.ItemBase;
import misterpemodder.hc.main.items.properties.IHexianItemList;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public final class RegistryHelper {
	
	private static final ListMultimap<String, ItemBlock> ITEM_BLOCKS = ArrayListMultimap.create();
	
	private static Logger getModLogger(ModContainer mod) {
		if(ModUtils.isHexianMod(mod)) {
			return ModUtils.getHexianMod(mod).getLogger();
		}
		return HexianCore.instance.getLogger();
	}
	
	public static void registerBlocks(IHexianBlockList[] blocks, IForgeRegistry<Block> registry) {
		registerBlocks(blocks, registry, true);
	}

	public static void registerBlocks(IHexianBlockList[] blocks, IForgeRegistry<Block> registry, boolean storeItemBlocks) {
		ModContainer mod = ModUtils.activeModContainer();
		getModLogger(mod).info("Registering Blocks...");
		String modId = mod.getModId();
		
		for(IHexianBlockList b : blocks) {
			IHexianBlock block = b.getHexianBlock();
			registry.register(b.getBlock());
			
			if(storeItemBlocks && block.hasOwnItemBlock())
				ITEM_BLOCKS.put(modId, block.getItemBlock());
			
			if(block instanceof IBlockTileEntity)
				GameRegistry.registerTileEntity(((IBlockTileEntity<?>)block).getTileEntityClass(), b.getBlock().getRegistryName().toString());
		}
	}
	
	public static void registerItems(IHexianItemList[] items, IForgeRegistry<Item> registry) {
		ModContainer mod = ModUtils.activeModContainer();
		Logger logger = getModLogger(mod);
		logger.info("Registering Items...");
		
		for(IHexianItemList i : items) {
			registry.register(i.getItem());
		}
		
		if(!ITEM_BLOCKS.isEmpty() && !ITEM_BLOCKS.get(mod.getModId()).isEmpty()) {
			logger.info("Registering ItemsBlocks...");
			for(ItemBlock i : ITEM_BLOCKS.get(mod.getModId())) {
				registry.register(i);
			}
		}
	}
	
	public static void registerRenders(IHexianBlockList[] blocks) {
		for(IHexianBlockList bl : blocks) {
			IHexianBlock b = bl.getHexianBlock();
			if(b.hasOwnItemBlock())
				b.registerItemRender();
		}
	}
	
	public static void registerRenders(IHexianItemList[] items) {
		for(IHexianItemList i : items) {
			i.getHexianItem().registerRender();
		}
	}
	
	public static void registerOreDict(IHexianBlockList[] blocks) {
		for(IHexianBlockList b : blocks) {
			b.getHexianBlock().registerOreDict();
		}
	}
	
	public static void registerOreDict(IHexianItemList[] items) {
		for (IHexianItemList i : items) {
			if(i.getItem() instanceof ItemBase) {
				((ItemBase) i.getItem()).registerOreDict();
			}
		}
	}
	
	public static void registerCreativeTabItems(IHexianItemList[] items) {
		for (IHexianItemList i : items) {
			if(i.getHexianItem().isEnabled() && i.getCreativeTab() != null)
				i.getItem().setCreativeTab(i.getCreativeTab());
		}
	}
	
}
