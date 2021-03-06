import geometry.Curve;
import geometry.CurveProperties;
import geometry.Point;
import geometry.scheme.ClosedAngleHalfingTangentChooser;
import geometry.scheme.ClosedCircleTangentChooser;
import geometry.scheme.ClosedTangentChooser;
import geometry.scheme.CornerCreatingTangentChooser;
import geometry.scheme.DefaultCurve;
import geometry.scheme.DefaultTangentChooser;
import geometry.scheme.SelectableEndTangentChooser;
import geometry.scheme.SubdivisionScheme;
import geometry.scheme.chaikin.ChaikinScheme;
import geometry.scheme.fourpoint.AllAtOnceSubdivisionStrategy;
import geometry.scheme.fourpoint.C1TangentCurve;
import geometry.scheme.fourpoint.ClosedPointSelector;
import geometry.scheme.fourpoint.FourPointCurve;
import geometry.scheme.fourpoint.DefaultFourPointScheme;
import geometry.scheme.fourpoint.DefaultPointSelector;
import geometry.scheme.fourpoint.EndpointReflectingPointSelector;
import geometry.scheme.fourpoint.FourPointScheme;
import geometry.scheme.fourpoint.LongestFirstSubdivisionStrategy;
import geometry.scheme.fourpoint.SizeLimitingSubdivisionStrategy;
import geometry.scheme.fourpoint.TangentCurve;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import ui.CurveVisualizer;

@SuppressWarnings("unused")
public class Runner {
	private static final int ITERATIONS = 900;
	private static final int MAX_DRAWING_POINTS = 1000;
	private static final boolean DRAW_POINTS = true;

	private static final int STARTING_POINTS_INDEX = 7;
	private static final int CHAIKIN_ITERATIONS = 7;
	private static final int FOURPOINT_ITERATIONS = 7;

	private static final double[] TENSION_VALUES = new double[] { 1.0 / 2.0, 1.0 / 4.0, 1.0 / 7.0, 1.0 / 8.0,
			1.0 / 10.0, 1.0 / 12.0, 1.0 / 16.0, 1.0 / 32.0, 1.0 / 64.0, 1.0 / 128.0 };

	private static final Point[][] STARTING_POINTS = new Point[][] {
			{ new Point(0, 0), new Point(100, 0), new Point(100, 10), new Point(99, 10), new Point(99, 0) },
			{ new Point(-1, -1), new Point(1, -1), new Point(1, 1), new Point(-1, 1), new Point(-1, -1) },
			{ new Point(-1, 1), new Point(1, .05), new Point(1, -.05), new Point(-1, -1), new Point(-1, 1) },
			{ new Point(-1, 0), new Point(-Math.sin(Math.PI / 6), Math.cos(Math.PI / 6)),
					new Point(Math.sin(Math.PI / 6), Math.cos(Math.PI / 6)), new Point(1, 0),
					new Point(Math.sin(Math.PI / 6), -Math.cos(Math.PI / 6)),
					new Point(-Math.sin(Math.PI / 6), -Math.cos(Math.PI / 6)), new Point(-1, 0) },
			{ new Point(-1, -1), new Point(1, -1), new Point(1, 1), new Point(-1, 1), new Point(-1.1, 0),
					new Point(-1, -1) },
			{ new Point(-1, -1), new Point(1, -1), new Point(-1, 1), new Point(-1, 3), new Point(1, 3) },
			{ new Point(-1, 1), new Point(-0.5, .02), new Point(-0.5, -.02), new Point(-1, -1), new Point(-1, 1) },
			{ new Point(-0.2, 0), new Point(-0.05, -0.2), new Point(0.05, -0.2), new Point(0.2, 0), new Point(0.1, 1), new Point(0, 1.04), new Point(-0.1, 1),
					new Point(-0.2, 0) } };

	private static final Collection<Color> COLOR_COLLECTION = Arrays.asList(new Color[] { Color.BLACK, Color.BLUE,
			Color.GREEN });

