package misterpemodder.hc.main.network.proxy;

/**
 * A common proxy interface for client and server
 */
public interface ICommonProxy {
	
	public abstract void preInit();
	public abstract void init();
	public abstract void postInit();
	
}
