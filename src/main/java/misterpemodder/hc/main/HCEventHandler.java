package misterpemodder.hc.main;

import misterpemodder.hc.api.block.ILockable;
import misterpemodder.hc.api.capability.owner.IOwnerHandler;
import misterpemodder.hc.main.apiimpl.capability.owner.CapabilityOwner;
import misterpemodder.hc.main.tileentity.TileEntityContainerBase;
import misterpemodder.hc.main.utils.StringUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = HCRefs.MOD_ID)
public final class HCEventHandler {
	
	@SubscribeEvent
	public static void blockBreakEvent(BlockEvent.BreakEvent event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		TileEntity t = world.getTileEntity(pos);
		
		if(t != null) {
			
			boolean flag1 = t instanceof ILockable && ((ILockable)t).isLocked();
			
			IOwnerHandler ownerHandler = t.getCapability(CapabilityOwner.OWNER_HANDLER_CAPABILITY, null);
			boolean flag2 = false;
			if(ownerHandler != null) {
				flag2 = ownerHandler.hasOwner() && !ownerHandler.isOwner(event.getPlayer());
			}
			
			if(flag1 && flag2) {
				IBlockState state = world.getBlockState(pos);
				event.getPlayer().sendMessage(new TextComponentString(TextFormatting.RED + StringUtils.translate("protectedBlock.noBreaking", state.getBlock().getItem(world, pos, state).getDisplayName())));
				event.setCanceled(true);
				if(t instanceof TileEntityContainerBase) {
					((TileEntityContainerBase)t).sync();
					t.markDirty();
				}
			}
		}
		
	}

}
