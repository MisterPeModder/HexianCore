package misterpemodder.hc.main.network.proxy;

import net.minecraft.util.text.translation.I18n;

/**
 * internal server proxy of HexianCore, do not extends this class.
 */
public class ServerProxyHC extends CommonProxyHC {

	@Override
	public void preInit() {}

	@Override
	public void init() {}

	@Override
	public void postInit() {}

	@Override
	public String translate(String translateKey, Object... params) {
		return I18n.translateToLocalFormatted(translateKey, params);
	}

}
