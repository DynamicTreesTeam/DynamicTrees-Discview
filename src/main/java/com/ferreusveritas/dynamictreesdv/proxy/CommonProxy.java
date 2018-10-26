package com.ferreusveritas.dynamictreesdv.proxy;

import com.ferreusveritas.dynamictreesdv.command.CommandDebugDiscs;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

	public void preInit() {
		// TODO Auto-generated method stub
	}

	public void init() {
		// TODO Auto-generated method stub
	}
	
	public void registerCommands(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandDebugDiscs());
	}
	
	public void postInit() {
		// TODO Auto-generated method stub
	}
	
}
