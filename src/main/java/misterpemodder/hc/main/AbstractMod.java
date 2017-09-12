package misterpemodder.hc.main;



import org.apache.logging.log4j.Logger;

import misterpemodder.hc.main.network.HexianNetworkWrapper;
import misterpemodder.hc.main.network.packet.PacketClientToServer;
import misterpemodder.hc.main.network.packet.PacketServerToClient;
import misterpemodder.hc.main.network.proxy.ICommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

public abstract class AbstractMod {
	
	private HexianNetworkWrapper network;
	
	public abstract ICommonProxy getProxy();
	
	protected abstract HexianNetworkWrapper createNetworkWrapper();
	
	public Logger getLogger() {
		return HCRefs.LOGGER;
	}
	
	public HexianNetworkWrapper getNetworkWrapper() {
		return this.network;
	}

	public void preInitialization(FMLPreInitializationEvent event) {
		
		this.network = createNetworkWrapper();
		if (this.network != null) {
			this.network.registerMessage(PacketServerToClient.PacketServerToClientHandler.class, PacketServerToClient.class, this.network.serverToClientChannelId, Side.CLIENT);
			this.network.registerMessage(PacketClientToServer.PacketClientToServerHandler.class, PacketClientToServer.class, this.network.clientToServerChannelId, Side.SERVER);
		}
		
		if(getProxy() != null)
			getProxy().preInit();
	}

	@Mod.EventHandler
	public void initialization(FMLInitializationEvent event) {
		if(getProxy() != null)
			getProxy().init();
	}

	@Mod.EventHandler
	public void postInitialization(FMLPostInitializationEvent event) {
		if(getProxy() != null)
			getProxy().postInit();
	}
	
}
