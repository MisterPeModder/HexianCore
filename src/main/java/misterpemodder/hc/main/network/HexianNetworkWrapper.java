package misterpemodder.hc.main.network;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class HexianNetworkWrapper extends SimpleNetworkWrapper {

	public final int serverToClientChannelId;
	public final int clientToServerChannelId;
	
	public HexianNetworkWrapper(String channelName) {
		this(channelName, 0, 1);
	}
	
	public HexianNetworkWrapper(String channelName, int serverToClientChannelId, int clientToServerChannelId) {
		super(channelName);
		this.clientToServerChannelId = clientToServerChannelId;
		this.serverToClientChannelId = serverToClientChannelId;
	}

}
