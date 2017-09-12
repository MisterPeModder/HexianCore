package misterpemodder.hc.main.network.proxy;

import net.minecraft.client.resources.I18n;

/**
 * internal client proxy of HexianCore, do not extends this class.
 */
public class ClientProxyHC extends CommonProxyHC {

	@Override
	public void preInit() {}

	@Override
	public void init() {}

	@Override
	public void postInit() {}

	@Override
	public String translate(String translateKey, Object... params) {
		return I18n.format(translateKey, params);
	}

}
