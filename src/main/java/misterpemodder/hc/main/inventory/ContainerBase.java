package misterpemodder.hc.main.inventory;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import misterpemodder.hc.api.block.ILockable;
import misterpemodder.hc.api.item.IItemLock;
import misterpemodder.hc.main.HCRefs;
import misterpemodder.hc.main.client.gui.tabs.TabBase;
import misterpemodder.hc.main.inventory.elements.ISyncedContainerElement;
import misterpemodder.hc.main.inventory.slot.IHidableSlot;
import misterpemodder.hc.main.inventory.slot.SlotFiltered;
import misterpemodder.hc.main.inventory.slot.SlotHidable;
import misterpemodder.hc.main.inventory.slot.SlotHidableInventory;
import misterpemodder.hc.main.inventory.slot.SlotHidableInventory.SlotHidableCrafting;
import misterpemodder.hc.main.network.packet.PacketHandler;
import misterpemodder.hc.main.tileentity.TileEntityContainerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerArmorInvWrapper;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import net.minecraftforge.items.wrapper.PlayerOffhandInvWrapper;

public abstract class ContainerBase<TE extends TileEntityContainerBase> extends Container {
	
	protected static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] {EntityEquipmentSlot.FEET, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.HEAD};
	public final int BPART_OFFSET;
	
	protected TE te;
	protected PlayerMainInvWrapper playerMainInv;
	protected PlayerOffhandInvWrapper playerOffhandInv;
	protected PlayerArmorInvWrapper playerArmorInv;
	
	public InventoryCrafting craftMatrix;
    public IInventory craftResult;
    public IBaublesItemHandler baublesInv;
    
	protected MutablePair<TabBase<?, ?>, TabBase<?, ?>> selectedTabs = new MutablePair<>();
	public EntityPlayer player;
	
	public final ImmutableList<ISyncedContainerElement> containerElements;
	
	private boolean elementsInitiated = false;
	
	public ContainerBase(TE te, InventoryPlayer playerInv, int bPartOffset) {
		this(te, playerInv, bPartOffset, true);
	}
	
	public ContainerBase(TE te, InventoryPlayer playerInv, int bPartOffset, boolean hasArmorTab) {
		this.te = te;
		
		BPART_OFFSET = bPartOffset;
		this.player = playerInv.player;
		this.playerMainInv = new PlayerMainInvWrapper(playerInv);
		
		if(hasArmorTab) {
			this.playerOffhandInv = new PlayerOffhandInvWrapper(playerInv);
			this.playerArmorInv = new PlayerArmorInvWrapper(playerInv);
		}
		
		setPlayerInvSlots(hasArmorTab);
		setLockSlot();
		setTeSlots(te);
		setCraftingSlots(hasArmorTab);
		setExtraInvSlots();
		
		hideSlots();
		
		ImmutableList.Builder<ISyncedContainerElement> builder = new ImmutableList.Builder<>();
		builder.addAll(addContainerElements(new ArrayList<>()));
		this.containerElements = builder.build();
	}
	
	public ImmutablePair<TabBase<?, ?>, TabBase<?, ?>> getSelectedTabs() {
		return ImmutablePair.of(selectedTabs.left, selectedTabs.right);
	}
	
	public void setSelectedTabs(Pair<TabBase<? extends ContainerBase<TE>, TE>, TabBase<? extends ContainerBase<TE>, TE>> tabs) {
		 this.selectedTabs = MutablePair.of(tabs.getLeft(), tabs.getRight());
	}

	public void hideSlots() {
		for(Slot sl : this.inventorySlots) {
			if(sl instanceof IHidableSlot) {
				IHidableSlot slot = (IHidableSlot) sl;
				if(selectedTabs.getLeft() != null && selectedTabs.getRight() != null) {
					slot.setEnabled(selectedTabs.getLeft().shouldDisplaySlot(slot) || selectedTabs.getRight().shouldDisplaySlot(slot));
				} else if(getDefaultSlotIndexes().contains(sl.slotNumber)){
					slot.setEnabled(true);
				} else if(slot instanceof SlotHidable) {
					IItemHandler h = ((SlotHidable)slot).getItemHandler();
					slot.setEnabled(h == te.getInventory() || h == playerMainInv);
				} else {
					slot.setEnabled(false);
				}
			}
		}
	}
	
	public TE getTileEntity() {
		return this.te;
	}
	
	public PlayerMainInvWrapper getPlayerInv() {
		return this.playerMainInv;
	}
	
	public PlayerOffhandInvWrapper getPlayerOffandInv() {
		return this.playerOffhandInv;
	}
	
	public PlayerArmorInvWrapper getPlayerArmorInv() {
		return this.playerArmorInv;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
	protected abstract void setTeSlots(TE te);
	
	protected abstract List<Integer> getDefaultSlotIndexes();
	
	/**
	 * Use this method to add synced container elements to the list.
	 * 
	 * @param elements - A list containing all the elements to be added.
	 * @return The elements list. 
	 */
	protected List<ISyncedContainerElement> addContainerElements(List<ISyncedContainerElement> elements) {
		return elements;
	}
	
	protected boolean shouldEnableBaublesCompat() {
		return true;
	}
	
	public final boolean isBaublesCompatEnabled() {
		return HCRefs.baublesLoaded && shouldEnableBaublesCompat();
	}
	
	protected void setPlayerInvSlots(boolean hasArmorTab) {
		//Main Inventory
	    for (int y = 0; y < 3; ++y) {
	        for (int x = 0; x < 9; ++x) {
	            this.addSlotToContainer(new SlotHidable(playerMainInv, x + y * 9 + 9, 26 + x * 18, BPART_OFFSET+18 + y * 18, true));
	        }
	    }
	 
	    //Hotbar
	    for (int x = 0; x < 9; ++x) {
	        this.addSlotToContainer(new SlotHidable(playerMainInv, x, 26 + x * 18, BPART_OFFSET+76, true));
	    }
	    
	    if(hasArmorTab) {
			//Armor
	    	for (int y = 0; y < 4; ++y) {
	    		final EntityEquipmentSlot entityequipmentslot = VALID_EQUIPMENT_SLOTS[y];
	    		Predicate<ItemStack> armorTest = new Predicate<ItemStack>() {
					public boolean apply(ItemStack stack) {
						return stack.getItem().isValidArmor(stack, entityequipmentslot, player);
					}
				};
				SlotFiltered slot = new SlotFiltered(playerArmorInv, y, 61, BPART_OFFSET+69 - y * 18, true, armorTest);
	    		slot.setBackgroundName(ItemArmor.EMPTY_SLOT_NAMES[y]);
				this.addSlotToContainer(slot);
	    	}
	    
	    	//Offhand
	    	int o = isBaublesCompatEnabled()? 19 : 0;
	    	Slot slot = new SlotHidable(playerOffhandInv, 0, 83+o, BPART_OFFSET+69, true);
	    	slot.setBackgroundName("minecraft:items/empty_armor_slot_shield");
	    	this.addSlotToContainer(slot);
		}
	}
	
	protected void setCraftingSlots(boolean hasArmorTab) {
		if(hasArmorTab) {
			craftMatrix = new InventoryCrafting(this, 3, 3);
			craftResult = new InventoryCraftResult();
			
			this.addSlotToContainer(new SlotHidableCrafting(playerMainInv.getInventoryPlayer().player, this.craftMatrix, this.craftResult, 0, 181, BPART_OFFSET+75));

	        for (int y = 0; y < 3; ++y) {
	            for (int x = 0; x < 3; ++x) {
	                this.addSlotToContainer(new SlotHidableInventory(this.craftMatrix, x + y * 3, 149 + x * 18, BPART_OFFSET+15 + y * 18));
	            }
	        }
	        
	        if(isBaublesCompatEnabled()) {
				baublesInv = BaublesApi.getBaublesHandler(player);
				for (int x = 0; x < 2; ++x) {
					for (int y = 0; y < 4; ++y) {
		            	if(y == 3 && x == 1) break;
		                this.addSlotToContainer(new SlotHidable(this.baublesInv, y + x * 4, 83 + x * 19, BPART_OFFSET+15 + y * 18, true));
		            }
		        }
			}
	        
	        this.onCraftMatrixChanged(this.craftMatrix);
		}
	}
	
	protected void setLockSlot() {
		if(this.te instanceof ILockable) {
			Predicate<ItemStack> lockTest = new Predicate<ItemStack>() {
				public boolean apply(ItemStack stack) {
					return stack.getItem() instanceof IItemLock;
				}
			};
			final ILockable lockable = (ILockable) this.te;
			SlotFiltered ls = new SlotFiltered(lockable.getLockItemHandler(), 0, 8, 18, true, lockTest) {
				@Override
				public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
					lockable.setLocked(false);
					return super.onTake(thePlayer, stack);
				}
			};
			this.addSlotToContainer(ls);
		}
	}
	
	//override this to add extra slots
	protected void setExtraInvSlots() {
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		te.onInvClose(playerIn);
        super.onContainerClosed(playerIn);
	}
	
	public void onCraftMatrixChanged(IInventory inventoryIn) {
		if(craftResult != null)
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, player.world));
    }
	
	public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return craftResult != null && slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
    }
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		if(player != null && player instanceof EntityPlayerMP && te != null) {
			for(int i=0,s=containerElements.size(); i<s; i++) {
				ISyncedContainerElement ce = containerElements.get(i);
				if(ce.shouldSendDataToClient() || !elementsInitiated) {
					NBTTagCompound toSend = new NBTTagCompound();
					toSend.setTag("element_data", ce.writeData(new NBTTagCompound()));
					toSend.setInteger("element_id", i);
					PacketHandler.sendTo(PacketHandler.SYNCED_CONTAINER_ELEMENTS, toSend, (EntityPlayerMP)player);
				}
				
			}
			elementsInitiated = true;
		}
		
	}
	
}
