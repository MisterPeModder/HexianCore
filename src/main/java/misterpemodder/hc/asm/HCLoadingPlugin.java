package misterpemodder.hc.asm;

import java.util.Map;

import misterpemodder.hc.HCRefs;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.Name(HCRefs.MOD_NAME)
@IFMLLoadingPlugin.MCVersion("1.11.2")
@IFMLLoadingPlugin.SortingIndex(1001)
public class HCLoadingPlugin implements IFMLLoadingPlugin{

	public static boolean runtimeDeobfuscation = false;
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[]{};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
    public void injectData(Map<String, Object> data) {
		runtimeDeobfuscation = !(Boolean)data.get("runtimeDeobfuscationEnabled");
    }

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}