package com.ferreusveritas.dynamictreesdv.view;

import com.ferreusveritas.dynamictrees.systems.poissondisc.PoissonDisc;

public class Grid {
	
	private final int width, height;
	private final boolean[][] current;
	public boolean changed = true;

	public Grid() {
		this(128, 128);
	}
	
	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		this.current = new boolean[height][width];
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public void clean() {
		changed = false;
	}
	
	public void clear() {
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				setBlock(x, y, false);
			}
		}
	}
	
	public boolean getBlock(int x, int z) {
		if(x >= 0 && z >= 0 && x < getWidth() && z < getHeight()) {
			return current[z][x];
		}
		return false;
	}
	
	public void setBlock(int x, int z, boolean block) {
		if(x >= 0 && z >= 0 && x < getWidth() && z < getHeight()) {
			current[z][x] = block;
		}
		changed = true;
	}
	
	public void setBlockOr(int x, int z, boolean block) {
		if(x >= 0 && z >= 0 && x < getWidth() && z < getHeight()) {
			current[z][x] |= block;
		}
		changed = true;
	}
	
	public void drawDisc(PoissonDisc disc) {
		int startX = disc.x - disc.radius;
		int stopX = disc.x + disc.radius;
		int startZ = disc.z - disc.radius;
		int stopZ = disc.z + disc.radius;
			
		for(int z = startZ; z <= stopZ; z++) {
			for(int x = startX; x <= stopX; x++) {
				setBlockOr(x, z, disc.isEdge(x, z));
			}
		}
	}
}
