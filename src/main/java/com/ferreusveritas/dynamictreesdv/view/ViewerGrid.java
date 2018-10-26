package com.ferreusveritas.dynamictreesdv.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class ViewerGrid extends JPanel {

	private Cursor crosshair = new Cursor(Cursor.CROSSHAIR_CURSOR);
	private Cursor pointer = new Cursor(Cursor.DEFAULT_CURSOR);
	
	private int width, height;
	private int gridWidth, gridHeight;
	private int tileWidth, tileHeight;
	private boolean[][] current;
	private boolean[][] next;
	private ArrayList<Integer> born = new ArrayList();
	private ArrayList<Integer> lives = new ArrayList();
	private boolean active;
	private boolean leftMousePressed, rightMousePressed;
	public boolean gridChanged = true;
	private BufferedImage canvas;

	public ViewerGrid() {
		super(true);
		this.width = 500;
		this.height = 500;
		this.gridWidth = 100;
		this.gridHeight = 100;
		this.tileWidth = width / gridWidth;
		this.tileHeight = height / gridHeight;
		this.current = new boolean[gridHeight][gridWidth];
		this.next = new boolean[gridHeight][gridWidth];
		this.born.add(3);
		this.lives.add(2);
		this.lives.add(3);
		setPreferredSize(new Dimension(width, height));
		setSize(new Dimension(width, height));
		setBackground(new Color(220, 220, 220));
		canvas = new BufferedImage(gridWidth, gridHeight, BufferedImage.TYPE_INT_ARGB);
		setActive(false);
		this.leftMousePressed = false;
		this.rightMousePressed = false;
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
	}

	public void clear() {
		for (int y = 0; y < this.gridHeight; y++) {
			for (int x = 0; x < this.gridWidth; x++) {
				this.current[y][x] = false;
				this.next[y][x] = false;
			}
		}
		gridChanged = true;
	}

	public void draw() {
		if (!gridChanged) return;
		
		Graphics2D g = (Graphics2D) getGraphics();
		for (int y = 0; y < gridHeight; y++) {
			for (int x = 0; x < gridWidth; x++) {
				canvas.setRGB(x, y, current[y][x] ? 0xFF000000 : 0xFFDCDCDC);
			}
		}
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g.drawImage(canvas, 0, 0, width, height, null);
		
		gridChanged = false;
	}

	public void doInput() {
		if (!active && (rightMousePressed != leftMousePressed)) {
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

	public void parseRules(String nums) {
		try {
			lives.clear();
			born.clear();
			int index = 0;
			for (int i = 1; i < nums.length() + 1; i++) {
				if (nums.substring(i - 1, i).equals("/")) {
					index = i + 1;
					break;
				}
				lives.add(Integer.parseInt(nums.substring(i - 1, i)));
			}
			for (int i = index; i < nums.length() + 1; i++) {
				born.add(Integer.parseInt(nums.substring(i - 1, i)));
			}
		} catch (NumberFormatException e) {
		}
	}

	public void setActive(boolean active) {
		this.active = active;
		this.setCursor(active ? pointer : crosshair);
	}

	public void update() {
		if (!active) return;
		
		for (int y = 0; y < gridHeight; y++) {
			for (int x = 0; x < gridWidth; x++) {
				int neighbors = 0;
				for (int nY = -1; nY <= 1; nY++) {
					for (int nX = -1; nX <= 1; nX++) {
						if (nX == 0 && nY == 0) continue;
						if (current[wrap(y + nY, gridHeight)][wrap(x + nX, gridWidth)]) neighbors++;
					}
				}

				if (!current[y][x] && canBirth(neighbors)) next[y][x] = true;
				else if (current[y][x] && canLive(neighbors)) next[y][x] = true;
				else next[y][x] = false;
			}
		}
		
		for (int y = 0; y < this.gridHeight; y++) {
			for (int x = 0; x < this.gridWidth; x++) {
				current[y][x] = next[y][x];
				next[y][x] = false;
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

	public boolean canLive(int num) {
		return lives.contains(num);
	}

	public boolean canBirth(int num) {
		return born.contains(num);
	}

	public int wrap(int num, int max) {
		if (num >= max) return 0;
		if (num <= -1) return max - 1;
		return num;
	}
}
