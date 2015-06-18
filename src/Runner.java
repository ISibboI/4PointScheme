import geometry.Curve;
import geometry.Point;
import geometry.scheme.CornerCreatingTangentChooser;
import geometry.scheme.DefaultFourPointScheme;
import geometry.scheme.DefaultPointSelector;
import geometry.scheme.FourPointScheme;
import geometry.scheme.TangentCurve;

import java.awt.Color;

import ui.CurveVisualizer;

public class Runner {
	private static final int ITERATIONS = 10;
	private static final int MAX_DRAWING_POINTS = 1000;
	private static final boolean DRAW_POINTS = false;

	public static void main(String[] args) throws InterruptedException {
		CurveVisualizer visualizer = new CurveVisualizer();

		Curve startingPoints = new TangentCurve(new Point[] { new Point(0, 0), new Point(100, 0), new Point(100, 10),
				new Point(99, 10), new Point(99, 0) }, 1.0 / 16.0, 0.9, new CornerCreatingTangentChooser());

		FourPointScheme scheme = new DefaultFourPointScheme(startingPoints, ITERATIONS, new DefaultPointSelector());

		System.out.println("Evaluating...");
		scheme.evaluate();
		System.out.println("Evaluation complete.");

		if (DRAW_POINTS) {
			Thread.sleep(1000);

			if (scheme.getResult().size() <= MAX_DRAWING_POINTS) {
				System.out.println("Drawing curve...");
				visualizer.drawCurve(scheme.getResult(), Color.BLACK);
				System.out.println("Curve drawn.");
			} else {
				visualizer.dispose();
			}
		} else {
			visualizer.dispose();
		}

		System.out.println("Finished.");
	}
}