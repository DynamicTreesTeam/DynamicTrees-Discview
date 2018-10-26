package com.ferreusveritas.dynamictreesdv.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DiscView implements Runnable {
	
	public Thread viewerThread;
	private final AtomicBoolean running = new AtomicBoolean(false);
	public JPanel panel;
	public JFrame window;
	public BufferedImage canvas;
	public Graphics2D graphics;
	public int t = 0;
	
	public DiscView() {
		panel = new JPanel(true);
		panel.setPreferredSize(new Dimension(512, 512));
		panel.setBackground(new Color(255, 255, 255));
		window = new JFrame("Poisson Disc Viewer");
		window.setContentPane(panel);
		window.pack();
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		window.pack();
		window.setResizable(false);
		window.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		    	System.out.println("Closing Viewer");
		    	stop();
		    }
		});
		
		canvas = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
		graphics = (Graphics2D) panel.getGraphics();
		
		viewerThread = new Thread(this);
		viewerThread.start();
	}
	
	public void draw() {
		//g.clearRect(0, 0, panel.getWidth(), panel.getHeight());
		for (int y = 0; y < canvas.getHeight(); y++) {
			for (int x = 0; x < canvas.getWidth(); x++) {
				if (y < (t % canvas.getHeight()) && x < (t % canvas.getWidth())) {
					canvas.setRGB(x, y, 0xFF000000);
				} else {
					canvas.setRGB(x, y, 0xFFFFFFFF);
				}
			}
		}
		t++;
		graphics.drawImage(canvas, 0, 0, panel.getWidth(), panel.getHeight(), null);
	}

	@Override
	public void run() {
		running.set(true);
		while (running.get()) {
			draw();
			
			try {
				Thread.sleep(500L);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}
	
	public void stop() {
        running.set(false);
    }

}
