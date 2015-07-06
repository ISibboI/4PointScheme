package geometry.scheme;

import geometry.Line;
import geometry.Point;

public class ClosedTangentChooser implements TangentChooser {
	@Override
	public Line chooseTangent(Point a, Point b, Point c) {
		Point ac = c.sub(a);
		return new Line(b, b.add(ac));
	}

	@Override
	public Line chooseFirstTangent(Point a, Point b, Point c) {
		return chooseTangent(a, b, c);
	}

	@Override
	public Line chooseLastTangent(Point a, Point b, Point c) {
		return chooseTangent(a, b, c);
	}

	@Override
	public void setStep(int step) {
		// Ignore.
	}
}