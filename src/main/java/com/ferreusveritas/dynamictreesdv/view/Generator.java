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
	private volatile boolean paused = false;
	private final Object pauseLock = new Object();
	private PoissonDiscProvider provider;
	private Grid grid;
	
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
			synchronized (pauseLock) {
				if (!running) { // may have changed while waiting to
					// synchronize on pauseLock
					break;
				}
				if (paused) {
					try {
						pauseLock.wait(); // will cause this Thread to block until 
						// another thread calls pauseLock.notifyAll()
						// Note that calling wait() will 
						// relinquish the synchronized lock that this 
						// thread holds on pauseLock so another thread
						// can acquire the lock to call notifyAll()
						// (link with explanation below this code)
					} catch (InterruptedException ex) {
						break;
					}
					if (!running) { // running might have changed since we paused
						break;
					}
				}
			}
			
			try {
				Thread.sleep(2);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// Your code here
			System.out.println("running");
			doSomething();
			
		}
	}
	
	public void doSomething() {
		Random rand = new Random();
		grid.setBlock(rand.nextInt(grid.getWidth()), rand.nextInt(grid.getHeight()), true);
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
		resume();
		// to unblock
	}
	
	public void pause() {
		System.out.println("pause");
		// you may want to throw an IllegalStateException if !running
		paused = true;
	}
	
	public void resume() {
		synchronized (pauseLock) {
			System.out.println("resume");
			paused = false;
			pauseLock.notifyAll(); // Unblocks thread
		}
	}
	
	@Override
	public void begin(int chunkX, int chunkZ) {
		System.out.println("begin");
		// TODO Auto-generated method stub
	}
	
	@Override
	public void collectSolved(List<PoissonDisc> discs) {
		System.out.println("collectSolved");
		// TODO Auto-generated method stub
	}
	
	@Override
	public void doEdgeMasking(List<PoissonDisc> discs) {
		System.out.println("doEdgeMasking");
		// TODO Auto-generated method stub
	}
	
	@Override
	public void maskSolvedDiscs(List<PoissonDisc> discs) {
		System.out.println("maskSolvedDiscs");
		// TODO Auto-generated method stub
	}
	
	@Override
	public void createRootDisc(List<PoissonDisc> discs, PoissonDisc rootDisc) {
		System.out.println("createRootDisc");
		// TODO Auto-generated method stub
	}
	
	@Override
	public void gatherUnsolved(List<PoissonDisc> unsolvedDiscs, List<PoissonDisc> discs) {
		System.out.println("gatherUnsolved");
		// TODO Auto-generated method stub
	}
	
	@Override
	public void updateCount(int count) {
		System.out.println("updateCount");
		// TODO Auto-generated method stub
	}
	
	@Override
	public void pickMasterDisc(PoissonDisc master, List<PoissonDisc> unsolvedDiscs) {
		System.out.println("pickMasterDisc");
		// TODO Auto-generated method stub
	}
	
	@Override
	public void getRadius(PoissonDisc master, int radius) {
		System.out.println("getRadius");
		// TODO Auto-generated method stub
	}
	
	@Override
	public void findSecondDisc(PoissonDisc master, Vec2i slavePos) {
		System.out.println("findSecondDisc");
		// TODO Auto-generated method stub
	}
	
	@Override
	public void maskMasterSlave(PoissonDisc master, PoissonDisc slave) {
		System.out.println("maskMasterSlave");
		// TODO Auto-generated method stub
	}
	
	@Override
	public void intersectingList(PoissonDisc slave, Map<Integer, PoissonDisc> intersecting, List<PoissonDisc> discs) {
		System.out.println("intersectingList");
		// TODO Auto-generated method stub
	}
	
	@Override
	public void findThirdDiscCandidate(PoissonDisc master1, PoissonDisc master2, PoissonDisc slave) {
		System.out.println("findThirdDiscCandidate");
		// TODO Auto-generated method stub
	}
	
	@Override
	public void findThirdDiscSolved(PoissonDisc slave) {
		System.out.println("findThirdDiscSolved");
		// TODO Auto-generated method stub
	}
	
	@Override
	public void solveDiscs(List<PoissonDisc> unsolvedDiscs, List<PoissonDisc> discs) {
		System.out.println("solveDiscs");
		// TODO Auto-generated method stub
	}
	
	@Override
	public void gatherUnsolved2(List<PoissonDisc> unsolvedDiscs, List<PoissonDisc> discs) {
		System.out.println("gatherUnsolved2");
		// TODO Auto-generated method stub
	}
	
}
