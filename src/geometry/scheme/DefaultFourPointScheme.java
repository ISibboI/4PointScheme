package geometry.scheme;

import geometry.Curve;
import geometry.CurveProperties;

public class DefaultFourPointScheme implements FourPointScheme {
	private final Curve _startingCurve;
	private Curve _resultCurve;
	private final int _iterations;
	private final PointSelector _pointSelector;
	
	public DefaultFourPointScheme(final Curve startingPoints, final int iterations, final PointSelector pointSelector) {
		_startingCurve = startingPoints.clone();
		_iterations = iterations;
		_pointSelector = pointSelector;
	}
	
	@Override
	public void evaluate() {
		Curve currentCurve = _startingCurve;
		
		System.out.println("Starting maximum edge length ratio: " + CurveProperties.maxEdgeLengthRatio(_startingCurve));
		
		for (int i = 0; i < _iterations; i++) {
			currentCurve = currentCurve.subdivide(_pointSelector);

			System.out.println("Maximum edge length ratio after " + (i + 1) + " iterations: " + CurveProperties.maxEdgeLengthRatio(currentCurve));
		}
		
		_resultCurve = currentCurve;
	}

	@Override
	public Curve getResult() {
		return _resultCurve;
	}
}