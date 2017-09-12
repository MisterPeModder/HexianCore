package misterpemodder.hc.main.items;

public interface IHexianItem {
	
	public String getUnlocalizedName();
	
	public default String[] getVariantsNames() {
		String[] name = {this.getUnlocalizedName()};
		return name;
	}
	public default boolean isEnabled() {
		return true;
	}
	public void registerRender();
}
