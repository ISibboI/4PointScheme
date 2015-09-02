package geometry.scheme;

import geometry.Line;
import geometry.Point;

public class CircleTangentChooser implements TangentChooser {
	@Override
	public Line chooseTangent(Point a, Point b, Point c) {
		Line l1 = new Line(a.add(b).div(2), b).orthogonal();
		Line l2 = new Line(c.add(b).div(2), b).orthogonal();
		double cutDistance = l1.cut(l2);
		
		if (cutDistance <= 0) {
			throw new RuntimeException("Could not construct circle.");
		}
		
		Point center = l1.getStart().add(l1.getDirection().mul(cutDistance));
		return new Line(b, center).orthogonal();
	}

	@Override
	public Line chooseFirstTangent(Point a, Point b, Point c) {
		return new Line(b, c).orthogonal();
	}

	@Override
	public Line chooseLastTangent(Point a, Point b, Point c) {
		return new Line(b, a).orthogonal();
	}

	@Override
	public void setStep(int step) {
		// Ignore.
	}
}