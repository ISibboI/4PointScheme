import geometry.Curve;
import geometry.Point;
import geometry.scheme.AllAtOnceSubdivisionStrategy;
import geometry.scheme.CornerCreatingTangentChooser;
import geometry.scheme.DefaultFourPointScheme;
import geometry.scheme.DefaultPointSelector;
import geometry.scheme.DefaultTangentChooser;
import geometry.scheme.EndpointReflectingPointSelector;
import geometry.scheme.FourPointScheme;
import geometry.scheme.LongestFirstSubdivisionStrategy;
import geometry.scheme.SelectableEndTangentChooser;
import geometry.scheme.SizeLimitingSubdivisionStrategy;
import geometry.scheme.TangentCurve;

import java.awt.Color;

import ui.CurveVisualizer;

@SuppressWarnings("unused")
public class Runner {
	private static final int ITERATIONS = 900;
	private static final int MAX_DRAWING_POINTS = 1000;
	private static final boolean DRAW_POINTS = true;
	private static final double[] TENSION_VALUES = new double[] { 1.0 / 2.0, 1.0 / 4.0, 1.0 / 7.0, 1.0 / 8.0,
			1.0 / 10.0, 1.0 / 12.0, 1.0 / 16.0, 1.0 / 32.0, 1.0 / 64.0, 1.0 / 128.0 };

	private static final Point[][] STARTING_POINTS = new Point[][] {
			{ new Point(0, 0), new Point(100, 0), new Point(100, 10), new Point(99, 10), new Point(99, 0) },
			{ new Point(0, 0), new Point(100, 0), new Point(100, 100), new Point(0, 100), new Point(0, 0) } };

	public static void main(String[] args) throws InterruptedException {
		CurveVisualizer visualizer = null;
		Curve startingPoints = null;
		FourPointScheme scheme = null;

		if (DRAW_POINTS) {
			visualizer = new CurveVisualizer();
		}

		// for (double tension : TENSION_VALUES) {
		// startingPoints = new TangentCurve(new Point[] { new Point(0, 0), new
		// Point(100, 0), new Point(100, 10),
		// new Point(99, 10), new Point(99, 0) }, tension, 0.9, new
		// CornerCreatingTangentChooser());
		//
		// scheme = new DefaultFourPointScheme(startingPoints, ITERATIONS, new
		// DefaultPointSelector(), new LongestFirstSubdivisionStrategy());
		//
		// System.out.println("Evaluating...");
		// scheme.evaluate();
		// System.out.println("Evaluation complete.");
		// }

		startingPoints = new TangentCurve(STARTING_POINTS[0], 1.0 / 16.0, 0.9, new SelectableEndTangentChooser(new Point(10, 1), new Point(1, 1)));

//		scheme = new DefaultFourPointScheme(startingPoints, ITERATIONS, new DefaultPointSelector(),
//				new LongestFirstSubdivisionStrategy());
		scheme = new DefaultFourPointScheme(startingPoints, 20, new DefaultPointSelector(),
				new AllAtOnceSubdivisionStrategy());
//		scheme = new DefaultFourPointScheme(startingPoints, 7, new DefaultPointSelector(),
//				new SizeLimitingSubdivisionStrategy(new AllAtOnceSubdivisionStrategy(), 900));

		System.out.println("Evaluating...");
		scheme.evaluate();
		System.out.println("Evaluation complete.");

		if (DRAW_POINTS) {
			Thread.sleep(1000);

			if (scheme.getResult().size() <= MAX_DRAWING_POINTS) {
				System.out.println("Drawing curve...");
				visualizer.drawCurves(startingPoints, scheme.getResult(), Color.BLACK);
				System.out.println("Curve drawn.");
			} else {
				visualizer.dispose();
			}
		}

		System.out.println("Finished.");
	}
}