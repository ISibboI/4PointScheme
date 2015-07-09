package geometry.scheme;

import java.util.Arrays;
import java.util.Iterator;

import geometry.AbstractCurve;
import geometry.Curve;
import geometry.Point;
import geometry.PointSelector;

public class DefaultCurve extends AbstractCurve {
	private final Point[] _points;

	public DefaultCurve(final Point[] points) {
		_points = Arrays.copyOf(points, points.length);
	}

	public DefaultCurve(final int size) {
		_points = new Point[size];
	}

	public DefaultCurve(final DefaultCurve defaultCurve) {
		this(defaultCurve._points);
	}

	@Override
	public Iterator<Point> iterator() {
		return new Iterator<Point>() {
			int current = 0;

			@Override
			public boolean hasNext() {
				return current < _points.length;
			}

			@Override
			public Point next() {
				return _points[current++];
			}
		};
	}

	@Override
	public DefaultCurve clone() {
		return new DefaultCurve(getPoints());
	}

	@Override
	public int size() {
		return _points.length;
	}

	@Override
	public Point getPoint(int i) {
		return _points[i];
	}

	@Override
	public void setPoint(int i, Point point) {
		_points[i] = point;
	}

	@Override
	public void printProperties() {
		// Do nothing.
	}

	@Override
	public Curve createSubcurve(int offset, int length) {
		DefaultCurve subcurve = new DefaultCurve(length);

		for (int i = 0; i < length; i++) {
			subcurve.setPoint(i, getPoint(i + offset));
		}

		return subcurve;
	}

	protected Point[] getPoints() {
		return _points;
	}

}
