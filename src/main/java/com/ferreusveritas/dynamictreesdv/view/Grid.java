package com.ferreusveritas.dynamictreesdv.view;

import java.util.ArrayList;
import java.util.List;

public class Grid {
	
	private final int width, height;
	private final boolean[][] current;
	private boolean changed = true;
	public List<GridDrawable> drawables;

	public Grid() {
		this(128, 128);
	}
	
	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		this.current = new boolean[height][width];
		this.drawables = new ArrayList<>();
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
	
	public boolean isDirty() {
		return changed;
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
	
	public void renderDrawables() {
		synchronized (drawables) {
			drawables.forEach(d -> d.draw(this));
		}
	}
	
	public void addDrawable(GridDrawable gd) {
		synchronized (drawables) {
			drawables.add(gd);
		}
		changed = true;
	}
	
	public void remDrawable(GridDrawable gd) {
		synchronized (drawables) {
			 drawables.remove(gd);
		}
		changed = true;
	}
	
	public void clearDrawables() {
		synchronized (drawables) {
			drawables.clear();
		}
		changed = true;
	}
	
}
