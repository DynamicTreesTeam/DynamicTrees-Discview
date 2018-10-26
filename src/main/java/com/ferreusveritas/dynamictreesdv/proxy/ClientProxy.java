package com.ferreusveritas.dynamictreesdv.proxy;

import com.ferreusveritas.dynamictreesdv.view.DiscView;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		// TODO Auto-generated method stub
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}

	@Override
	public void postInit() {
		new DiscView();
	}

	
}
