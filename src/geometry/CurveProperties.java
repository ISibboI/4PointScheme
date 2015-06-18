package geometry;

import java.util.Iterator;

public final class CurveProperties {
	private CurveProperties() {
	}

	public static double maxEdgeLengthRatio(final Curve curve) {
		double maxRatio = 1;

		Iterator<Point> points = curve.iterator();
		Point preLast = points.next();
		Point last = points.next();

		for (Point current = points.next(); points.hasNext();) {
			preLast = last;
			last = current;
			current = points.next();
			
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

	public static double maxEdgeLengthDoubleRatio(final Curve curve) {
		double maxRatio = 1;

		Iterator<Point> points = curve.iterator();
		Point prePreLast = points.next();
		Point preLast = points.next();
		Point last = points.next();

		for (Point current = points.next(); points.hasNext();) {
			prePreLast = preLast;
			preLast = last;
			last = current;
			current = points.next();

			double currentRatio = preLast.distanceTo(last) / last.distanceTo(current);
			double lastRatio = prePreLast.distanceTo(preLast) / preLast.distanceTo(last);
			double ratio = lastRatio / currentRatio;

			if (ratio < 1) {
				ratio = 1 / ratio;
			}

			if (ratio > maxRatio) {
				maxRatio = ratio;
			}
		}

		return maxRatio;
	}

	public static double maxEdgeLengthAfterNextRatio(final Curve curve) {
		double maxRatio = 1;

		Iterator<Point> points = curve.iterator();
		Point prePreLast = points.next();
		Point preLast = points.next();
		Point last = points.next();

		for (Point current = points.next(); points.hasNext();) {
			prePreLast = preLast;
			preLast = last;
			last = current;
			current = points.next();
			
			double ratio = prePreLast.distanceTo(preLast) / last.distanceTo(current);

			if (ratio < 1) {
				ratio = 1 / ratio;
			}

			if (ratio > maxRatio) {
				maxRatio = ratio;
			}
		}

		return maxRatio;
	}
	
	public static double maxEdgeAngle(final Curve curve) {
		double maxAngle = 0;

		Iterator<Point> points = curve.iterator();
		Point preLast = points.next();
		Point last = points.next();

		for (Point current = points.next(); points.hasNext();) {
			preLast = last;
			last = current;
			current = points.next();
			
			Line lastLine = new Line(preLast, last);
			Line currentLine = new Line(last, current);
			
			double angle = lastLine.angle(currentLine);
			
			if (angle > maxAngle) {
				maxAngle = angle;
			}
		}

		return maxAngle;
	}
}
