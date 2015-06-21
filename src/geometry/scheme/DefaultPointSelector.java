package geometry.scheme;
import geometry.Curve;
import geometry.Point;
import geometry.PointSelector;


public class DefaultPointSelector implements PointSelector {
	private int _index = -1;
	
	@Override
	public Point getA(Curve curve) {
		if (_index == 0) {
			return curve.getPoint(0);
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
			return curve.getPoint(curve.size() - 1);
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