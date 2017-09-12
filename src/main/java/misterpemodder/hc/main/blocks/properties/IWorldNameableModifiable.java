package misterpemodder.hc.main.blocks.properties;

import net.minecraft.world.IWorldNameable;

/**
 * A version of {@code IWorldNameable} that allows its custom name to be changed.
 */
public interface IWorldNameableModifiable extends IWorldNameable{

	public void setCustomName(String name);
	
}
