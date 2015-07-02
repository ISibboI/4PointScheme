package geometry.scheme;

import geometry.Curve;
import geometry.Line;
import geometry.Point;
import geometry.PointSelector;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.util.Arrays;

public class TangentCurve extends DefaultCurve {
	private final Line[] _tangents;
	private final double _displacementParameter;
	private final TangentChooser _tangentChooser;

	public TangentCurve(int size, final double tensionParameter,
			final double displacementParameter,
			final TangentChooser tangentChooser) {
		super(size, tensionParameter);

		_tangents = new Line[size];
		_displacementParameter = displacementParameter;
		_tangentChooser = tangentChooser;
	}

	public TangentCurve(final Point[] points, final double tensionParameter,
			final double displacementParameter,
			final TangentChooser tangentChooser) {
		super(points, tensionParameter);

		_tangents = new Line[points.length];
		_displacementParameter = displacementParameter;
		_tangentChooser = tangentChooser;

		chooseAllTangents();
	}

	public TangentCurve(final TangentCurve tangentCurve) {
		super(tangentCurve);

		_tangents = Arrays.copyOf(tangentCurve._tangents,
				tangentCurve._tangents.length);
		_displacementParameter = tangentCurve._displacementParameter;
		_tangentChooser = tangentCurve._tangentChooser;
	}

	public TangentCurve(DefaultCurve defaultCurve,
			final double displacementParameter,
			final TangentChooser tangentChooser) {
		super(defaultCurve);

		_tangents = new Line[defaultCurve.size()];
		_displacementParameter = displacementParameter;
		_tangentChooser = tangentChooser;

		chooseAllTangents();
	}

	protected void chooseAllTangents() {
		_tangents[0] = _tangentChooser.chooseFirstTangent(getPoint(size() - 2),
				getPoint(0), getPoint(1));
		_tangents[size() - 1] = _tangentChooser.chooseLastTangent(
				getPoint(size() - 2), getPoint(size() - 1), getPoint(1));

		for (int i = 1; i < _tangents.length - 1; i++) {
			chooseTangent(i);
		}
	}

	protected void chooseAllNewTangents() {
		for (int i = 1; i < _tangents.length - 1; i += 2) {
			chooseTangent(i);
		}
	}

	protected void chooseTangent(int i) {
		_tangents[i] = _tangentChooser.chooseTangent(getPoint(i - 1),
				getPoint(i), getPoint(i + 1));
	}

	@Override
	public TangentCurve clone() {
		return new TangentCurve(this);
	}

	public Line getTangent(int i) {
		return _tangents[i];
	}

	protected void setTangent(int i, Line tangent) {
		_tangents[i] = tangent;
	}

	protected Point getNewPoint(PointSelector selector) {
		Point a = selector.getA(this);
		Point b = selector.getB(this);
		Point c = selector.getC(this);
		Point d = selector.getD(this);
		Line tb = getTangent(selector.getIndex());
		Line tc = getTangent(selector.getIndex() + 1);

		Point ab = b.sub(a);
		Point dc = c.sub(d);
		Point displacementVector = ab.add(dc);
		Point center = b.add(c).mul(0.5);

		Line displacementLine = new Line(center, center.add(displacementVector));

		double tbCutDistance = displacementLine.cut(tb);
		double tcCutDistance = displacementLine.cut(tc);

		if (tbCutDistance <= 0) {
			tbCutDistance = getTensionParameter() / _displacementParameter;
		}

		if (tcCutDistance <= 0) {
			tcCutDistance = getTensionParameter() / _displacementParameter;
		}

		if (!Double.isFinite(tbCutDistance) || !Double.isFinite(tcCutDistance)) {
			throw new RuntimeException("Error cutting lines: " + tbCutDistance
					+ ", " + tcCutDistance);
		}

		double minCutDistance = Math.min(tbCutDistance, tcCutDistance);
		double displacementSize = _displacementParameter * minCutDistance;
		double convergingDisplacementSize = Math.min(getTensionParameter(),
				displacementSize);

		return center.add(displacementVector.mul(convergingDisplacementSize));
	}

	@Override
	public Curve subdivide(PointSelector pointSelector, int step) {
		_tangentChooser.setStep(step);
		TangentCurve result = doubleSize();

		copyPointsToSubdivided(result);

		for (int i = 1; i < result.size() - 1; i += 2) {
			pointSelector.setIndex(i / 2);
			result.setPoint(i, getNewPoint(pointSelector));
		}

		for (int i = 0; i < result.size(); i += 2) {
			result.setTangent(i, getTangent(i / 2));
		}

		result.chooseAllNewTangents();
		result.setTangent(0, getTangent(0));
		result.setTangent(result.size() - 1, getTangent(size() - 1));

		return result;
	}

