/*
=======================================================================
======*--------------*=======/-----\=====/-----\===/--------------\====
=====/                \======|     |=====|     |===|               \===
====/                  \=====|     |=====|     |===|     /---------/===
===/                    \====|     |=====|     |===|     |=============
==/                      \===|     \-----/     |===|     |=============
=/                        \==|                 |===|     |=============
=\                        /==|                 |===|     |=============
==\                      /===|     /-----\     |===|     |=============
===\                    /====|     |=====|     |===|     |=============
====\                  /=====|     |=====|     |===|     \---------\===
=====\                /======|     |=====|     |===|               /===
======*--------------*=======\-----/=====\-----/===\--------------/====
=======================================================================
*/

package misterpemodder.hc.main;

import static misterpemodder.hc.main.network.packet.PacketHandler.CHEST_UPDATE_HANDLER;
import static misterpemodder.hc.main.network.packet.PacketHandler.SYNCED_CONTAINER_ELEMENTS;
import static misterpemodder.hc.main.network.packet.PacketHandler.TE_UPDATE_HANDLER;
import static misterpemodder.hc.main.network.packet.PacketHandler.TE_UPDATE_REQUEST_HANDLER;

import misterpemodder.hc.main.apiimpl.capability.owner.CapabilityOwner;
import misterpemodder.hc.main.client.gui.tabs.ButtonClickHandler;
import misterpemodder.hc.main.compat.craftingtweaks.CraftingTweaksCompat;
import misterpemodder.hc.main.network.HexianNetworkWrapper;
import misterpemodder.hc.main.network.packet.PacketHandler;
import misterpemodder.hc.main.network.proxy.CommonProxyHC;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
		modid = HCRefs.MOD_ID,
		name = HCRefs.MOD_NAME,
		version = HCRefs.MOD_VERSION,
		acceptedMinecraftVersions = HCRefs.ACCEPTED_MC_VERSIONS,
		acceptableRemoteVersions = "*",
		dependencies = "required-after:forge@[13.20.1.2386,);",
		updateJSON = HCRefs.UPDATE_JSON
	)
public class HexianCore extends AbstractMod {
	
	@Instance(HCRefs.MOD_ID)
	public static HexianCore instance;
	
	@SidedProxy(modId = HCRefs.MOD_ID, clientSide = HCRefs.CLIENT_PROXY_CLASS, serverSide = HCRefs.SERVER_PROXY_CLASS)
	public static CommonProxyHC proxy;

	@Override
	public CommonProxyHC getProxy() {
		return proxy;
	}

	@Override
	protected HexianNetworkWrapper createNetworkWrapper() {
		return new HexianNetworkWrapper(HCRefs.MOD_ID);
	}
	
	@Mod.EventHandler
	public void preInitialization(FMLPreInitializationEvent event) {
		HCRefs.LOGGER.info("Pre-Init!");
		
		super.preInitialization(event);
		
		PacketHandler.registerPacketHandlers(
				TE_UPDATE_HANDLER, TE_UPDATE_REQUEST_HANDLER, CHEST_UPDATE_HANDLER,
				ButtonClickHandler.BUTTON_CLICK_HANDLER, SYNCED_CONTAINER_ELEMENTS
				);
		
		CapabilityOwner.register();
	}
	
	@Mod.EventHandler
	public void initialization(FMLInitializationEvent event) {
		HCRefs.LOGGER.info("Init!");
		
		super.initialization(event);
		
		HCRefs.baublesLoaded = Loader.isModLoaded("baubles");
		CraftingTweaksCompat.init();
	}
	
	@Mod.EventHandler
	public void postInitialization(FMLPostInitializationEvent event) {
		HCRefs.LOGGER.info("Post-Init!");
		
		super.postInitialization(event);
	}

}
