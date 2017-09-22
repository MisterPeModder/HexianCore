package misterpemodder.hc.main.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import misterpemodder.hc.api.capability.owner.IOwnerHandler;
import misterpemodder.hc.api.item.IItemLock;
import misterpemodder.hc.main.apiimpl.capability.owner.CapabilityOwner;
import misterpemodder.hc.main.blocks.properties.IBlockNames;
import misterpemodder.hc.main.blocks.properties.IBlockValues;
import misterpemodder.hc.main.tileentity.TileEntityCustomChest;
import misterpemodder.hc.main.utils.GuiHelper;
import misterpemodder.hc.main.utils.GuiHelper.IGuiElement;
import misterpemodder.hc.main.utils.StringUtils;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockCustomChest<TE extends TileEntityCustomChest> extends BlockContainerBase<TE> {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	protected static final AxisAlignedBB CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
	
	
	public BlockCustomChest(IBlockNames n, IBlockValues v, CreativeTabs tab) {
		super(n, v, tab);
	}
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.NORMAL;
	}
	
	@Override
	protected List<IProperty<?>> getProperties() {
		ArrayList<IProperty<?>> list = new ArrayList<>();
		list.addAll(super.getProperties());
		list.add(FACING);
		return list;
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex() - 2;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta+2));
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }
	
	@Override
	public int damageDropped(IBlockState state) {
	    return 0;
	}
	
	@Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }
    
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    	return CHEST_AABB;
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
    	return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean hasCustomBreakingProgress(IBlockState state) {
        return true;
    }
    
    protected abstract IGuiElement getGuiElements();
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(world.isRemote) {
			return true;
		} else if(super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ)){
			return true;
		}
		else if(this.getTileEntity(world, pos) != null) {
			TileEntityCustomChest te = this.getTileEntity(world, pos);
			te.sync();
			IOwnerHandler ownerHandler = te.getCapability(CapabilityOwner.OWNER_HANDLER_CAPABILITY, null);
			
			if(ownerHandler != null && ownerHandler.hasOwner()) {
				if(!ownerHandler.isOwner(player) && te.isLocked()) {
					player.sendStatusMessage(new TextComponentString(TextFormatting.RED+StringUtils.translate("tile.blockTitaniumChest.locked")), true);
					return true;
				}
			}
				
			te.onInvOpen(player);
			GuiHelper.openGui(player, getGuiElements(), world, pos);
		}
		return true;
	}
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te,
			ItemStack stack) {
		if (te instanceof IWorldNameable && ((IWorldNameable)te).hasCustomName()) {
            player.addExhaustion(0.005F);
            
            if (worldIn.isRemote) {
                return;
            }
            
            Item item = this.getItemDropped(state, worldIn.rand, 0);
            if (item == Items.AIR) {
                return;
            }

            ItemStack itemstack = new ItemStack(item, 1);
            itemstack.setStackDisplayName(((IWorldNameable)te).getName());
            spawnAsEntity(worldIn, pos, itemstack);
        }
        else {
            super.harvestBlock(worldIn, player, pos, state, (TileEntity)null, stack);
        }
	}
	
	
	@Override
	public boolean canDropFromExplosion(Explosion explosionIn) {
		return false;
	}
	
	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
		TileEntityCustomChest chest = (TileEntityCustomChest)world.getTileEntity(pos);
		if(chest != null && !world.isRemote) {
			ItemStack stack = chest.getLockItemHandler().getStackInSlot(0);
			if(!stack.isEmpty() && stack.getItem() instanceof IItemLock) {
				IItemLock lock = (IItemLock)stack.getItem();
				if(!lock.isBroken(stack)) {
					
					if(lock.attemptBreak(stack, new Random()) == EnumActionResult.SUCCESS) {
						world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F, true);
						chest.setLocked(false);
						lock.onLockBroken(world, pos, explosion.getExplosivePlacedBy());
					}
				}
			}
		}
	}

}
