package misterpemodder.hc.asm;

import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableSet;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

/**
 * A dual-implementation of {@link IClassTransformer} and {@link IFMLLoadingPlugin}
 * that facilitate the creation of core mods.
 */
public abstract class HCLoadingPlugin implements IClassTransformer, IFMLLoadingPlugin {

	public static boolean runtimeDeobfuscation = false;
	private final ImmutableSet<ClassPatcher> classPatchers;
	protected final Logger logger;
	
	/**
	 * Return a set containing the class patchers of this loding plugin
	 */
	protected abstract Set<ClassPatcher> getClassPatchers();
	
	public HCLoadingPlugin() {
		this.classPatchers = ImmutableSet.copyOf(getClassPatchers());
		
		String name;
		try {
			name = this.getClass().getAnnotation(IFMLLoadingPlugin.Name.class).value();
		} catch (NullPointerException e){
			name = this.getClass().getSimpleName();
		}
		this.logger = LogManager.getLogger("Hexian Core (" + name + ")");
	}
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[]{this.getClass().getName()};
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

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		for (ClassPatcher cp : this.classPatchers) {
			if(cp.matches(transformedName)) {
				return cp.makePatches(transformedName, basicClass, logger);
			}
		}
		return basicClass;
	}

}
