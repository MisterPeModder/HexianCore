package misterpemodder.hc.main.blocks;

import java.util.Random;

import misterpemodder.hc.api.capability.owner.IOwnerHandler;
import misterpemodder.hc.main.apiimpl.capability.owner.CapabilityOwner;
import misterpemodder.hc.main.blocks.properties.IBlockNames;
import misterpemodder.hc.main.blocks.properties.IBlockValues;
import misterpemodder.hc.main.blocks.properties.IWorldNameableModifiable;
import misterpemodder.hc.main.tileentity.TileEntityContainerBase;
import misterpemodder.hc.main.utils.StringUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public abstract class BlockContainerBase<TE extends TileEntityContainerBase> extends BlockTileEntity<TE> {

	public BlockContainerBase(IBlockNames n, IBlockValues v, CreativeTabs tab) {
		super(n, v, tab);
	}
	
	public ItemStack toItem(TE tileEntity, IBlockState state) {
		NBTTagCompound c = new NBTTagCompound();
		NBTTagCompound teNBT = tileEntity.serializeNBT();
		teNBT.removeTag("x");
		teNBT.removeTag("y");
		teNBT.removeTag("z");
		c.setTag("BlockEntityTag", teNBT);
		NBTTagList l = new NBTTagList();
		l.appendTag(new NBTTagString(StringUtils.translate("item.hasNBT.desc")));
		NBTTagCompound d = new NBTTagCompound();
		d.setTag("Lore", l);
		c.setTag("display", d);
		
		ItemStack stack = new ItemStack(this.getItemDropped(state, new Random(), 0));
		stack.setTagCompound(c);
		
		return stack;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TE te = this.getTileEntity(world, pos);
		
		GameRules rules = world.getGameRules();
		
		if(te != null && rules.getBoolean("doTileDrops")) {
			if(!rules.getBoolean("dropInvContents"))
				world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), this.toItem(te, state)));
			else
				this.handleItemSpawningLogic(te, world, pos, state);
		}
		super.breakBlock(world, pos, state);
	}
	
	public void handleItemSpawningLogic(TE te,World world, BlockPos pos, IBlockState state) {
		IItemHandler itemHandler = te.getInventory();
		int s = itemHandler.getSlots();
		for(int i=0; i<s; i++) {
			if(!itemHandler.getStackInSlot(i).isEmpty()) {
				world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), itemHandler.getStackInSlot(i)));
			}
		}
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (stack.hasDisplayName() && tileentity instanceof IWorldNameableModifiable) {
			((IWorldNameableModifiable) tileentity).setCustomName(stack.getDisplayName());
		}
		if (placer instanceof EntityPlayer) {
			IOwnerHandler ownerHandler = tileentity.getCapability(CapabilityOwner.OWNER_HANDLER_CAPABILITY, null);
			if (ownerHandler != null)
				ownerHandler.setOwner((EntityPlayer) placer);
		}
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TE te = this.getTileEntity(world, pos);
		if(te == null) return 0;
        int i = 0;
        float f = 0.0F;
        IItemHandler h = te.getInventory();
        for (int j = 0; j < h.getSlots(); j++) {
        	ItemStack itemstack = h.getStackInSlot(j);
            if (!itemstack.isEmpty()) {
            	f += (float)itemstack.getCount() / (float)Math.min(h.getSlotLimit(j), itemstack.getMaxStackSize());
                i++;
            }
        }
        f /= h.getSlots();
        return MathHelper.floor(f * 14.0F) + (i > 0 ? 1 : 0);
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		TE te = this.getTileEntity(worldIn, pos);
		if(te != null && !worldIn.isRemote) {
			te.sync();
		}
		super.onBlockAdded(worldIn, pos, state);
	}
	
}
