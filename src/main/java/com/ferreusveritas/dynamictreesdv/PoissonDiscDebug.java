package com.ferreusveritas.dynamictreesdv;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.systems.poissondisc.PoissonDisc;

import net.minecraft.world.gen.NoiseGeneratorPerlin;

public class PoissonDiscDebug {
	
	public static int scale = 8;
	
	public static void outputCirclesToPng(List<PoissonDisc> circles, int chunkX, int chunkZ, String name) {
		int width = 48 * scale;
		int height = 48 * scale;
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		//Draw virtual chunks
		Color lightGrey = new Color(186, 189, 182);
		Color darkGrey = new Color(136, 138, 133);
		
		for(int gz = 0; gz < 3; gz++) {
			for(int gx = 0; gx < 3; gx++) {
				drawRect(img, gx * 16 * scale, gz * 16 * scale, 16 * scale, 16 * scale, ((gz * 3 + gx) % 2) == 0 ? lightGrey : darkGrey);
			}
		}
		
		//Draw actual chunks(circles are shifted by x+8, z+8)
		Color blue1 = new Color(0, 0, 128, 32);
		Color blue2 = new Color(0, 64, 128, 32);
		
		for(int gz = 0; gz < 3; gz++) {
			for(int gx = 0; gx < 3; gx++) {
				drawRect(img, ((gx * 16) - 8) * scale, ((gz * 16) - 8) * scale, 16 * scale, 16 * scale, ((gz * 3 + gx) % 2) == 0 ? blue1 : blue2);
			}
		}
		
		//Draw circles
		for(PoissonDisc c: circles) {
			drawCircle(img, c, (chunkX - 1) * 16, (chunkZ - 1) * 16);
		}
		
		//Draw the compass
		drawLine(img, 4, 4, 4, 12, Color.BLACK);
		drawLine(img, 4, 4, 12, 12, Color.BLACK);
		drawLine(img, 12, 4, 12, 12, Color.BLACK);
		drawLine(img, 8, 16, 8, 32, Color.RED);
		drawLine(img, 8, 16, 8 - 5, 16 + 5, Color.RED);
		drawLine(img, 8, 16, 8 + 5, 16 + 5, Color.RED);
		
		
		if(name.isEmpty()) {
			name = "unresolved-" + chunkX + ":" + chunkZ;
		}
		
		Logger.getLogger(ModConstants.MODID).log(Level.INFO, "Writing:" + name + ".png");
		
		try {
			ImageIO.write(img, "png", new File("./unsolved/" + name + ".png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void drawCircle(BufferedImage image, PoissonDisc circle, int xOffset, int zOffset) {
		Color green = new Color(115, 210, 22, circle.real ? 192 : 64);
		Color red = new Color(204, 0, 0, circle.real ? 192 : 64);
		Color col = circle.hasFreeAngles() ? red : green;
		
		int startX = circle.x - circle.radius;
		int stopX = circle.x + circle.radius;
		int startZ = circle.z - circle.radius;
		int stopZ = circle.z + circle.radius;
		
		for(int z = startZ; z <= stopZ; z++) {
			for(int x = startX; x <= stopX; x++) {
				if(circle.isInside(x, z)) {
					drawRect(image, (x - xOffset) * scale, (z - zOffset) * scale, scale, scale, col);
					//safeSetRGB(image, x - xOffset, z - zOffset, col);
				}
			}
		}
		
		//Draw arc segments
		double radius = circle.radius + 0.5f;
		
		for(int i = 0; i < 32; i++) {
			boolean isOn = (circle.arc & (1 << i)) != 0;
			double x1 = circle.x + 0.5 + Math.cos(Math.PI * 2 * i / 32.0) * radius;
			double z1 = circle.z + 0.5 + Math.sin(Math.PI * 2 * i / 32.0) * radius;
			double x2 = circle.x + 0.5 + Math.cos(Math.PI * 2 * (i + 1)/ 32.0) * radius;
			double z2 = circle.z + 0.5 + Math.sin(Math.PI * 2 * (i + 1) / 32.0) * radius;
			drawLine(image, (int)((x1 - xOffset) * scale), (int)((z1 - zOffset) * scale), (int)((x2 - xOffset) * scale), (int)((z2 - zOffset) * scale), (((i & 1) == 0) ? (isOn ? Color.BLACK : Color.PINK)  : (isOn ? Color.DARK_GRAY : Color.CYAN)));
		}
	}
	
	public static void drawLine(BufferedImage image, int x1, int z1, int x2, int z2, Color color) {
		int w = x2 - x1;
		int h = z2 - z1;
		int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
		if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1;
		if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1;
		if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1;
		int longest = Math.abs(w);
		int shortest = Math.abs(h);
		if (!(longest>shortest)) {
			longest = Math.abs(h);
			shortest = Math.abs(w);
			if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1;
			dx2 = 0;
		}
		int numerator = longest >> 1;
			for (int i=0;i<=longest;i++) {
				safeSetRGB(image, x1,z1,color);
				numerator += shortest;
				if (!(numerator<longest)) {
					numerator -= longest;
					x1 += dx1;
					z1 += dy1;
				} else {
					x1 += dx2;
					z1 += dy2;
				}
			}
	}
	
	public static void drawRect(BufferedImage image, int x, int z, int w, int h, Color color) {
		for(int zi = 0; zi < h; zi++) {
			for(int xi = 0; xi < w; xi++) {
				safeSetRGB(image, x + xi, z + zi, color);
			}
		}
	}
	
	public static void safeSetRGB(BufferedImage image, int x, int z, Color color) {
		if(x >= 0 && z >= 0 && x < image.getWidth() && z < image.getHeight()){
			color.getAlpha();
			Color dst = new Color(image.getRGB(x, z));
			
			float dr = dst.getRed() / 255f;
			float dg = dst.getGreen() / 255f;
			float db = dst.getBlue() / 255f;			
			float sr = color.getRed() / 255f;
			float sg = color.getGreen() / 255f;
			float sb = color.getBlue() / 255f;
			float sa = color.getAlpha() / 255f;
			
			//Simple Alpha blending
			image.setRGB(x, z, new Color(sr * sa + dr * (1f - sa), sg * sa + dg * (1f - sa), sb * sa + db * (1f - sa)).getRGB());
		}
	}
	
	public static void initNoisetest() {
		
		int width = 128;
		int height = 128;
		
		NoiseGeneratorPerlin noiseGenerator = new NoiseGeneratorPerlin(new Random(2), 1);
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		for(int oct = 0; oct < 7; oct++) {
			System.out.println("Noise" + oct);
			
			for(int i = 0; i < width; i++) {
				for(int j = 0; j < height; j++) {
					
					float noise = (float) ((noiseGenerator.getValue(i / 64.0, j / 64.0) + 1D) / 2.0D);
					
					switch(oct){
					case 6:	noise += (float) ((noiseGenerator.getValue(i / 1.0, j / 1.0) + 1D) / 2.0D) / 64;
					case 5:	noise += (float) ((noiseGenerator.getValue(i / 2.0, j / 2.0) + 1D) / 2.0D) / 32;
					case 4:	noise += (float) ((noiseGenerator.getValue(i / 4.0, j / 4.0) + 1D) / 2.0D) / 16;
					case 3:	noise += (float) ((noiseGenerator.getValue(i / 8.0, j / 8.0) + 1D) / 2.0D) / 8;
					case 2:	noise += (float) ((noiseGenerator.getValue(i / 16.0, j / 16.0) + 1D) / 2.0D) / 4;
					case 1: noise += (float) ((noiseGenerator.getValue(i / 32.0, j / 32.0) + 1D) / 2.0D) / 2;
					}
					
					noise /= 2;
					int value = (int) (255 * noise);
					
					img.setRGB(i, j, new Color(value, value, value).getRGB());
				}
			}
			
			try {
				ImageIO.write(img, "png", new File("./" + "noise" + oct + ".png"));
			} catch(IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
////////////////////////////////////////////////////////////
	
	public static void saveImage(BufferedImage img, String directory, String name) throws IOException {
		ImageIO.write(img, "png", new File(directory + name + ".png"));
	}
	
	public static void setAlpha(BufferedImage img, int alpha) {
		for(int i = 0; i < img.getWidth(); i++) {
			for(int j = 0; j < img.getHeight(); j++) {
				Color currentColor = new Color(img.getRGB(i, j));
				Color newColor = new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), alpha);
				
				img.setRGB(i, j, newColor.getRGB());
			}
		}
	}
	
}
