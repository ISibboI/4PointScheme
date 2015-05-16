package geometry;

public class Line {
	private final Point _start;
	private final Point _end;
	
	public Line(final Point start, final Point end) {
		_start = start;
		_end = end;
	}

	public Point getStart() {
		return _start;
	}

	public Point getEnd() {
		return _end;
	}

	public double cut(Line other) {
		Point a = _start;
		Point b = _end;
		Point c = other.getStart();
		Point d = other.getEnd();
		
		double over = (d.getY() - c.getY()) * (a.getX() - d.getX()) - (a.getY() - d.getY()) * (d.getX() - c.getX());
		double under = (b.getY() - a.getY()) * (d.getX() - c.getX()) - (d.getY() - c.getY()) * (b.getX() - a.getX());
		return under == 0 ? 0 : over / under;
	}

	public Line orthogonal() {
		Point vector = _end.sub(_start);
		return new Line(_start, _start.add(vector.orthogonal()));
	}
}