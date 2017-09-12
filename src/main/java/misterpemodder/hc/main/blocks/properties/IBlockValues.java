package misterpemodder.hc.main.blocks.properties;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;

/**
 * Contains most of the values that a blocks has such as hardness, material, etc...
 */
public interface IBlockValues {
	
	public default float getHardness(IBlockState state) {
		return 1.0F;
	}
	public default float getResistance(IBlockState state) {
		return 1.0F;
	}
	public default SoundType getSoundType(IBlockState state) {
		return SoundType.STONE;
	}
	public default Material getMaterial(@Nullable IBlockState state) {
		return Material.ROCK;
	}
	public default MapColor getMapColor(@Nullable IBlockState state) {
		return MapColor.STONE;
	}
	public default float getSlipperiness(IBlockState state) {
		return 0.6F;
	}
	public default float getLightLevel(IBlockState state) {
		return 0;
	}
	public default int getLightOpacity(IBlockState state) {
		return 255;
	}
	public default boolean getUseDefaultTab() {
		return true;
	}
	public default BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.SOLID;
	}
	public default boolean isFullCube(IBlockState state) {
        return true;
    }
    public default boolean isOpaqueCube(IBlockState state){
    	return true;
    }
    public default String getHarvestTool(IBlockState state){
    	return "pickaxe";
    }
    public default int getHarvestLevel(IBlockState state){
    	return 0;
    }

}
