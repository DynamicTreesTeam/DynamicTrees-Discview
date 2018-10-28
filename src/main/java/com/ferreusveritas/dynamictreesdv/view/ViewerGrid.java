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

import javax.swing.JPanel;

public class ViewerGrid extends JPanel {
	
	private static final long serialVersionUID = 6672294391228363761L;
	//private Cursor crosshair = new Cursor(Cursor.CROSSHAIR_CURSOR);
	private Cursor pointer = new Cursor(Cursor.DEFAULT_CURSOR);
	private Grid grid;
	private int width, height;
	private int tileWidth, tileHeight;
	private boolean leftMousePressed, rightMousePressed;
	private BufferedImage canvas;
	Long seed = null;
	
	private Generator generator;
	
	public ViewerGrid() {
		super(true);
		this.width = 512;
		this.height = 512;
		grid = new Grid();
		this.tileWidth = width / grid.getWidth();
		this.tileHeight = height / grid.getHeight();
		setPreferredSize(new Dimension(width, height));
		setSize(new Dimension(width, height));
		setBackground(new Color(220, 220, 220));
		canvas = new BufferedImage(grid.getWidth(), grid.getHeight(), BufferedImage.TYPE_INT_ARGB);
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
		if (!grid.changed) return;
		
		Graphics2D g = (Graphics2D) getGraphics();
		for (int y = 0; y < grid.getHeight(); y++) {
			for (int x = 0; x < grid.getWidth(); x++) {
				canvas.setRGB(x, y, grid.getBlock(x, y) ? 0xFF000000 : bgColor(x, y));
			}
		}
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g.drawImage(canvas, 0, 0, width, height, null);
		
		grid.clean();
	}
	
	public void doInput() {
		if (rightMousePressed != leftMousePressed) {
			boolean newState = leftMousePressed;
			Point pos = getMousePosition();
			if ((pos != null) && (pos.getX() >= 0.0D) && (pos.getY() >= 0.0D)) {
				int mouseX = (int) (pos.getX() / tileWidth);
				int mouseY = (int) (pos.getY() / tileHeight);
				if ((mouseY < grid.getHeight()) && (mouseX < grid.getWidth())) {
					grid.setBlock(mouseX, mouseY, newState);
				}
			}
		}
	}
	
	public void clear() {
		if(generator != null) {
			generator.stop();
		}
		System.out.println("clear");
		generator = new Generator(grid);
		generator.setSeed(seed);
		grid.clear();
	}
	
	public void update() {
		//Do nothing for now
	}
	
	public void generate() {
		System.out.println("generate");
		//clear();
		generator.startGeneration();
	}
	
	public void step() {
		generator.step();
	}
	
	public void startThread() {
		generator.start();
	}
	
	public void stopThread() {
		generator.stop();
	}
	
	public int getGridWidth() {
		return this.grid.getWidth();
	}
	
	public int getGridHeight() {
		return this.grid.getHeight();
	}
	
	public void setSeed(String text) {
		try {
			seed = Long.parseLong(text);
		} catch (NumberFormatException e) {
			seed = null;
		}
	}
	
}
