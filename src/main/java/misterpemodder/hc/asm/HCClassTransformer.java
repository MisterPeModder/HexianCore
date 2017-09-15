package misterpemodder.hc.asm;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableSet;

import net.minecraft.launchwrapper.IClassTransformer;

/**
 * An implementation of {@link IClassTransformer}
 * that facilitate the creation of core mods.
 */
public abstract class HCClassTransformer implements IClassTransformer {

	private final ImmutableSet<ClassPatcher> classPatchers;
	protected final Logger logger;
	
	/**
	 * Return a set containing the class patchers of this loding plugin
	 * 
	 * @return A set containing all the class patcher of this class transformer.
	 */
	protected abstract Set<ClassPatcher> getClassPatchers();
	
	protected abstract String getName();
	
	public HCClassTransformer() {
		this.classPatchers = ImmutableSet.copyOf(getClassPatchers());

		this.logger = LogManager.getLogger("Hexian Core (" + this.getName() + ")");
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