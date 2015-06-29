package geometry;

public class Point {
	private final double _x;
	private final double _y;

	public Point(final double x, final double y) {
		_x = x;
		_y = y;
	}

	public Point add(final Point other) {
		return new Point(_x + other._x, _y + other._y);
	}

	public Point sub(Point other) {
		return new Point(_x - other._x, _y - other._y);
	}

	public Point mul(double d) {
		return new Point(_x * d, _y * d);
	}

	public Point div(double d) {
		return new Point(_x / d, _y / d);
	}

	public double getX() {
		return _x;
	}

	public double getY() {
		return _y;
	}

	public Point mul(Point other) {
		return new Point(_x * other._x, _y * other._y);
	}

	public double distanceTo(Point other) {
		return sub(other).distanceToOrigin();
	}

	public double squaredDistanceTo(Point other) {
		return sub(other).squaredDistanceToOrigin();
	}

	public double distanceToOrigin() {
		return Math.sqrt(_x * _x + _y * _y);
	}

	public double squaredDistanceToOrigin() {
		return _x * _x + _y * _y;
	}

	public String toString() {
		return "[" + _x + ", " + _y + "]";
	}

	public Point orthogonal() {
		return new Point(-_y, _x);
	}

	public double scalarProduct(Point other) {
		return _x * other._x + _y * other._y;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Point) {
			Point p = (Point)o;
			
			return p._x == _x && p._y == _y;
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return Double.hashCode(_x) ^ ~Double.hashCode(_y);
	}

	public Point normalize() {
		return div(distanceToOrigin());
	}
}