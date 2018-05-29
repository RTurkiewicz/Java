package Threads;

import java.util.ArrayList;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Data.Statistic;
import static java.lang.Thread.sleep;

public class ThreadTarget implements Runnable {
	
	private static final Logger logger = LogManager.getLogger(ThreadTarget.class);
	private int id;
	private Random random;
	private int minSeed = 5;
	private int maxSeed = 150;
	private int minTime = 10;
	private int maxTime = 100;
	private ThreadManager threadManager;


	public ThreadTarget(int id, ThreadManager threadManager) {
		this.id = id;
		this.threadManager = threadManager;
		random = new Random();
		StartTreadInfo(id);
	}

	@Override
	/**
	 * {@link java.lang.Thread}
	 */
	public void run() {
		random = new Random();
		while (true) {
			int seed = random.nextInt(maxSeed - minSeed + 1) + minSeed;

			logger.info("Thread " + id + " seed " + seed);
			
			switch(seed%3) {
			case 2:
				logger.info("Thread " + id + " seed " + seed + " average "
						+ Data.Statistic.Average(threadManager.GetList(seed, 50, 100)));
				break;
				
			case 1:
				logger.info("Thread " + id + " seed " + seed + " size "
						+ Data.Statistic.Size(threadManager.GetList(seed, 50, 100)));
				break;
				
			case 0:
				logger.info("Thread " + id + " seed " + seed + " sum "
						+ Data.Statistic.Sum(threadManager.GetList(seed, 50, 100)));
				break;
			}


			try {
				//
				sleep(minTime + random.nextInt(maxTime - minSeed + 1));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void StartTreadInfo(int id) {
		logger.info("New thread created: " + id);
	}

}
