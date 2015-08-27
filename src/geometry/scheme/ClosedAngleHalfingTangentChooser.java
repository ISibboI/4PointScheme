package geometry.scheme;

import geometry.Line;
import geometry.Point;

public class ClosedAngleHalfingTangentChooser extends ClosedTangentChooser {
	@Override
	public Line chooseTangent(Point a, Point b, Point c) {
		Point ab = b.sub(a).normalize();
		Point bc = c.sub(b).normalize();
		
		return new Line(b, b.add(ab.add(bc)));
	}
}