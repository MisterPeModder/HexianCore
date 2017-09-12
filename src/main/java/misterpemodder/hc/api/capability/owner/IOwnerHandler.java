package misterpemodder.hc.api.capability.owner;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Indicates if something has an owner.
 * @author MisterPeModder
 */
public interface IOwnerHandler {

	/**
	 * Used to check there is an owner
	 * 
	 * @return true if there is an owner.
	 */
	boolean hasOwner();
	
	/**
	 * Returns the owner's name even if if he is not connected to the server.
	 * @return onwer's name if there is one, null is there is not.
	 */
	@Nullable
	String getOwnerName();
	
	/**
	 * Returns the EntityPlayer corresponding to the owner.
	 * May not work if the player is not connected to the server.
	 * @param world The world the containing object is in.
	 * @return EntityPlayer corresponding to owner, if there is one.
	 */
	@Nullable
	EntityPlayer getOwner(World world);
	
	/**
	 * Sets the owner to the passed EntityPlayer.
	 * @param player The new owner. CAN be null, null is used to clear owner.
	 */
	void setOwner(@Nullable EntityPlayer player);
	
	/**
	 * Compares the owner to the specified player.
	 * @param player The player to be compared.
	 * @return true if the player is the owner, false if not.
	 */
	boolean isOwner(EntityPlayer player);
	
	/**
	 * Removes the owner.
	 */
	void clearOwner();
}
