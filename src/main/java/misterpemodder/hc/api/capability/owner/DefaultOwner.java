package misterpemodder.hc.api.capability.owner;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Default implementation of {@link IOwnerHandler}.
 * @author MisterPeModder
 */
public class DefaultOwner implements IOwnerHandler {

	@Override
	public boolean hasOwner() {
		return false;
	}

	@Override
	public String getOwnerName() {
		return "";
	}

	@Override
	public EntityPlayer getOwner(World world) {
		return null;
	}

	@Override
	public void setOwner(@Nullable EntityPlayer player) {
		
	}

	@Override
	public boolean isOwner(EntityPlayer player) {
		return getOwner(player.getEntityWorld()).isEntityEqual(player);
	}
	
	@Override
	public void clearOwner() {
		
	}

}
