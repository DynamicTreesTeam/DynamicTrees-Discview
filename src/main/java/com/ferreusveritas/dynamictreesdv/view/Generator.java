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
	private volatile boolean running = true;
	private PoissonDiscProvider provider;
	private Grid grid;
	
	private final Object debugPauseLock = new Object();
	private volatile boolean debugPaused = false;
	
	private volatile boolean generate = false;
	
	
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
			
			try {
				Thread.sleep(200);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// Your code here
			//System.out.println("running");
			doSomething();
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
	
	public void doSomething() {
		Random rand = new Random();
		grid.setBlock(rand.nextInt(grid.getWidth()), rand.nextInt(grid.getHeight()), true);
	}
	
	public void startGeneration() {
		generate = true;
	}
	
	public void generateDiscs() {
		if(generate) {
			System.out.println("generateDiscs");
			
			grid.clear();
			
			for(int cz = 0; cz < 8; cz++) {
				for(int cx = 0; cx < 8; cx++) {
					List<PoissonDisc> discs = getPoissonDiscs(cx, 0, cz);
					for(PoissonDisc disc : discs) {
						grid.drawDisc(disc);
					}
				}
			}
			
			generate = false;
		}
	}
	
	public void start() {
		System.out.println("start");
		running = true;
		
		new Thread(this).start();
	}
	
	public void stop() {
		System.out.println("stop");
		running = false;
		// you might also want to interrupt() the Thread that is 
		// running this Runnable, too, or perhaps call:
		debugResume();
		// to unblock
	}
	
	@Override
	public void begin(int chunkX, int chunkZ) {
		System.out.println("begin");
		next();
		// TODO Auto-generated method stub
	}
	
	@Override
	public void collectSolved(List<PoissonDisc> discs) {
		System.out.println("collectSolved");
		next();
		// TODO Auto-generated method stub
	}
	
	@Override
	public void doEdgeMasking(List<PoissonDisc> discs) {
		System.out.println("doEdgeMasking");
		next();
		// TODO Auto-generated method stub
	}
	
	@Override
	public void maskSolvedDiscs(List<PoissonDisc> discs) {
		System.out.println("maskSolvedDiscs");
		next();
		// TODO Auto-generated method stub
	}
	
	@Override
	public void createRootDisc(List<PoissonDisc> discs, PoissonDisc rootDisc) {
		System.out.println("createRootDisc");
		next();
		// TODO Auto-generated method stub
	}
	
	@Override
	public void gatherUnsolved(List<PoissonDisc> unsolvedDiscs, List<PoissonDisc> discs) {
		System.out.println("gatherUnsolved");
		next();
		// TODO Auto-generated method stub
	}
	
	@Override
	public void updateCount(int count) {
		System.out.println("updateCount");
		next();
		// TODO Auto-generated method stub
	}
	
	@Override
	public void pickMasterDisc(PoissonDisc master, List<PoissonDisc> unsolvedDiscs) {
		System.out.println("pickMasterDisc");
		next();
		// TODO Auto-generated method stub
	}
	
	@Override
	public void getRadius(PoissonDisc master, int radius) {
		System.out.println("getRadius");
		next();
		// TODO Auto-generated method stub
	}
	
	@Override
	public void findSecondDisc(PoissonDisc master, Vec2i slavePos) {
		System.out.println("findSecondDisc");
		next();
		// TODO Auto-generated method stub
	}
	
	@Override
	public void maskMasterSlave(PoissonDisc master, PoissonDisc slave) {
		System.out.println("maskMasterSlave");
		next();
		// TODO Auto-generated method stub
	}
	
	@Override
	public void intersectingList(PoissonDisc slave, Map<Integer, PoissonDisc> intersecting, List<PoissonDisc> discs) {
		System.out.println("intersectingList");
		next();
		// TODO Auto-generated method stub
	}
	
	@Override
	public void findThirdDiscCandidate(PoissonDisc master1, PoissonDisc master2, PoissonDisc slave) {
		System.out.println("findThirdDiscCandidate");
		next();
		// TODO Auto-generated method stub
	}
	
	@Override
	public void findThirdDiscSolved(PoissonDisc slave) {
		System.out.println("findThirdDiscSolved");
		next();
		// TODO Auto-generated method stub
	}
	
	@Override
	public void solveDiscs(List<PoissonDisc> unsolvedDiscs, List<PoissonDisc> discs) {
		System.out.println("solveDiscs");
		next();
		// TODO Auto-generated method stub
	}
	
	@Override
	public void gatherUnsolved2(List<PoissonDisc> unsolvedDiscs, List<PoissonDisc> discs) {
		System.out.println("gatherUnsolved2");
		next();
		// TODO Auto-generated method stub
	}
	
}
