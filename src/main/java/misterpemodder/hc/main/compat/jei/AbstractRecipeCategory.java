package misterpemodder.hc.main.compat.jei;

import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import misterpemodder.hc.main.utils.ModUtils;
import misterpemodder.hc.main.utils.StringUtils;

public abstract class AbstractRecipeCategory<T extends IRecipeWrapper> extends BlankRecipeCategory<T> {

	protected final String uid;
	protected final String localizedName;
	
	public AbstractRecipeCategory(String uid, String unlocalizedName) {
		this.uid = uid;
		this.localizedName = StringUtils.translate(unlocalizedName);
	}
	
	@Override
	public String getUid() {
		return this.uid;
	}
	
	@Override
	public String getTitle() {
		return this.localizedName;
	}
	
	@Override
	public String getModName() {
		return ModUtils.activeModContainer().getName();
	}
	
}