	@Override
	public Curve subdivide(final PointSelector pointSelector, final int step,
			final int index) {
		_tangentChooser.setStep(step);
		TangentCurve result = incrementSize();

		for (int i = 0; i <= index; i++) {
			result.setPoint(i, getPoint(i));
			result.setTangent(i, getTangent(i));
		}

		pointSelector.setIndex(index);
		result.setPoint(index + 1, getNewPoint(pointSelector));

		for (int i = index + 2; i < result.size(); i++) {
			result.setPoint(i, getPoint(i - 1));
			result.setTangent(i, getTangent(i - 1));
		}

		result.chooseTangent(index + 1);

		return result;
	}

	/**
	 * Calculates the angle fraction that has to be limited for theorem 6.7 to
	 * work.
	 */
	public double getMinimumTangentAngleFraction() {
		double minAngleFraction = Double.POSITIVE_INFINITY;

		for (int i = 2; i < size() - 2; i += 2) {
			Point a = getPoint(i - 2);
			Point b = getPoint(i - 1);
			Point c = getPoint(i);
			Point d = getPoint(i + 1);
			Point e = getPoint(i + 2);
			Line tangent = getTangent(i);

			double preFracAngle = new Line(c, b).angle(tangent);
			double preAngle = new Line(c, a).angle(tangent);
			double succFracAngle = new Line(c, d).angle(tangent);
			double succAngle = new Line(c, e).angle(tangent);

			if (preFracAngle > preAngle) {
				preFracAngle = Math.PI - preFracAngle;
				preAngle = Math.PI - preAngle;
			}

			if (succFracAngle > succAngle) {
				succFracAngle = Math.PI - succFracAngle;
				succAngle = Math.PI - succAngle;
			}

			if (preFracAngle / preAngle < minAngleFraction) {
				minAngleFraction = preFracAngle / preAngle;
			}

			if (1 - preFracAngle / preAngle < minAngleFraction) {
				minAngleFraction = 1 - preFracAngle / preAngle;
			}

			if (succFracAngle / succAngle < minAngleFraction) {
				minAngleFraction = succFracAngle / succAngle;
			}

			if (1 - succFracAngle / succAngle < minAngleFraction) {
				minAngleFraction = 1 - succFracAngle / succAngle;
			}

			if (minAngleFraction < 0) {
				throw new RuntimeException(
						"Error calculating tangent angle fractions.");
			}
		}

		return minAngleFraction;
	}

	@Override
	public void printProperties() {
		super.printProperties();

		System.out.println("Displacement: " + _displacementParameter);
	}

	@Override
	public void draw(Graphics2D g, double xScale, double yScale) {
		super.draw(g, xScale, yScale);

		Point scale = new Point(xScale, yScale);
		Path2D.Double path = new Path2D.Double();
		final double length = 10;

		for (int i = 0; i < size(); i++) {
			Line tangent = getTangent(i);
			Point start = tangent
					.getStart()
					.mul(scale)
					.sub(tangent.getDirection().mul(scale)
							.mul(length / tangent.length()));
			Point end = tangent
					.getStart()
					.mul(scale)
					.add(tangent.getDirection().mul(scale)
							.mul(length / tangent.length()));

			path.moveTo(start.getX(), start.getY());
			path.lineTo(end.getX(), end.getY());
		}

		final Stroke stroke = g.getStroke();

		g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND));
		g.draw(path);

		g.setStroke(stroke);
	}

	@Override
	public Curve createSubcurve(int offset, int length) {
		TangentCurve subcurve = new TangentCurve(length, getTensionParameter(),
				_displacementParameter, _tangentChooser);

		for (int i = 0; i < length; i++) {
			subcurve.setPoint(i, getPoint(i + offset));
			subcurve.setTangent(i, getTangent(i + offset));
		}

		return subcurve;
	}

	protected TangentCurve doubleSize() {
		return new TangentCurve(size() * 2 - 1, getTensionParameter(),
				_displacementParameter, _tangentChooser);
	}

	protected TangentCurve incrementSize() {
		return new TangentCurve(size() + 1, getTensionParameter(),
				_displacementParameter, _tangentChooser);
	}

	public double getDisplacementParameter() {
		return _displacementParameter;
	}

	public TangentChooser getTangentChooser() {
		return _tangentChooser;
	}
}