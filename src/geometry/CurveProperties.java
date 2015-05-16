package geometry;

import java.util.Iterator;

public final class CurveProperties {
	private CurveProperties(){}
	
	public static double maxEdgeLengthRatio(final Curve curve) {
		double maxRatio = 1;
		
		Iterator<Point> points = curve.iterator();
		Point preLast = points.next();
		Point last = points.next();
		
		for (Point current = points.next(); points.hasNext(); preLast = last, last = current, current = points.next()) {
			double ratio = preLast.distanceTo(last) / last.distanceTo(current);
			
			if (ratio < 1) {
				ratio = 1 / ratio;
			}
			
			if (ratio > maxRatio) {
				maxRatio = ratio;
			}
		}
		
		return maxRatio;
	}
}
