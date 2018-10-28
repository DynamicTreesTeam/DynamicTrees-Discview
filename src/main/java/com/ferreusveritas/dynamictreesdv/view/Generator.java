package com.ferreusveritas.dynamictreesdv.view;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.ferreusveritas.dynamictrees.api.worldgen.IPoissonDebug;
import com.ferreusveritas.dynamictrees.systems.poissondisc.PoissonDisc;
import com.ferreusveritas.dynamictrees.systems.poissondisc.PoissonDiscProvider;
import com.ferreusveritas.dynamictrees.systems.poissondisc.Vec2i;
import com.ferreusveritas.dynamictreesdv.RadiusCoordinator;

public class Generator implements Runnable, IPoissonDebug {
	private volatile boolean running = false;
	private PoissonDiscProvider provider;
	private Grid grid;
	
	private final Object debugPauseLock = new Object();
	private volatile boolean debugPaused = false;
	
	private volatile boolean generate = false;
	
	private Thread current;
	
	public Generator(Grid grid) {
		provider = new PoissonDiscProvider(new RadiusCoordinator(new Random()));
		provider.setDebug(this);
		this.grid = grid;
	}
	
	public void setSeed(Long seed) {
		provider.setSeed(seed);
	}
	
	public List<PoissonDisc> getPoissonDiscs(int chunkX, int chunkY, int chunkZ) {
		return provider.getPoissonDiscs(chunkX, chunkY, chunkZ);
	}
	
	@Override
	public void run () {
		while (running){
			sleep(100);
			generateDiscs();
		}
	}
	
	public void debugPause() {
		while (running && debugPaused){
			synchronized (debugPauseLock) {
				if (debugPaused) {
					try {
						debugPauseLock.wait(); // will cause this Thread to block until 
					} catch (InterruptedException ex) {
						continue;
					}
				}
			}
		}
	}
	
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void next() {
		debugPaused = true;
		if(running) {
			debugPause();
		}
	}
	
	public void step() {
		debugResume();
	}
	
	public void debugResume() {
		synchronized (debugPauseLock) {
			debugPaused = false;
			debugPauseLock.notifyAll(); // Unblocks thread
		}
	}
	
	public void startGeneration() {
		generate = true;
	}
	
	public void generateDiscs() {
		if(generate) {
			System.out.println("generateDiscs");
						
			for(int cz = 0; cz < 8; cz++) {
				for(int cx = 0; cx < 8; cx++) {
					List<PoissonDisc> discs = getPoissonDiscs(cx, 0, cz);
					for(PoissonDisc disc : discs) {
						grid.addDrawable(new GridDisc(disc));
					}
				}
			}
			
			generate = false;
		}
	}
	
	public void start() {
		System.out.println("start");
		if(!running) {
			running = true;
			current = new Thread(this);
			current.start();
		}
	}
	
	public void stop() {
		System.out.println("stop");
		if(running) {
			running = false;
			debugResume();
			if(current != null) {
				try {
					current.join();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				current = null;
			}
		}
	}
	
	@Override
	public void begin(int chunkX, int chunkZ) {
		System.out.println("begin");
		next();
	}
	
	@Override
	public void collectSolved(List<PoissonDisc> discs) {
		System.out.println("collectSolved");
		next();
	}
	
	@Override
	public void doEdgeMasking(List<PoissonDisc> discs) {
		System.out.println("doEdgeMasking");
		next();
	}
	
	@Override
	public void maskSolvedDiscs(List<PoissonDisc> discs) {
		System.out.println("maskSolvedDiscs");
		next();
	}
	
	@Override
	public void createRootDisc(List<PoissonDisc> discs, PoissonDisc rootDisc) {
		System.out.println("createRootDisc");
		GridDisc disc = new GridDisc(rootDisc);
		grid.addDrawable(disc);
		next();
		grid.remDrawable(disc);
	}
	
	@Override
	public void gatherUnsolved(List<PoissonDisc> unsolvedDiscs, List<PoissonDisc> discs) {
		System.out.println("gatherUnsolved");
		next();
	}
	
	@Override
	public void updateCount(int count) {
		System.out.println("updateCount");
		next();
	}
	
	@Override
	public void pickMasterDisc(PoissonDisc master, List<PoissonDisc> unsolvedDiscs) {
		System.out.println("pickMasterDisc");
		next();
	}
	
	@Override
	public void getRadius(PoissonDisc master, int radius) {
		System.out.println("getRadius");
		next();
	}
	
	@Override
	public void findSecondDisc(PoissonDisc master, Vec2i slavePos) {
		System.out.println("findSecondDisc");
		next();
	}
	
	@Override
	public void maskMasterSlave(PoissonDisc master, PoissonDisc slave) {
		System.out.println("maskMasterSlave");
		next();
	}
	
	@Override
	public void intersectingList(PoissonDisc slave, Map<Integer, PoissonDisc> intersecting, List<PoissonDisc> discs) {
		System.out.println("intersectingList");
		next();
	}
	
	@Override
	public void findThirdDiscCandidate(PoissonDisc master1, PoissonDisc master2, PoissonDisc slave) {
		System.out.println("findThirdDiscCandidate");
		next();
	}
	
	@Override
	public void findThirdDiscSolved(PoissonDisc slave) {
		System.out.println("findThirdDiscSolved");
		next();
	}
	
	@Override
	public void solveDiscs(List<PoissonDisc> unsolvedDiscs, List<PoissonDisc> discs) {
		System.out.println("solveDiscs");
		next();
	}
	
	@Override
	public void gatherUnsolved2(List<PoissonDisc> unsolvedDiscs, List<PoissonDisc> discs) {
		System.out.println("gatherUnsolved2");
		next();
	}
	
}
