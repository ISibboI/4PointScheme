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

		System.out.println("\n===================================================================================================\n");
		_startingCurve.printProperties();
		System.out.println();
		
		System.out.println("Starting maximum edge length ratio: " + CurveProperties.maxEdgeLengthRatio(_startingCurve));
		System.out.println("Starting maximum edge length after next ratio: " + CurveProperties.maxEdgeLengthAfterNextRatio(_startingCurve));
		System.out.println("Starting maximum edge length double ratio: " + CurveProperties.maxEdgeLengthDoubleRatio(_startingCurve));
		System.out.println("Starting maximum edge angle: " + CurveProperties.maxEdgeAngle(_startingCurve));

		for (int i = 0; i < _iterations; i++) {
			currentCurve = currentCurve.subdivide(_pointSelector, i);

			System.out.println("---------------------------------------------------------------------------------------------------");
			System.out.println("Maximum edge length ratio after " + (i + 1) + " iterations: "
					+ CurveProperties.maxEdgeLengthRatio(currentCurve));
			System.out.println("Maximum edge length after next ratio after " + (i + 1) + " iterations: "
					+ CurveProperties.maxEdgeLengthAfterNextRatio(currentCurve));
			System.out.println("Maximum edge length double ratio after " + (i + 1) + " iterations: "
					+ CurveProperties.maxEdgeLengthDoubleRatio(currentCurve));
			System.out.println("Maximum edge angle after " + (i + 1) + " iterations: "
					+ CurveProperties.maxEdgeAngle(currentCurve));

			if (currentCurve instanceof TangentCurve) {
				System.out.println("Minimum angle fraction after " + (i + 1) + " iterations: "
						+ ((TangentCurve) currentCurve).getMinimumTangentAngleFraction());
			}
		}

		_resultCurve = currentCurve;
	}

	@Override
	public Curve getResult() {
		return _resultCurve;
	}
}