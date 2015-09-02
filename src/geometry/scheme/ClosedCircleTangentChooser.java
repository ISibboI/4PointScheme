package geometry.scheme;

import geometry.Line;
import geometry.Point;

public class ClosedCircleTangentChooser extends CircleTangentChooser {
	@Override
	public Line chooseFirstTangent(Point a, Point b, Point c) {
		return chooseTangent(a, b, c);
	}

	@Override
	public Line chooseLastTangent(Point a, Point b, Point c) {
		return chooseTangent(a, b, c);
	}
}
