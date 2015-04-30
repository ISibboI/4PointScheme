import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import javax.swing.JFrame;


public class FourPointScheme {
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	
	public static void main(String[] args) throws InterruptedException, IOException {
//		DrawFrame drawFrame = new DrawFrame("4-Point-Scheme", WIDTH, HEIGHT, 20, 20, 1);
//		drawFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Point2D.Double[] points = new Point2D.Double[] {new Point2D.Double(100, 100), new Point2D.Double(400, 100), new Point2D.Double(400, 110)};
		File f = new File("result.txt");
		
		if (f.exists()) {
			f.delete();
		}
		
		f.createNewFile();
		
		PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream(f)));
		
		for (int j = 0; j < 1000; j++) {
			points = new Point2D.Double[3];
			Point2D.Double[] startingPoints = points;
			
			for (int i = 0; i < points.length; i++) {
				points[i] = new Point2D.Double(Math.random(), Math.random());
			}
			
			for (int i = 0; i < 17; i++) {
				points = applyConvexityPreservingFourPointScheme(points, 0.9999, 1.0 / 16.0);
//				System.out.print("Applied scheme " + (i + 1) + " times. ");
//				System.out.println("Maximum edge length ratio is " + maxEdgeLengthRatio(points) + ".");
			}
			
			double maxEdgeLengthRatio = maxEdgeLengthRatio(points);
			
			if (maxEdgeLengthRatio > 1.43845) {
				out.print("!!! ");
				System.out.println("!!! ratio: " + maxEdgeLengthRatio + "; points: " + pointsToString(startingPoints));
			}
			
			out.println("ratio: " + maxEdgeLengthRatio + "; points: " + pointsToString(startingPoints));
		}
		
		out.close();
		
//		Thread.sleep(1000);
//		
//		drawPoints(drawFrame, points);
//		
//		System.out.println("Finished");
	}
	
	private static String pointsToString(Double[] startingPoints) {
		StringBuilder str = new StringBuilder();
		str.append("[");
		
		if (startingPoints.length != 0) {
			str.append(startingPoints[0].x).append("/").append(startingPoints[0].y);
			
			for (int i = 1; i < startingPoints.length; i++) {
				str.append(", ");
				str.append(startingPoints[0].x).append("/").append(startingPoints[0].y);
			}
		}
		
		str.append("]");
		return str.toString();
	}
	
	

	private static void drawPoints(DrawFrame drawFrame, Double[] points) {
		Graphics2D g = drawFrame.startRender();
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.BLACK);
		
		for (int i = 0; i < points.length - 1; i++) {
			g.drawLine((int) Math.round(points[i].x), (int) Math.round(points[i].y), (int) Math.round(points[i + 1].x), (int) Math.round(points[i + 1].y));
		}
		
		drawFrame.switchToUIRender();
		drawFrame.finishRender();
	}

	public static Point2D.Double[] applyFourPointScheme(Point2D.Double[] source, double tension) {
		Point2D.Double[] subdivided = new Point2D.Double[2 * source.length - 1];
		
		subdivided[0] = source[0];
		subdivided[subdivided.length - 1] = source[source.length - 1];
		
		for (int i = 0; i < subdivided.length; i += 2) {
			subdivided[i] = source[i / 2];
		}
		
		for (int i = 3; i < subdivided.length - 2; i += 2) {
			subdivided[i] = sub(mul(add(source[i / 2], source[i / 2 + 1]), tension + 0.5), mul(add(source[i / 2 - 1], source[i / 2 + 2]), tension));
		}
		
		subdivided[1] = sub(mul(add(source[0], source[1]), tension + 0.5), mul(add(source[0], source[2]), tension));
		subdivided[subdivided.length - 2] = sub(mul(add(source[source.length - 2], source[source.length - 1]), tension + 0.5), mul(add(source[source.length - 3], source[source.length - 1]), tension));
		
		return subdivided;
	}
	
	public static Point2D.Double[] applyConvexityPreservingFourPointScheme(Point2D.Double[] source, double C, double W) {
		Point2D.Double[] subdivided = new Point2D.Double[2 * source.length - 1];
		
		subdivided[0] = source[0];
		subdivided[subdivided.length - 1] = source[source.length - 1];
		
		for (int i = 0; i < subdivided.length; i += 2) {
			subdivided[i] = source[i / 2];
		}
		
		for (int i = 1; i < subdivided.length; i += 2) {
			Point2D.Double a;
			Point2D.Double d;
			
			if (i == 1) {
				a = source[i / 2];
			} else {
				a = source[i / 2 - 1];
			}
			
			Point2D.Double b = source[i / 2];
			Point2D.Double c = source[i / 2 + 1];
			
			if (i == subdivided.length - 2) {
				d = source[i / 2 + 1];
			} else {
				d = source[i / 2 + 2];
			}
			
			Point2D.Double displacement = add(sub(b, a), sub(c, d));
			Point2D.Double tangentB;
			Point2D.Double tangentC;
			
			if (i == 1) {
				tangentB = add(b, new Point2D.Double(b.y - c.y, c.x - b.x));
			} else {
				tangentB = add(b, sub(c, a));
			}
			
			if (i == subdivided.length - 1) {
				tangentC = add(c, new Point2D.Double(b.y - c.y, c.x - b.x));
			} else {
				tangentC = add(c, sub(b, d));
			}
				
			Point2D.Double center = mul(add(b, c), 0.5);
			
			double alpha = cutLines(center, add(center, displacement), b, tangentB);
			double beta = cutLines(center, add(center, displacement), c, tangentC);
			
			if (alpha <= 0) {
				alpha = W / C;
			}
			
			if (beta <= 0) {
				beta = W / C;
			}
			
			double mu = C * Math.min(alpha, beta);
			double w = Math.min(W, mu);
			
			subdivided[i] = add(center, mul(displacement, w));
		}
		
		return subdivided;
	}

	private static Double sub(Double a, Double b) {
		return new Point2D.Double(a.x - b.x, a.y - b.y);
	}

	private static Double mul(Double a, double d) {
		return new Point2D.Double(a.x * d, a.y * d);
	}

	private static Double add(Double a, Double b) {
		return new Point2D.Double(a.x + b.x, a.y + b.y);
	}
	
	private static double maxEdgeLengthRatio(Point2D.Double[] points) {
		double maxSquaredRatio = 1;
		
		for (int i = 0; i < points.length - 2; i++) {
			Point2D.Double a = points[i];
			Point2D.Double b = points[i + 1];
			Point2D.Double c = points[i + 2];
			
			double squaredLengthA = sqr(a.x - b.x) + sqr(a.y - b.y);
			double squaredLengthB = sqr(c.x - b.x) + sqr(c.y - b.y);
			
			double ratio = squaredLengthA / squaredLengthB;
			
			if (ratio < 1) {
				ratio = 1 / ratio;
			}
			
			if (ratio > maxSquaredRatio) {
				maxSquaredRatio = ratio;
			}
		}
		
		return Math.sqrt(maxSquaredRatio);
	}

	private static double sqr(double d) {
		return d * d;
	}
	
	/**
	 * Returns the factor applied to (b - a) to get the intersection point.
	 */
	private static double cutLines(Point2D.Double a, Point2D.Double b, Point2D.Double c, Point2D.Double d) {
		double over = (d.y - c.y) * (a.x - d.x) - (a.y - d.y) * (d.x - c.x);
		double under = (b.y - a.y) * (d.x - c.x) - (d.y - c.y) * (b.x - a.x);
		return under == 0 ? 0 : over / under;
	}
}