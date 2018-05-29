package Threads;

import Data.DataGenerator;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadManager {
	
	private int deletecounter = 0;
	private static final Logger logger = LogManager.getLogger(ThreadManager.class);
	private ArrayList<Threads.ThreadTarget> threadsArrayList = new ArrayList<Threads.ThreadTarget>();


	@SuppressWarnings("rawtypes")
	private HashMap<Integer, SoftReference<ArrayList>> hashMap = new HashMap<Integer, SoftReference<ArrayList>>();


	@SuppressWarnings("rawtypes")
	private ReferenceQueue<ArrayList> referenceQueue = new ReferenceQueue<>();


	public ThreadManager(int numberOfThreads) {

		for (int i = 0; i < numberOfThreads; i++) {
			threadsArrayList.add(new Threads.ThreadTarget(i, this));
			logger.info("New thread created: " + i);
		}

		for (Threads.ThreadTarget threadTarget : threadsArrayList) {
			Thread thread = new Thread(threadTarget);
			thread.start();
		}
		// referenceQueue.
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ThreadTarget> GetList(int seed, int minValue, int maxValue) {

		synchronized (hashMap) {

			logger.info("Requesting " + seed);

			// If the specified key is not already associated with a value
			hashMap.putIfAbsent(seed,
					new SoftReference<>(DataGenerator.GenerateList(seed, minValue, maxValue), referenceQueue));

			// Create a new list and overwrite the old list if data beyond memory-sensitive
			// caches
			if (hashMap.get(seed).get() == null) {
				deletecounter++;
				logger.info("Seed " + seed + " not found. Total lists deleted:" + deletecounter);
				//logger.info("Creating new list.");
				hashMap.put(seed,
						new SoftReference<>(DataGenerator.GenerateList(seed, minValue, maxValue), referenceQueue));
			}

			@SuppressWarnings("rawtypes")
			ArrayList arrayList = hashMap.get(seed).get();
			return (arrayList == null) ? null : arrayList;
		}
	}

}
