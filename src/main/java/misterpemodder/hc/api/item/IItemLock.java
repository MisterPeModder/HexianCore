package misterpemodder.hc.api.item;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Represents an item that can be used as a lock.
 * @author MisterPeModder
 */
public interface IItemLock {
	
	/**
     * Called when a lockable container has been exploded.
     * This can be used to decrease durability or for other purposes.
     *
     * @param lockStack - The ItemStack that is being exploded.
     * @param random
     * @return EnumActionResult.SUCCES if the lock has been broken,
     *         EnumActionResult.FAIL or PASS if not.
     */
	EnumActionResult attemptBreak(ItemStack lockStack, Random random);
	
	/**
	 * Called after the lock has been broken.
	 * This can be used to warn the owner his block has been unlocked or for other purposes.
	 * 
	 * @param world
	 * @param pos
	 * @param entity - The entity that has triggered the explosion, if there is one.
	 */
	void onLockBroken(World world, BlockPos pos, @Nullable EntityLivingBase entity);
	
	/**
	 * Called to know if the lock is broken.
	 * 
	 * @param stack - The lock ItemStack
	 * @return true if the lock is broken, false if not.
	 */
	boolean isBroken(ItemStack stack);
	
}
