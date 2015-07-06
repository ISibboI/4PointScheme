package geometry.scheme;

import geometry.Line;
import geometry.Point;

public class ClosedAngleHalfingTangentChooser extends ClosedTangentChooser {
	@Override
	public Line chooseTangent(Point a, Point b, Point c) {
		Point ab = b.sub(a).normalize();
		Point cb = b.sub(c).normalize();
		
		return new Line(b, b.add(ab.sub(cb)));
	}
}