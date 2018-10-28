package com.ferreusveritas.dynamictreesdv.view;

import com.ferreusveritas.dynamictrees.systems.poissondisc.PoissonDisc;

public class GridDisc extends GridDrawable {
	
	PoissonDisc disc;
	
	public GridDisc(PoissonDisc disc) {
		this.disc = disc;
	}
	
	@Override
	public void draw(Grid grid) {
		int startX = disc.x - disc.radius;
		int stopX = disc.x + disc.radius;
		int startZ = disc.z - disc.radius;
		int stopZ = disc.z + disc.radius;
			
		for(int z = startZ; z <= stopZ; z++) {
			for(int x = startX; x <= stopX; x++) {
				grid.setBlockOr(x, z, disc.isEdge(x, z));
			}
		}
	}
	
}
