import java.awt.Color;

import geometry.Curve;
import geometry.Point;
import geometry.scheme.DefaultFourPointScheme;
import geometry.scheme.DefaultPointSelector;
import geometry.scheme.DefaultTangentChooser;
import geometry.scheme.FourPointScheme;
import geometry.scheme.TangentCurve;
import ui.CurveVisualizer;

public class Runner {
	public static void main(String[] args) throws InterruptedException {
		CurveVisualizer visualizer = new CurveVisualizer();

		Curve startingPoints = new TangentCurve(new Point[] { new Point(0, 0), new Point(100, 3), new Point(110, 3),
				new Point(111, 2.9), new Point(121, 0) }, 1.0 / 16.0, 0.9, new DefaultTangentChooser());
		int iterations = 20;

		FourPointScheme scheme = new DefaultFourPointScheme(startingPoints, iterations, new DefaultPointSelector());

		System.out.println("Evaluating...");
		scheme.evaluate();
		System.out.println("Evaluation complete.");

		Thread.sleep(1000);

		System.out.println("Drawing curve...");
		visualizer.drawCurve(scheme.getResult(), Color.BLACK);
		System.out.println("Curve drawn.");
	}
}