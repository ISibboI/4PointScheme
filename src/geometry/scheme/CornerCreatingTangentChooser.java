package geometry.scheme;

import geometry.Line;
import geometry.Point;

public class CornerCreatingTangentChooser extends DefaultTangentChooser {
	private int _step;

	@Override
	public Line chooseTangent(Point a, Point b, Point c) {
		double angleFactor = 0.5;

		for (int i = 0; i < _step; i++) {
			angleFactor *= 0.5;
		}

		Point ab = b.sub(a);
		Point bc = c.sub(b);
		Point tangentVector = ab.mul(angleFactor).add(bc.mul(1 - angleFactor));
		
		return new Line(b, b.add(tangentVector));
	}

	@Override
	public void setStep(int step) {
		_step = step;
	}
}