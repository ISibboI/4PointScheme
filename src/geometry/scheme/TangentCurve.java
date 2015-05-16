package geometry.scheme;

import geometry.Curve;
import geometry.Line;
import geometry.Point;

import java.util.Arrays;

public class TangentCurve extends DefaultCurve {
	private final Line[] _tangents;
	private final double _displacementParameter;
	private final TangentChooser _tangentChooser;

	public TangentCurve(int size, final double tensionParameter, final double displacementParameter,
			final TangentChooser tangentChooser) {
		super(size, tensionParameter);

		_tangents = new Line[size];
		_displacementParameter = displacementParameter;
		_tangentChooser = tangentChooser;
	}

	public TangentCurve(final Point[] points, final double tensionParameter, final double displacementParameter,
			final TangentChooser tangentChooser) {
		super(points, tensionParameter);

		_tangents = new Line[points.length];
		_displacementParameter = displacementParameter;
		_tangentChooser = tangentChooser;

		chooseAllTangents();
	}

	public TangentCurve(final TangentCurve tangentCurve) {
		super(tangentCurve);

		_tangents = Arrays.copyOf(tangentCurve._tangents, tangentCurve._tangents.length);
		_displacementParameter = tangentCurve._displacementParameter;
		_tangentChooser = tangentCurve._tangentChooser;
	}

	public TangentCurve(DefaultCurve defaultCurve, final double displacementParameter,
			final TangentChooser tangentChooser) {
		super(defaultCurve);

		_tangents = new Line[defaultCurve.size()];
		_displacementParameter = displacementParameter;
		_tangentChooser = tangentChooser;

		chooseAllTangents();
	}

	private void chooseAllTangents() {
		_tangents[0] = _tangentChooser.chooseFirstTangent(getPoint(0), getPoint(1));
		_tangents[size() - 1] = _tangentChooser.chooseLastTangent(getPoint(size() - 2), getPoint(size() - 1));

		for (int i = 1; i < _tangents.length - 1; i++) {
			chooseTangent(i);
		}
	}

	private void chooseTangent(int i) {
		_tangents[i] = _tangentChooser.chooseTangent(getPoint(i - 1), getPoint(i), getPoint(i + 1));
	}

	@Override
	public TangentCurve clone() {
		return new TangentCurve(this);
	}

	public Line getTangent(int i) {
		return _tangents[i];
	}

	private void setTangent(int i, Line tangent) {
		_tangents[i] = tangent;
	}

	protected void copyTangentsToSubdivided(TangentCurve subdivided) {
		if (subdivided.size() != size() * 2 - 1) {
			throw new IllegalArgumentException("Subdivided curve has wrong length.");
		}

		for (int i = 0; i < subdivided.size(); i += 2) {
			subdivided.setTangent(i, getTangent(i / 2));
		}
	}

	private Point getNewPoint(PointSelector selector) {
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
		
		if (!Double.isFinite(tbCutDistance)
				|| !Double.isFinite(tcCutDistance)) {
			throw new RuntimeException("Error cutting lines: " + tbCutDistance + ", " + tcCutDistance);
		}

		double minCutDistance = Math.min(tbCutDistance, tcCutDistance);
		double displacementSize = _displacementParameter * minCutDistance;
		double convergingDisplacementSize = Math.min(getTensionParameter(), displacementSize);

		return center.add(displacementVector.mul(convergingDisplacementSize));
	}

	@Override
	public Curve subdivide(PointSelector pointSelector) {
		TangentCurve result = new TangentCurve(size() * 2 - 1, getTensionParameter(), _displacementParameter,
				_tangentChooser);

		copyPointsToSubdivided(result);
		copyTangentsToSubdivided(result);

		for (int i = 1; i < result.size() - 1; i += 2) {
			pointSelector.setIndex(i / 2);
			result.setPoint(i, getNewPoint(pointSelector));
			result.chooseTangent(i);
		}

		return result;
	}
}