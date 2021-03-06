package geometry.scheme;
import geometry.Line;
import geometry.Point;


public class SelectableEndTangentChooser implements TangentChooser {
	private final Point _startDirection;
	private final Point _endDirection;
	
	public SelectableEndTangentChooser(final Point startDirection, final Point endDirection) {
		_startDirection = startDirection;
		_endDirection = endDirection;
	}
	
	@Override
	public Line chooseTangent(Point a, Point b, Point c) {
		Point ac = c.sub(a);
		return new Line(b, b.add(ac));
	}

	@Override
	public Line chooseFirstTangent(Point a, Point b, Point c) {
		return new Line(b, b.add(_startDirection)).orthogonal();
	}

	@Override
	public Line chooseLastTangent(Point a, Point b, Point c) {
		return new Line(b, b.add(_endDirection)).orthogonal();
	}

	@Override
	public void setStep(int step) {
		// Ignore
	}
}