package com.ferreusveritas.dynamictreesdv.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.ferreusveritas.dynamictrees.api.worldgen.IPoissonDebug;
import com.ferreusveritas.dynamictrees.systems.poissondisc.PoissonDisc;
import com.ferreusveritas.dynamictrees.systems.poissondisc.PoissonDiscProvider;
import com.ferreusveritas.dynamictreesdv.RadiusCoordinator;

public class Generator implements Runnable, IPoissonDebug {
	
	private final int MASTER = 0xFFDD0066;
	private final int INTERSECT = 0xFF880022;
	private final int SLAVE = 0xFFCCCC22;
	private final int UNSOLVED = 0xFF555555;
	private final int SOLVED = 0xFF0000CC;
	
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
	
	private void next(List<GridDrawable> drawables) {
		grid.addDrawables(drawables);
		next();
		grid.remDrawables(drawables);
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
						grid.addDrawable(new GridDisc(disc, 0xFF000000));
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
		List<GridDrawable> drawables = new ArrayList<>();
		discs.forEach(d -> drawables.add(new GridDisc(d, SOLVED)));
		next(drawables);
	}
	
	@Override
	public void doEdgeMasking(List<PoissonDisc> discs) {
		System.out.println("doEdgeMasking");
		List<GridDrawable> drawables = new ArrayList<>();
		discs.forEach(d -> drawables.add(new GridDisc(d, SOLVED)));
		next(drawables);
	}
	
	@Override
	public void maskSolvedDiscs(List<PoissonDisc> discs) {
		System.out.println("maskSolvedDiscs");
		next();
	}
	
	@Override
	public void createRootDisc(List<PoissonDisc> allDiscs, PoissonDisc rootDisc) {
		System.out.println("createRootDisc");
		List<GridDrawable> drawables = new ArrayList<>();
		drawables.add(new GridDisc(rootDisc, MASTER));
		next(drawables);
	}
	
	@Override
	public void gatherUnsolved(List<PoissonDisc> unsolvedDiscs, List<PoissonDisc> allDiscs) {
		System.out.println("gatherUnsolved");
		List<GridDrawable> drawables = new ArrayList<>();
		allDiscs.forEach(d -> drawables.add(new GridDisc(d, SOLVED)));
		unsolvedDiscs.forEach(d -> drawables.add(new GridDisc(d, UNSOLVED)));
		next(drawables);
	}
	
	@Override
	public void updateCount(int count, List<PoissonDisc> unsolvedDiscs, List<PoissonDisc> allDiscs) {
		System.out.println("updateCount: " + count);
		List<GridDrawable> drawables = new ArrayList<>();
		allDiscs.forEach(d -> drawables.add(new GridDisc(d, SOLVED)));
		unsolvedDiscs.forEach(d -> drawables.add(new GridDisc(d, UNSOLVED)));
		next(drawables);
	}
	
	@Override
	public void pickMasterDisc(PoissonDisc master, List<PoissonDisc> unsolvedDiscs, List<PoissonDisc> allDiscs) {
		System.out.println("pickMasterDisc");
		List<GridDrawable> drawables = new ArrayList<>();
		allDiscs.forEach(d -> drawables.add(new GridDisc(d, SOLVED)));
		unsolvedDiscs.forEach(d -> drawables.add(new GridDisc(d, UNSOLVED)));
		drawables.add(new GridDisc(master, MASTER));
		next(drawables);
	}
	
	@Override
	public void getRadius(PoissonDisc master, int radius, List<PoissonDisc> unsolvedDiscs, List<PoissonDisc> allDiscs) {
		System.out.println("getRadius: " + radius);
		List<GridDrawable> drawables = new ArrayList<>();
		allDiscs.forEach(d -> drawables.add(new GridDisc(d, SOLVED)));
		unsolvedDiscs.forEach(d -> drawables.add(new GridDisc(d, UNSOLVED)));
		next(drawables);
	}
	
	@Override
	public void findSecondDisc(PoissonDisc master, PoissonDisc slave, List<PoissonDisc> unsolvedDiscs, List<PoissonDisc> allDiscs) {
		System.out.println("findSecondDisc");
		List<GridDrawable> drawables = new ArrayList<>();
		allDiscs.forEach(d -> drawables.add(new GridDisc(d, SOLVED)));
		unsolvedDiscs.forEach(d -> drawables.add(new GridDisc(d, UNSOLVED)));
		drawables.add(new GridDisc(master, MASTER));
		drawables.add(new GridDisc(slave, SLAVE));
		next(drawables);
	}
	
