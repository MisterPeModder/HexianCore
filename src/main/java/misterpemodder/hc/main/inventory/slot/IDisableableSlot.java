package misterpemodder.hc.main.inventory.slot;

/**
 * Represents an inventory slot that can be fully enabled/disabled.
 */
public interface IDisableableSlot extends IHidableSlot {
	
	void setEnabled(boolean enable);

}
