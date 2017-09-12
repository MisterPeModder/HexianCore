package misterpemodder.hc.main.utils;

import misterpemodder.hc.main.HCRefs;
import net.minecraft.util.ResourceLocation;

public class ResourceLocationHC extends ResourceLocation {

	public ResourceLocationHC(String resourceName) {
		super(HCRefs.MOD_ID, resourceName);
	}

}
