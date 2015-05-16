package geometry.scheme;

import geometry.AbstractCurve;
import geometry.Curve;
import geometry.Point;

import java.util.Arrays;
import java.util.Iterator;


public class DefaultCurve extends AbstractCurve {
	private final Point[] _points;
	private final double _tensionParameter;
	
	public DefaultCurve(final Point[] points, final double tensionParameter) {
		_points = Arrays.copyOf(points, points.length);
		_tensionParameter = tensionParameter;
	}
	
	public DefaultCurve(int size, final double tensionParameter) {
		_points = new Point[size];
		_tensionParameter = tensionParameter;
	}

	public DefaultCurve(DefaultCurve defaultCurve) {
		this(defaultCurve._points, defaultCurve._tensionParameter);
	}

	@Override
	public int size() {
		return _points.length;
	}

	@Override
	public DefaultCurve clone() {
		return new DefaultCurve(_points, _tensionParameter);
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
	
	protected void copyPointsToSubdivided(DefaultCurve subdivided) {
		if (subdivided.size() != size() * 2 - 1) {
			throw new IllegalArgumentException("Subdivided curve has wrong length.");
		}
		
		for (int i = 0; i < subdivided.size(); i += 2) {
			subdivided.setPoint(i, getPoint(i / 2));
		}
	}

	@Override
	public Curve subdivide(PointSelector pointSelector) {
		DefaultCurve result = new DefaultCurve(size() * 2 - 1, _tensionParameter);
		
		copyPointsToSubdivided(result);
		
		for (int i = 1; i < result.size() - 1; i += 2) {
			pointSelector.setIndex(i / 2);
			result.setPoint(i, getNewPoint(pointSelector));
		}
		
		return result;
	}
	
	public double getTensionParameter() {
		return _tensionParameter;
	}
}