import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import javax.swing.JFrame;


public class FourPointScheme {
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	
	public static void main(String[] args) throws InterruptedException {
		DrawFrame drawFrame = new DrawFrame("4-Point-Scheme", WIDTH, HEIGHT, 20, 20, 1);
		drawFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Point2D.Double[] points = new Point2D.Double[] {new Point2D.Double(100, 100), new Point2D.Double(400, 100), new Point2D.Double(400, 110)};
		
		for (int i = 0; i < 18; i++) {
			points = applyFourPointScheme(points, 1.0 / 8.01);
			System.out.print("Applied scheme " + (i + 1) + " times. ");
			System.out.println("Maximum edge length ratio is " + maxEdgeLengthRatio(points) + ".");
		}
		
		Thread.sleep(1000);
		
		drawPoints(drawFrame, points);
		
		System.out.println("Finished");
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
	
	public static Point2D.Double[] applyConvexityPreservingFourPointScheme(Point2D.Double[] source, double c, double w) {
Point2D.Double[] subdivided = new Point2D.Double[2 * source.length - 1];
		
		subdivided[0] = source[0];
		subdivided[subdivided.length - 1] = source[source.length - 1];
		
		for (int i = 0; i < subdivided.length; i += 2) {
			subdivided[i] = source[i / 2];
		}
		
		for (int i = 3; i < subdivided.length - 2; i += 2) {
			subdivided[i] = sub(mul(add(source[i / 2], source[i / 2 + 1]), w + 0.5), mul(add(source[i / 2 - 1], source[i / 2 + 2]), w));
		}
		
		subdivided[1] = sub(mul(add(source[0], source[1]), w + 0.5), mul(add(source[0], source[2]), w));
		subdivided[subdivided.length - 2] = sub(mul(add(source[source.length - 2], source[source.length - 1]), w + 0.5), mul(add(source[source.length - 3], source[source.length - 1]), w));
		
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
	
	private static double cutLines(Point2D.Double a, Point2D.Double b, Point2D.Double c, Point2D.Double d) {
		return ((d.y - c.y) * (a.x - d.x) - (a.y - d.y) * (d.x - c.x)) / ((b.y - a.y) * (d.x - c.x) - (d.y - c.y) * (b.x - a.x));
	}
}