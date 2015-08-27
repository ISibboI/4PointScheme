package geometry.scheme.fourpoint;

import geometry.Point;
import geometry.PointSelector;
import geometry.scheme.DefaultCurve;

public class FourPointCurve extends DefaultCurve {
	private final double _tensionParameter;

	public FourPointCurve(final Point[] points, final double tensionParameter) {
		super(points);
		_tensionParameter = tensionParameter;
	}

	public FourPointCurve(int size, final double tensionParameter) {
		super(size);
		_tensionParameter = tensionParameter;
	}

	public FourPointCurve(FourPointCurve fourPointCurve) {
		super(fourPointCurve);
		_tensionParameter = fourPointCurve.getTensionParameter();
	}

	@Override
	public FourPointCurve clone() {
		return new FourPointCurve(getPoints(), _tensionParameter);
	}

	private Point getNewPoint(final PointSelector selector) {
		Point a = selector.getA(this);
		Point b = selector.getB(this);
		Point c = selector.getC(this);
		Point d = selector.getD(this);

		Point ab = b.sub(a);
		Point dc = c.sub(d);
		Point displacement = ab.add(dc).mul(_tensionParameter);
		Point center = b.add(c).mul(0.5);

		return center.add(displacement);
	}

	protected void copyPointsToSubdivided(FourPointCurve subdivided) {
		if (subdivided.size() != size() * 2 - 1) {
			throw new IllegalArgumentException(
					"Subdivided curve has wrong length.");
		}

		for (int i = 0; i < subdivided.size(); i += 2) {
			subdivided.setPoint(i, getPoint(i / 2));
		}
	}

	public FourPointCurve subdivide(PointSelector pointSelector, int step) {
		FourPointCurve result = new FourPointCurve(size() * 2 - 1,
				_tensionParameter);

		copyPointsToSubdivided(result);

		for (int i = 1; i < result.size() - 1; i += 2) {
			pointSelector.setIndex(i / 2);
			result.setPoint(i, getNewPoint(pointSelector));
		}

		return result;
	}

	public FourPointCurve subdivide(PointSelector pointSelector, int step,
			int index) {
		FourPointCurve result = new FourPointCurve(size() + 1,
				_tensionParameter);

		for (int i = 0; i <= index; i++) {
			result.setPoint(i, getPoint(i));
		}

		pointSelector.setIndex(index);
		result.setPoint(index + 1, getNewPoint(pointSelector));

		for (int i = index + 2; i < result.size(); i++) {
			result.setPoint(i, getPoint(i - 1));
		}

		return result;
	}

	public double getTensionParameter() {
		return _tensionParameter;
	}

	@Override
	public void printProperties() {
		System.out.println("Tension: " + _tensionParameter);
	}

	@Override
	public FourPointCurve createSubcurve(int offset, int length) {
		FourPointCurve subcurve = new FourPointCurve(length,
				getTensionParameter());

		for (int i = 0; i < length; i++) {
			subcurve.setPoint(i, getPoint(i + offset));
		}

		return subcurve;
	}
}