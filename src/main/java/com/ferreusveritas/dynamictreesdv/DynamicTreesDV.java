package com.ferreusveritas.dynamictreesdv;

import com.ferreusveritas.dynamictreesdv.proxy.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = ModConstants.MODID, name = ModConstants.NAME, version=ModConstants.VERSION, dependencies=ModConstants.DEPENDENCIES)
public class DynamicTreesDV {
	
	@Mod.Instance(ModConstants.MODID)
	public static DynamicTreesDV instance;
	
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
