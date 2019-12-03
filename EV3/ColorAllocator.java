
public class ColorAllocator {

		//Fully Calibrated !!! 
		private static double[] black = {0.021435995, 0.027944634, 0.028183777};
		private static double[] white = {0.247722, 0.30802986, 0.26647097};
		private static double[] red = {0.21750888, 0.03697843, 0.047216922};
		private static double[] blue = {0.045246534, 0.22411129, 0.20622523};
		private static double[] green = {0.07612464, 0.22847155, 0.104868546};
		private static double[] yellow = {0.24536116, 0.18231525, 0.06914638};
		private static double[] burgundy = {0.06835903, 0.033195656, 0.045375083};
		
		public ColorAllocator() {
			
		}
		
		public double difference(float[] point, double[] ref) {
			return Math.sqrt(Math.pow(point[0] - ref[0], 2) + Math.pow(point[1] - ref[1], 2) + Math.pow(point[2] - ref[2], 2));
		}
		
		public Color findClosest(float[] col) {
			Color minCol = Color.BLACK;
			double minimum_dist = difference(col, black);
			double distance = minimum_dist;
			
			distance = difference(col, white);
			if (distance < minimum_dist) {
				minCol = Color.WHITE;
				minimum_dist = distance;
			}
			
			distance = difference(col, red);
			if (distance < minimum_dist) {
				minCol = Color.RED;
				minimum_dist = distance;
			}
			
			distance = difference(col, blue);
			if (distance < minimum_dist) {
				minCol = Color.BLUE;
				minimum_dist = distance;
			}
			
			distance = difference(col, green);
			if (distance < minimum_dist) {
				minCol = Color.GREEN;
				minimum_dist = distance;
			}
			
			distance = difference(col, yellow);
			if (distance < minimum_dist) {
				minCol = Color.YELLOW;
				minimum_dist = distance;
			}
			
			distance = difference(col, burgundy);
			if (distance < minimum_dist) {
				minCol = Color.BURGUNDY;
				minimum_dist = distance;
			}
			
			
			return minCol;
		}
	}

	enum Color {
		BLACK, WHITE, RED, BLUE, GREEN, YELLOW, BURGUNDY
	}
