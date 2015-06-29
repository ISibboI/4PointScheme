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
	
	public static double getAngle(final Curve curve, final int index) {
		return getAngle(curve, index, false);
	}

	public static double getAngle(final Curve curve, final int index, final boolean verbose) {
		Point a = curve.getPoint(index - 1);
		Point b = curve.getPoint(index);
		Point c = curve.getPoint(index + 1);

		Point ab = b.sub(a);
		Point bc = c.sub(b);

		double angleAB = Math.atan2(ab.getY(), ab.getX());
		double angleBC = Math.atan2(bc.getY(), bc.getX());
		
		double angle = angleBC - angleAB;
		
		if (verbose) {
			System.out.println("AngleAB: " + angleAB);
			System.out.println("AngleBC: " + angleBC);
		}
		
		if (angle < -Math.PI) {
			angle += 2 * Math.PI;
		}
		
		if (angle > Math.PI || angle < -Math.PI) {
			throw new RuntimeException("Calculated wrong angle: " + angle);
		}
		
		return angle;
	}

	public static boolean isConvex(final Curve curve) {
		double sign = getAngle(curve, 1);

		for (int i = 2; i < curve.size() - 1; i++) {
			if (sign * getAngle(curve, i) < 0) {
				return false;
			}
		}

		return true;
	}
	
	public static int getUnconvexity(final Curve curve) {
		double sign = getAngle(curve, 1);

		for (int i = 2; i < curve.size() - 1; i++) {
			if (sign * getAngle(curve, i) < 0) {
				return i;
			}
		}

		return -1;
	}
	
	public static double maxAngleRatio(final Curve curve) {
		double lastAngle = getAngle(curve, 1);
		double maxRatio = 1;
		
		for (int i = 2; i < curve.size() - 1; i++) {
			double currentAngle = getAngle(curve, i);
			double ratio = currentAngle / lastAngle;
			
			if (ratio < 1) {
				ratio = 1 / ratio;
			}
			
			if (ratio > maxRatio) {
				maxRatio = ratio;
			}
			
			lastAngle = currentAngle;
		}
		
		return maxRatio;
	}
}
