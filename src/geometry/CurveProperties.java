package geometry;

import geometry.scheme.fourpoint.TangentCurve;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Iterator;

import org.nevec.rjm.BigDecimalMath;

public final class CurveProperties {
	private static final MathContext MATH_CONTEXT = new MathContext(100, RoundingMode.HALF_UP);
	
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

		if (angle > Math.PI) {
			angle -= 2 * Math.PI;
		}

		if (angle > Math.PI || angle < -Math.PI) {
			throw new RuntimeException("Calculated wrong angle: " + angle);
		}

		return angle;
	}
	
	public static BigDecimal getExactAngle(final Curve curve, final int index) {
		Point a = curve.getPoint(index - 1);
		Point b = curve.getPoint(index);
		Point c = curve.getPoint(index + 1);

		BigDecimal bx = BigDecimal.valueOf(b.getX());
		BigDecimal by = BigDecimal.valueOf(b.getY());
		
		BigDecimal abx = bx.subtract(BigDecimal.valueOf(a.getX()));
		BigDecimal aby = by.subtract(BigDecimal.valueOf(a.getY()));
		BigDecimal bcx = BigDecimal.valueOf(c.getX()).subtract(bx);
		BigDecimal bcy = BigDecimal.valueOf(c.getY()).subtract(by);
		
		BigDecimal dot = abx.multiply(bcx).add(aby.multiply(bcy));
		dot = dot.divide(BigDecimalMath.root(2, abx.multiply(abx).add(aby.multiply(aby))), MATH_CONTEXT);
		dot = dot.divide(BigDecimalMath.root(2, bcx.multiply(bcx).add(bcy.multiply(bcy))), MATH_CONTEXT);
		
		if (dot.doubleValue() == 1) {
			return BigDecimal.ZERO;
		}
		
		return BigDecimalMath.acos(dot);
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
		BigDecimal lastAngle = getExactAngle(curve, 1);
		BigDecimal maxRatio = BigDecimal.ONE;

		for (int i = 2; i < curve.size() - 1; i++) {
			BigDecimal currentAngle = getExactAngle(curve, i);
			
			if (currentAngle.equals(BigDecimal.ZERO)) {
				System.out.println("Angle error too large");
				currentAngle = BigDecimal.ONE.divide(BigDecimal.valueOf(1024 * 1024));
			}
			
			BigDecimal ratio = currentAngle.divide(lastAngle, MATH_CONTEXT);

			if (ratio.compareTo(BigDecimal.ONE) < 0) {
				ratio = BigDecimal.ONE.divide(ratio, MATH_CONTEXT);
			}

			if (ratio.compareTo(maxRatio) > 0) {
				maxRatio = ratio;
			}

			lastAngle = currentAngle;
		}

		return maxRatio.doubleValue();
	}

	public static TangentCurve dualize(final TangentCurve curve) {
		final TangentCurve dualizedCurve = new TangentCurve(curve.size(), curve.getTensionParameter(),
				curve.getDisplacementParameter(), curve.getTangentChooser());

		for (int i = 0; i < curve.size(); i++) {
			Line tangent = curve.getTangent(i);
			Point point = curve.getPoint(i);

			dualizedCurve.setPoint(i, dualize(tangent));
			dualizedCurve.setTangent(i, dualize(point));
		}

		return dualizedCurve;
	}

	private static Point dualize(final Line tangent) {
		double a = tangent.getStart().getX();
		double b = tangent.getStart().getY();
		double c = tangent.getEnd().getX();
		double d = tangent.getEnd().getY();

		int count = 0;
		while ((b == 0 || d == 0 || a * d / b - c == 0) && count < 10) {
			Point direction = tangent.getDirection();
			a -= direction.getX();
			b -= direction.getY();
			c += direction.getX();
			d += direction.getY();

			count++;
		}

		double u = (1 - d / b) / (a * d / b - c);
		double v = -(u * c + 1) / d;

		if (Double.isInfinite(u) || Double.isInfinite(v) || Double.isNaN(u) || Double.isNaN(v)) {
			throw new RuntimeException("Cannot dualize tangent");
		}

		return new Point(u, v);
	}

	private static Line dualize(final Point point) {
		final double a = point.getX();
		final double b = point.getY();

		if (a == 0 && b == 0) {
			throw new RuntimeException("Cannot dualize point");
		}

		if (a == 0) {
			return new Line(new Point(-1, 1 / b), new Point(1, 1 / b));
		}

		if (b == 0) {
			return new Line(new Point(1 / a, -1), new Point(1 / a, 1));
		}

		return new Line(new Point(1 / a, -2 / b), new Point(-2 / a, 1 / b));
	}

	public static Point findCenterPoint(final Curve curve) {
		Point center = new Point(0, 0);

		for (Point p : curve) {
			center = center.add(p);
		}

		return center.div(curve.size());
	}
}
