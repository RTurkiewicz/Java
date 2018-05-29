package Data;

import java.util.ArrayList;
import java.util.Random;

public class DataGenerator {

	private static int minListSize = 10000;
	private static int maxListSize = 100000;

	public static ArrayList<Double> GenerateList(int seed, int minValue, int maxValue) {
		ArrayList<Double> list = new ArrayList<Double>();
		Random random = new Random(seed);

		int size = random.nextInt((maxListSize - minListSize) + 1) + minListSize;

		for (int i = 0; i < size; i++)
			list.add(minValue + (maxValue - minValue) * random.nextDouble());

		return list;
	}

}
