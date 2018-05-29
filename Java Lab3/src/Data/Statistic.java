package Data;

import java.util.ArrayList;

public class Statistic {
	
	public static double Size(@SuppressWarnings("rawtypes") ArrayList list) {
		return list.size();
	}
	
	public static double Sum(@SuppressWarnings("rawtypes") ArrayList list) {
		double sum = 0;

		for (int i = 0; i < list.size(); i++) {
			sum += (double) list.get(i);
		}

		return sum;
	}
	
	public static double Average(@SuppressWarnings("rawtypes") ArrayList list) {
		double sum = 0;

		for (int i = 0; i < list.size(); i++) {
			sum += (double) list.get(i);
		}

		return sum / list.size();
	}

}
