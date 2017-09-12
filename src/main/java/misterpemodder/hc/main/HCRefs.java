package misterpemodder.hc.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class HCRefs {
	
	public static final String MOD_ID = "hc";
	public static final String MOD_NAME = "Hexian Core";
	
	public static final String MOD_VERSION = "1.1.0";
	public static final String ACCEPTED_MC_VERSIONS = "[1.11.2]";
	
	public static final String CLIENT_PROXY_CLASS = "misterpemodder.hc.main.network.proxy.ClientProxyHC";
	public static final String SERVER_PROXY_CLASS = "misterpemodder.hc.main.network.proxy.ServerProxyHC";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
	
	public static final String DEFAULT_ITEM_NAME = HCRefs.MOD_ID+".missingNo.name";
	
	public static boolean baublesLoaded = false;
	
}
