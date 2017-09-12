package misterpemodder.hc.main.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;

public interface IBlockTileEntity<TE extends TileEntity> extends ITileEntityProvider{
	
	Class<TE> getTileEntityClass();

}
