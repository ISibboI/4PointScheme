package geometry.scheme;
import geometry.Line;
import geometry.Point;


public class DefaultTangentChooser implements TangentChooser {

	@Override
	public Line chooseTangent(Point a, Point b, Point c) {
		Point ac = c.sub(a);
		return new Line(b, b.add(ac));
	}

	@Override
	public Line chooseFirstTangent(Point b, Point c) {
		return new Line(b, c).orthogonal();
	}

	@Override
	public Line chooseLastTangent(Point a, Point b) {
		return new Line(b, a).orthogonal();
	}

}