	public static void main(String[] args) throws InterruptedException {
		CurveVisualizer visualizer = null;

		if (DRAW_POINTS) {
			visualizer = new CurveVisualizer();
		}

		if (DRAW_POINTS) {
			Thread.sleep(1000);
		}

		System.out.println("Evaluating...");
		Collection<? extends Curve> result = evaluateFourPointScheme();
		System.out.println("Evaluation complete.");

		if (DRAW_POINTS) {
			if (result.size() <= MAX_DRAWING_POINTS) {
				System.out.println("Drawing curve...");
				visualizer.drawCurves(new TangentCurve(STARTING_POINTS[STARTING_POINTS_INDEX], 0, 0,
						new ClosedTangentChooser()), result, COLOR_COLLECTION);
				System.out.println("Curve drawn.");
			} else {
				visualizer.dispose();
			}
		}

		System.out.println("Finished.");
	}

	public static Curve evaluateChaikin() {
		DefaultCurve startingPoints = new DefaultCurve(STARTING_POINTS[STARTING_POINTS_INDEX]);
		SubdivisionScheme scheme = new ChaikinScheme(startingPoints, CHAIKIN_ITERATIONS, true);

		scheme.evaluate();

		return scheme.getResult();
	}

	public static Collection<? extends Curve> evaluateChaikinC1TangentComparison() {
		Collection<TangentCurve> result = new ArrayList<>(2);
		TangentCurve startingPoints = new TangentCurve(STARTING_POINTS[STARTING_POINTS_INDEX], 1 / 16.0, 0.9,
				new ClosedAngleHalfingTangentChooser());
		TangentCurve dualStartingPoints = CurveProperties.dualize(startingPoints);

		SubdivisionScheme normalScheme = new DefaultFourPointScheme(startingPoints, FOURPOINT_ITERATIONS,
				new ClosedPointSelector(), new AllAtOnceSubdivisionStrategy());
		SubdivisionScheme dualScheme = new DefaultFourPointScheme(dualStartingPoints, FOURPOINT_ITERATIONS,
				new ClosedPointSelector(), new AllAtOnceSubdivisionStrategy());

		normalScheme.evaluate();
		dualScheme.evaluate();

		((TangentCurve) normalScheme.getResult()).chooseAllTangents();
		((TangentCurve) dualScheme.getResult()).chooseAllTangents();

		result.add((TangentCurve) normalScheme.getResult());
		result.add(CurveProperties.dualize((TangentCurve) dualScheme.getResult()));

		return result;
	}

	public static Collection<? extends Curve> evaluateFourPointScheme() {
		FourPointScheme scheme = null;
		FourPointCurve startingPoints = null;

		// for (double tension : TENSION_VALUES) {
		// startingPoints = new TangentCurve(new Point[] { new Point(0, 0), new
		// Point(100, 0), new Point(100, 10),
		// new Point(99, 10), new Point(99, 0) }, tension, 0.9, new
		// CornerCreatingTangentChooser());
		//
		// scheme = new DefaultFourPointScheme(startingPoints, ITERATIONS,
		// new DefaultPointSelector(), new LongestFirstSubdivisionStrategy());
		//
		// System.out.println("Evaluating...");
		// scheme.evaluate();
		// System.out.println("Evaluation complete.");
		// }

//		 startingPoints = new
//		 C1TangentCurve(STARTING_POINTS[STARTING_POINTS_INDEX], 1.0 / 16.0, 1,
//		 new ClosedTangentChooser());
		startingPoints = new TangentCurve(STARTING_POINTS[STARTING_POINTS_INDEX], 1.0 / 16.0, 0.9,
				new ClosedAngleHalfingTangentChooser());
//		 startingPoints = new
//		 FourPointCurve(STARTING_POINTS[STARTING_POINTS_INDEX], 1.0 / 16.0);

		// scheme = new DefaultFourPointScheme(startingPoints, ITERATIONS, new
		// DefaultPointSelector(),
		// new LongestFirstSubdivisionStrategy());
		scheme = new DefaultFourPointScheme(startingPoints, FOURPOINT_ITERATIONS, new ClosedPointSelector(),
				new AllAtOnceSubdivisionStrategy());
		// scheme = new DefaultFourPointScheme(startingPoints, 7, new
		// DefaultPointSelector(),
		// new SizeLimitingSubdivisionStrategy(new
		// AllAtOnceSubdivisionStrategy(), 900));

		scheme.evaluate();

		return Collections.nCopies(1, scheme.getResult());
	}
}
