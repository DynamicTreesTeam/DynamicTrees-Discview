package com.ferreusveritas.dynamictreesdv;

import java.util.Random;

import com.ferreusveritas.dynamictrees.api.worldgen.IRadiusCoordinator;

public class RadiusCoordinator implements IRadiusCoordinator {

	private final Random random;
	
	public RadiusCoordinator(Random random) {
		this.random = random;
	}
	
	@Override
	public int getRadiusAtCoords(double x, double z) {
		return 5;
		//return random.nextInt(4) + 4;
	}
	
}