	@Override
	public void maskMasterSlave(PoissonDisc master, PoissonDisc slave, List<PoissonDisc> unsolvedDiscs, List<PoissonDisc> allDiscs) {
		System.out.println("maskMasterSlave");
		List<GridDrawable> drawables = new ArrayList<>();
		allDiscs.forEach(d -> drawables.add(new GridDisc(d, SOLVED)));
		unsolvedDiscs.forEach(d -> drawables.add(new GridDisc(d, UNSOLVED)));
		drawables.add(new GridDisc(master, MASTER));
		drawables.add(new GridDisc(slave, SLAVE));
		next(drawables);
	}
	
	@Override
	public void intersectingList(PoissonDisc slave, Map<Integer, PoissonDisc> intersecting, List<PoissonDisc> allDiscs) {
		System.out.println("intersectingList");
		List<GridDrawable> drawables = new ArrayList<>();
		allDiscs.forEach(d -> drawables.add(new GridDisc(d, SOLVED)));
		intersecting.forEach( (k,d) -> drawables.add(new GridDisc(d, INTERSECT)));
		drawables.add(new GridDisc(slave, SLAVE));
		next(drawables);
	}
	
	@Override
	public void findThirdDiscCandidate(PoissonDisc master1, PoissonDisc master2, PoissonDisc slave, List<PoissonDisc> unsolvedDiscs, List<PoissonDisc> allDiscs) {
		System.out.println("findThirdDiscCandidate");
		List<GridDrawable> drawables = new ArrayList<>();
		allDiscs.forEach(d -> drawables.add(new GridDisc(d, SOLVED)));
		unsolvedDiscs.forEach(d -> drawables.add(new GridDisc(d, UNSOLVED)));
		drawables.add(new GridDisc(master1, MASTER));
		drawables.add(new GridDisc(master2, MASTER));
		drawables.add(new GridDisc(slave, SLAVE));
		next(drawables);

	}
	
	@Override
	public void thirdCircleCandidateIntersects(PoissonDisc master1, PoissonDisc master2, PoissonDisc slave, PoissonDisc intersecting, List<PoissonDisc> unsolvedDiscs, List<PoissonDisc> allDiscs) {
		System.out.println("thirdCircleCandidateIntersects");
		List<GridDrawable> drawables = new ArrayList<>();
		allDiscs.forEach(d -> drawables.add(new GridDisc(d, SOLVED)));
		unsolvedDiscs.forEach(d -> drawables.add(new GridDisc(d, UNSOLVED)));
		drawables.add(new GridDisc(master1, MASTER));
		drawables.add(new GridDisc(master2, MASTER));
		drawables.add(new GridDisc(slave, SLAVE));
		drawables.add(new GridDisc(intersecting, INTERSECT));
		next(drawables);
	}
	
	@Override
	public void findThirdDiscSolved(PoissonDisc slave, List<PoissonDisc> unsolvedDiscs, List<PoissonDisc> allDiscs) {
		System.out.println("findThirdDiscSolved");
		List<GridDrawable> drawables = new ArrayList<>();
		allDiscs.forEach(d -> drawables.add(new GridDisc(d, SOLVED)));
		unsolvedDiscs.forEach(d -> drawables.add(new GridDisc(d, UNSOLVED)));
		drawables.add(new GridDisc(slave, SLAVE));
		next(drawables);
	}
	
	@Override
	public void solveDiscs(List<PoissonDisc> unsolvedDiscs, List<PoissonDisc> allDiscs) {
		System.out.println("solveDiscs");
		List<GridDrawable> drawables = new ArrayList<>();
		allDiscs.forEach(d -> drawables.add(new GridDisc(d, SOLVED)));
		unsolvedDiscs.forEach(d -> drawables.add(new GridDisc(d, UNSOLVED)));
		next(drawables);
	}
	
	@Override
	public void gatherUnsolved2(List<PoissonDisc> unsolvedDiscs, List<PoissonDisc> allDiscs) {
		System.out.println("gatherUnsolved2");
		List<GridDrawable> drawables = new ArrayList<>();
		allDiscs.forEach(d -> drawables.add(new GridDisc(d, SOLVED)));
		unsolvedDiscs.forEach(d -> drawables.add(new GridDisc(d, UNSOLVED)));
		next(drawables);
	}
	
}
