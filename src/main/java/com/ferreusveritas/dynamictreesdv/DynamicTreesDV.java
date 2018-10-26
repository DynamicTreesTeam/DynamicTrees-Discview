package com.ferreusveritas.dynamictreesdv;

import com.ferreusveritas.dynamictreesdv.packets.PacketOpenGui;
import com.ferreusveritas.dynamictreesdv.proxy.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = ModConstants.MODID, name = ModConstants.NAME, version=ModConstants.VERSION, dependencies=ModConstants.DEPENDENCIES)
public class DynamicTreesDV {
	
	@Mod.Instance(ModConstants.MODID)
	public static DynamicTreesDV instance;
	
	public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(ModConstants.MODID);
	
	@SidedProxy(clientSide = "com.ferreusveritas.dynamictreesdv.proxy.ClientProxy", serverSide = "com.ferreusveritas.dynamictreesdv.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		int disc = 0;
		network.registerMessage(PacketOpenGui.class, PacketOpenGui.class, disc++, Side.CLIENT);
		proxy.postInit();
	}
	
	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		proxy.registerCommands(event);
	}
	
	@Mod.EventBusSubscriber
	public static class RegistrationHandler {
		
	}
	
}
