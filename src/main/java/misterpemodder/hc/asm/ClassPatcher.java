package misterpemodder.hc.asm;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.google.common.collect.ImmutableListMultimap;

/**
 *  A class that provides asm class transformations.
 */
public abstract class ClassPatcher {
	
	private final ImmutableListMultimap<BiPredicate<Boolean, MethodNode>, Pair<BiPredicate<Boolean, AbstractInsnNode>, IPatch>> methodPatchers;
	
	public ClassPatcher() {
		
		ImmutableListMultimap.Builder<BiPredicate<Boolean, MethodNode>, Pair<BiPredicate<Boolean, AbstractInsnNode>, IPatch>> builder = ImmutableListMultimap.builder();
		
		List<IMethodPatcher> methodPatches = getMethodPatchers();
		if(methodPatches != null && !methodPatches.isEmpty()) {
			for(IMethodPatcher methodPatch : methodPatches) {
				List<IPatch> patches = methodPatch.getPatches();
				if(patches != null && !patches.isEmpty()) {
					List<Pair<BiPredicate<Boolean, AbstractInsnNode>, IPatch>> list = new ArrayList<>();
					for(IPatch p : patches) {
						list.add(Pair.of(p.getNodePredicate(), p));
					}
					builder.putAll(methodPatch.getMethodNodePredicate(), list);
				}
			}
		}
		
		this.methodPatchers = builder.build();
		
	}
	
	public abstract boolean matches(String transformedClassName);
	
	protected abstract List<IMethodPatcher> getMethodPatchers();
	
	public final byte[] makePatches(String className, byte[] basicClass, Logger logger) {
		
		if(methodPatchers != null && !methodPatchers.isEmpty()) {
			
			logger.info("Patching " + className + "...");
			
			ClassNode classNode = new ClassNode();
	        ClassReader classReader = new ClassReader(basicClass);
	        classReader.accept(classNode, ClassReader.SKIP_FRAMES);
			
	        for(MethodNode mn : classNode.methods) {
	        	for(BiPredicate<Boolean, MethodNode> mPredicate : methodPatchers.keySet()) {
	        		if(mn != null && mPredicate.test(HCLoadingPlugin.runtimeDeobfuscation, mn)) {
	        			
	        			List<Pair<BiPredicate<Boolean, AbstractInsnNode>, IPatch>> list = methodPatchers.get(mPredicate);
	        			if(list != null && !list.isEmpty()) {
	        				int s = 0;
	        				for(Pair<BiPredicate<Boolean, AbstractInsnNode>, IPatch> pair : list) {
	                			for(int i=s; i<mn.instructions.size(); i++) {
	                				AbstractInsnNode node = mn.instructions.get(i);
	                				if(pair.getLeft().test(HCLoadingPlugin.runtimeDeobfuscation, node)) {
	                					pair.getRight().makePatch(mn, node, i);
	                					
	                					if(!pair.getRight().alwaysPatch()) {
	                						s = i;
		                					break;
	                					}
	                				}
	                			}
	        				}
	        			}
	        		}
	        	}
			}
	        
			try {
		        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		        classNode.accept(writer);
		        logger.info(className+" patching complete!");
		        return writer.toByteArray();
			} catch(Throwable t) {
				t.printStackTrace();
			}
	        
		}
		
		return basicClass;
	}
	
	protected static interface IMethodPatcher {
		
		public BiPredicate<Boolean, MethodNode> getMethodNodePredicate();
		
		public List<IPatch> getPatches();
		
	}
	
	protected interface IPatch {
		
		public BiPredicate<Boolean, AbstractInsnNode> getNodePredicate();
		
		public void makePatch(MethodNode method, AbstractInsnNode targetNode, int nodeIndex);
		
		public default boolean alwaysPatch() {
			return false;
		}
		
	}
	
}
