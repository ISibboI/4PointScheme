package geometry.scheme.fourpoint;

import geometry.Curve;
import geometry.CurveProperties;
import geometry.PointSelector;

public class DefaultFourPointScheme implements FourPointScheme {
	private final FourPointCurve _startingCurve;
	private FourPointCurve _resultCurve;
	private final int _iterations;
	private final PointSelector _pointSelector;
	private final SubdivisionStrategy _subdivisionStrategy;

	public DefaultFourPointScheme(final FourPointCurve startingPoints, final int iterations, final PointSelector pointSelector,
			final SubdivisionStrategy subdivisionStrategy) {
		_startingCurve = startingPoints.clone();
		_iterations = iterations;
		_pointSelector = pointSelector;
		_subdivisionStrategy = subdivisionStrategy;
	}

	protected void printPreEvaluation() {
		System.out
				.println("\n===================================================================================================\n");
		_startingCurve.printProperties();
		System.out.println();

//		System.out.println("Starting maximum edge length ratio: " + CurveProperties.maxEdgeLengthRatio(_startingCurve));
//		System.out.println("Starting maximum edge length after next ratio: "
//				+ CurveProperties.maxEdgeLengthAfterNextRatio(_startingCurve));
//		System.out.println("Starting maximum edge length double ratio: "
//				+ CurveProperties.maxEdgeLengthDoubleRatio(_startingCurve));
//		System.out.println("Starting maximum edge angle: " + CurveProperties.maxEdgeAngle(_startingCurve));
		System.out.println("Starting maximum angle ratio: " + CurveProperties.maxAngleRatio(_startingCurve));
//		System.out.println("The starting curve is " + (CurveProperties.isConvex(_startingCurve) ? "" : "NOT ") + "convex.");
	}

	protected void printDuringEvaluation(Curve currentCurve, int iterations) {
//		System.out
//				.println("---------------------------------------------------------------------------------------------------");
//		System.out.println("Maximum edge length ratio after " + (iterations) + " iterations: "
//				+ CurveProperties.maxEdgeLengthRatio(currentCurve));
//		System.out.println("Maximum edge length after next ratio after " + (iterations) + " iterations: "
//				+ CurveProperties.maxEdgeLengthAfterNextRatio(currentCurve));
//		System.out.println("Maximum edge length double ratio after " + (iterations) + " iterations: "
//				+ CurveProperties.maxEdgeLengthDoubleRatio(currentCurve));
//		System.out.println("Maximum edge angle after " + (iterations) + " iterations: "
//				+ CurveProperties.maxEdgeAngle(currentCurve));
		System.out.println("Maximum angle ratio after " + iterations + " iterations: " + CurveProperties.maxAngleRatio(currentCurve));

//		if (currentCurve instanceof TangentCurve) {
//			System.out.println("Minimum angle fraction after " + (iterations) + " iterations: "
//					+ ((TangentCurve) currentCurve).getMinimumTangentAngleFraction());
//		}
	}

	protected void printPostEvaluation() {
//		System.out
//		.println("---------------------------------------------------------------------------------------------------");
//		System.out.println("The result curve is " + (CurveProperties.isConvex(_resultCurve) ? "" : "NOT ") + "convex.");
		System.out
				.println("\n===================================================================================================\n");
	}

	@Override
	public void evaluate() {
		FourPointCurve currentCurve = _startingCurve;

		printPreEvaluation();

		for (int i = 0; i < _iterations; i++) {
			currentCurve = _subdivisionStrategy.subdivide(currentCurve, _pointSelector, i);
			
			printDuringEvaluation(currentCurve, i+1);
			
			if (!CurveProperties.isConvex(currentCurve) && currentCurve instanceof TangentCurve) {
				System.out.println("The generated curve is NOT convex anymore!");
				System.out.println("Unconvexity with angle: " + CurveProperties.getAngle(currentCurve, CurveProperties.getUnconvexity(currentCurve), true));
				break;
			}
		}

		_resultCurve = currentCurve;
		printPostEvaluation();
	}

	@Override
	public Curve getResult() {
		return _resultCurve;
	}
}