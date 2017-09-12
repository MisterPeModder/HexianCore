package misterpemodder.hc.main.network.proxy;

/**
 * internal common proxy of HexianCore, do not extends this class.
 */
public abstract class CommonProxyHC implements ICommonProxy {
	
	/**
	 * Use StringUtils.translate instead.
	 */
	@Deprecated
	public abstract String translate(String translateKey, Object... params);

}
