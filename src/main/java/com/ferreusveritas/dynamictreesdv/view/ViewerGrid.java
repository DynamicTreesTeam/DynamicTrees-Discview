package com.ferreusveritas.dynamictreesdv.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import com.ferreusveritas.dynamictrees.systems.poissondisc.PoissonDisc;
import com.ferreusveritas.dynamictrees.systems.poissondisc.PoissonDiscProvider;
import com.ferreusveritas.dynamictreesdv.RadiusCoordinator;

public class ViewerGrid extends JPanel {

	private static final long serialVersionUID = 6672294391228363761L;
	private Cursor crosshair = new Cursor(Cursor.CROSSHAIR_CURSOR);
	private Cursor pointer = new Cursor(Cursor.DEFAULT_CURSOR);
	
	private int width, height;
	private int gridWidth, gridHeight;
	private int tileWidth, tileHeight;
	private boolean[][] current;
	private boolean leftMousePressed, rightMousePressed;
	public boolean gridChanged = true;
	private BufferedImage canvas;
	Long seed = null;

	private PoissonDiscProvider discProvider;
	
	public ViewerGrid() {
		super(true);
		this.width = 512;
		this.height = 512;
		this.gridWidth = 128;
		this.gridHeight = 128;
		this.tileWidth = width / gridWidth;
		this.tileHeight = height / gridHeight;
		this.current = new boolean[gridHeight][gridWidth];
		setPreferredSize(new Dimension(width, height));
		setSize(new Dimension(width, height));
		setBackground(new Color(220, 220, 220));
		canvas = new BufferedImage(gridWidth, gridHeight, BufferedImage.TYPE_INT_ARGB);
		this.leftMousePressed = false;
		this.rightMousePressed = false;
		this.setCursor(pointer);
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == 1)
					ViewerGrid.this.leftMousePressed = true;
				if (e.getButton() == 3)
					ViewerGrid.this.rightMousePressed = true;
			}

			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == 1)
					ViewerGrid.this.leftMousePressed = false;
				if (e.getButton() == 3)
					ViewerGrid.this.rightMousePressed = false;
			}
		});
	
		clear();
	}
	
	private int bgColor(int x, int z) {
		boolean chunkLight = (((x / 16) + (z / 16)) & 1) == 1;
		boolean blockLight = ((x + z) & 1) == 1;
		
		//Draw virtual chunks
		int lightGrey = new Color(186, 189, 182).getRGB();
		int darkGrey = new Color(136, 138, 133).getRGB();

		int lightGreen = new Color(186, 189, 112).getRGB();
		int darkGreen = new Color(136, 138, 80).getRGB();
		
		return chunkLight ? ( blockLight ? lightGrey : darkGrey) : ( blockLight ? lightGreen : darkGreen);
	}
	
	public void draw() {
		if (!gridChanged) return;
		
		Graphics2D g = (Graphics2D) getGraphics();
		for (int y = 0; y < gridHeight; y++) {
			for (int x = 0; x < gridWidth; x++) {
				canvas.setRGB(x, y, current[y][x] ? 0xFF000000 : bgColor(x, y));
			}
		}
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g.drawImage(canvas, 0, 0, width, height, null);
		
		gridChanged = false;
	}
	
	public void doInput() {
		if (rightMousePressed != leftMousePressed) {
			boolean newState = leftMousePressed;
			Point pos = getMousePosition();
			if ((pos != null) && (pos.getX() >= 0.0D) && (pos.getY() >= 0.0D)) {
				int mouseX = (int) (pos.getX() / tileWidth);
				int mouseY = (int) (pos.getY() / tileHeight);
				if ((mouseY < gridHeight) && (mouseX < gridWidth)) {
					boolean prevState = current[mouseY][mouseX];
					current[mouseY][mouseX] = newState;
					
					if (newState != prevState) gridChanged = true;
				}
			}
		}
	}
	
	public boolean getBlock(int x, int z, boolean block) {
		if(x >= 0 && z >= 0 && x < gridWidth && z < gridHeight) {
			return current[z][x];
		}
		return false;
	}
	
	public void setBlock(int x, int z, boolean block) {
		if(x >= 0 && z >= 0 && x < gridWidth && z < gridHeight) {
			current[z][x] = block;
		}
	}
	
	public void setBlockOr(int x, int z, boolean block) {
		if(x >= 0 && z >= 0 && x < gridWidth && z < gridHeight) {
			current[z][x] |= block;
		}
	}
	
	public void clear() {
		discProvider = new PoissonDiscProvider(new RadiusCoordinator(new Random()));
		discProvider.setSeed(seed);
		
		for (int y = 0; y < this.gridHeight; y++) {
			for (int x = 0; x < this.gridWidth; x++) {
				this.current[y][x] = false;
			}
		}
		gridChanged = true;
	}
	
	public void update() {
		//Do nothing for now
	}
	
	public void generate() {
		clear();
		
		for(int cz = 0; cz < 8; cz++) {
			for(int cx = 0; cx < 8; cx++) { 
				List<PoissonDisc> discs = discProvider.getPoissonDiscs(cx, 0, cz);
				
				for(PoissonDisc d : discs) {
					int startX = d.x - d.radius;
					int stopX = d.x + d.radius;
					int startZ = d.z - d.radius;
					int stopZ = d.z + d.radius;
					
					for(int z = startZ; z <= stopZ; z++) {
						for(int x = startX; x <= stopX; x++) {
							setBlockOr(x, z, d.isEdge(x, z));
						}
					}
					
				}
				
			}
		}
		
		gridChanged = true;
	}
	
	public int getGridWidth() {
		return this.gridWidth;
	}
	
	public int getGridHeight() {
		return this.gridHeight;
	}
	
	public void setSeed(String text) {
		try {
			seed = Long.parseLong(text);
		} catch (NumberFormatException e) {
			seed = null;
		}
	}
	
}
