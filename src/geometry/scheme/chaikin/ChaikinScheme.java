package geometry.scheme.chaikin;

import geometry.Curve;
import geometry.Point;
import geometry.scheme.DefaultCurve;
import geometry.scheme.SubdivisionScheme;

public class ChaikinScheme implements SubdivisionScheme {
	private final Curve _startingCurve;
	private final int _iterations;
	private final boolean _isClosed;
	private Curve _result;

	public ChaikinScheme(final Curve curve, final int iterations,
			final boolean isClosed) {
		_startingCurve = curve;
		_iterations = iterations;
		_isClosed = isClosed;
	}

	@Override
	public Curve getResult() {
		return _result;
	}

	@Override
	public void evaluate() {
		Curve currentCurve = _startingCurve;

		for (int i = 0; i < _iterations; i++) {
			currentCurve = chaikin(currentCurve);
		}

		_result = currentCurve;
	}

	private Curve chaikin(final Curve curve) {
		Curve result;

		if (_isClosed) {
			result = new DefaultCurve(2 * curve.size());
			result.setPoint(0,
					curve.getPoint(0).mul(.75).add(curve.getPoint(1).mul(0.25)));
			result.setPoint(result.size() - 1, result.getPoint(0));
			result.setPoint(result.size() - 2, curve.getPoint(curve.size() - 1)
					.mul(.75).add(curve.getPoint(curve.size() - 2).mul(.25)));
		} else {
			result = new DefaultCurve(2 * curve.size() - 2);
			result.setPoint(0, curve.getPoint(0));
			result.setPoint(result.size() - 1, curve.getPoint(curve.size() - 1));
		}

		for (int i = 1; i < result.size() - (_isClosed ? 2 : 1); i++) {
			Point smallSource = curve.getPoint(i / 2);
			Point bigSource = curve.getPoint(i / 2 + 1);

			if (i % 2 == 0) {
				Point tmp = smallSource;
				smallSource = bigSource;
				bigSource = tmp;
			}

			result.setPoint(i, bigSource.mul(.75).add(smallSource.mul(.25)));
		}

		return result;
	}
}