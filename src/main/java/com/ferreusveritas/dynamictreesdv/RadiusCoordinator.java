package com.ferreusveritas.dynamictreesdv;

import java.util.Random;

import com.ferreusveritas.dynamictrees.api.worldgen.IRadiusCoordinator;

public class RadiusCoordinator implements IRadiusCoordinator {
	
	private final Random random;
	
	public int packing = 0;
	
	public RadiusCoordinator(Random random) {
		this.random = random;
	}
	
	@Override
	public int getRadiusAtCoords(int x, int z) {
		switch(packing) {
			default:
			case 0: return random.nextInt(7) + 2;
			case 1: return 3;
			case 2: return 2;
		}
	}
	
	@Override
	public boolean setPacking(int chunkX, int chunkZ, int iteration) {
		this.packing = iteration;
		return packing < 3;
	}
	
}
