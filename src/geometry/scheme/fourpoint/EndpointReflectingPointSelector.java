package geometry.scheme.fourpoint;

import geometry.Curve;
import geometry.Line;
import geometry.Point;
import geometry.PointSelector;

public class EndpointReflectingPointSelector implements PointSelector {
	private int _index = -1;

	@Override
	public Point getA(Curve curve) {
		if (_index == 0) {
			if (!(curve instanceof TangentCurve)) {
				throw new RuntimeException("'curve' must be instance of TangentCurve!");
			}
			
			Line tangent = ((TangentCurve)curve).getTangent(0);
			Point BA = tangent.reflect(curve.getPoint(1).sub(curve.getPoint(0)));
			
			return curve.getPoint(0).add(BA);
		} else {
			return curve.getPoint(_index - 1);
		}
	}

	@Override
	public Point getB(Curve curve) {
		return curve.getPoint(_index);
	}

	@Override
	public Point getC(Curve curve) {
		return curve.getPoint(_index + 1);
	}

	@Override
	public Point getD(Curve curve) {
		if (_index == curve.size() - 2) {
			if (!(curve instanceof TangentCurve)) {
				throw new RuntimeException("'curve' must be instance of TangentCurve!");
			}
			
			Line tangent = ((TangentCurve)curve).getTangent(curve.size() - 1);
			Point CD = tangent.reflect(curve.getPoint(curve.size() - 2).sub(curve.getPoint(curve.size() - 1)));
			
			return curve.getPoint(curve.size() - 1).add(CD);
		} else {
			return curve.getPoint(_index + 2);
		}
	}

	@Override
	public void setIndex(int index) {
		_index = index;
	}

	@Override
	public int getIndex() {
		return _index;
	}
}