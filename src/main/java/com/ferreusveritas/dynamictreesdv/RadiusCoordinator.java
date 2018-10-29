package com.ferreusveritas.dynamictreesdv;

import java.util.Random;

import com.ferreusveritas.dynamictrees.api.worldgen.IRadiusCoordinator;

public class RadiusCoordinator implements IRadiusCoordinator {
	
	private final Random random;
	
	public int pass = 0;
	
	public RadiusCoordinator(Random random) {
		this.random = random;
	}
	
	@Override
	public int getRadiusAtCoords(int x, int z) {
		switch(pass) {
			default:
			case 0: return random.nextInt(7) + 2;
			case 1: return 3;
			case 2: return 2;
		}
	}
	
	@Override
	public boolean runPass(int chunkX, int chunkZ, int pass) {
		this.pass = pass;
		return pass < 3;
	}
	
}
