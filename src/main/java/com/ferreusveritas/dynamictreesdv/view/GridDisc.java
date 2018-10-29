package com.ferreusveritas.dynamictreesdv.view;

import com.ferreusveritas.dynamictrees.systems.poissondisc.PoissonDisc;

public class GridDisc extends GridDrawable {
	
	PoissonDisc disc;
	int color;
	
	public GridDisc(PoissonDisc disc, int color) {
		this.disc = disc;
		this.color = color;
	}
	
	@Override
	public void draw(Grid grid) {
		int startX = disc.x - disc.radius;
		int stopX = disc.x + disc.radius;
		int startZ = disc.z - disc.radius;
		int stopZ = disc.z + disc.radius;
			
		for(int z = startZ; z <= stopZ; z++) {
			for(int x = startX; x <= stopX; x++) {
				if(disc.isEdge(x, z) || ( disc.real && disc.isInside(x, z) ) ) {
					grid.setBlockOr(x, z, color);
				}
			}
		}
	}
	
}
