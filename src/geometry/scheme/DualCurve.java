package geometry.scheme;

import java.util.Arrays;
import java.util.Iterator;

import geometry.AbstractCurve;
import geometry.Curve;
import geometry.Line;
import geometry.Point;
import geometry.PointSelector;

public class DualCurve extends AbstractCurve {
	private final Point[] _points;
	
	public DualCurve(final int length) {
		_points = new Point[length];
	}

	public DualCurve(final DualCurve dualCurve) {
		_points = Arrays.copyOf(dualCurve._points, dualCurve.size());
	}
	
	public DualCurve(final TangentCurve tangentCurve) {
		_points = new Point[tangentCurve.size()];
		Point centerTransformation = findCenterPoint(tangentCurve).mul(-1);
		
		for (int i = 0; i < tangentCurve.size(); i++) {
			Line tangent = tangentCurve.getTangent(i);
			tangent = tangent.translate(centerTransformation);
			
			_points[i] = dualize(tangent);
		}
	}

	private Point dualize(final Line tangent) {
		double a = tangent.getStart().getX();
		double b = tangent.getStart().getY();
		double c = tangent.getEnd().getX();
		double d = tangent.getEnd().getY();
		
		double u = (1 - d / b) / (a * d / b - c);
		double v = -(u * c + 1) / d;
		
		return new Point(u, v);
	}

	private Point findCenterPoint(final Curve curve) {
		Point center = new Point(0, 0);
		
		for (Point p : curve) {
			center = center.add(p);
		}
		
		return center.div(curve.size());
	}

	@Override
	public int size() {
		return _points.length;
	}

	@Override
	public Point getPoint(final int i) {
		return _points[i];
	}

	@Override
	public void setPoint(final int i, final Point point) {
		_points[i] = point;
	}

	@Override
	public Curve subdivide(final PointSelector pointSelector, final int step) {
		// TODO Automatisch generierter Methodenstub
		return null;
	}

	@Override
	public Curve subdivide(PointSelector pointSelector, int step, int index) {
		throw new UnsupportedOperationException("Cannot subdivide a DualCurve");
	}

	@Override
	public void printProperties() {
		// Do nothing.
	}

	@Override
	public Curve createSubcurve(int offset, int length) {
		DualCurve subcurve = new DualCurve(length);

		for (int i = 0; i < length; i++) {
			subcurve.setPoint(i, getPoint(i + offset));
		}

		return subcurve;
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
	public DualCurve clone() {
		return new DualCurve(this);
	}

}
