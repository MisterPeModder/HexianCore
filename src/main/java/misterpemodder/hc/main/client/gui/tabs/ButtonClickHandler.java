package misterpemodder.hc.main.client.gui.tabs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.tuple.Pair;

import misterpemodder.hc.main.network.IPacketDataHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public final class ButtonClickHandler {

	private static final List<Pair<Predicate<String>, IButtonClickConsumer>> HANDLERS = new ArrayList<>();

	/**
	 * Registers a new button click handler.
	 * @param condition Determines if the given tab id is valid.
	 * @param handler Called when the condition predicates returns true.
	 */
	public static void registerHandler(Predicate<String> condition, IButtonClickConsumer handler) {
		HANDLERS.add(Pair.of(condition, handler));
	}
	
	/**
	 * <p> Handler type: client to server
	 * 
	 * <p> NBT tags:
	 * <ul>
	 * 	<li>pos: BlockPos serialized into long
	 * 	<li>world_dim_id: integer
	 * 	<li>tab_id: String, id of the tab that contains the button that has been clicked.
	 * 		use {@link TabBase}.MISC_TAB_ID if this tab does not exist.
	 * 	<li>button_id: integer
	 * 	<li>info: NBTTagCompound additional data
	 * </ul>
	 */
	public static final IPacketDataHandler BUTTON_CLICK_HANDLER = data -> {
		WorldServer world = DimensionManager.getWorld(data.getInteger("world_dim_id"));
		BlockPos pos = BlockPos.fromLong(data.getLong("pos"));
		String tId = data.getString("tab_id");
		int bId = data.getInteger("button_id");
		NBTTagCompound info = data.getCompoundTag("info");
		
		TileEntity te = world.getTileEntity(pos);
		
		for(Pair<Predicate<String>, IButtonClickConsumer> pair : HANDLERS) {
			if(pair.getLeft().test(tId)) {
				if(pair.getRight().handleClick(bId, world, pos, te, info))
					break;
			}
		}
	};
	
	
	
}
