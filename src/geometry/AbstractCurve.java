package geometry;

import geometry.scheme.fourpoint.TangentCurve;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.Iterator;

public abstract class AbstractCurve implements Curve {
	@Override
	public void draw(Graphics2D g, double xScale, double yScale) {
		if (size() < 2) {
			return;
		}

		Iterator<Point> points = iterator();

		Point scale = new Point(xScale, yScale);
		Path2D.Double path = new Path2D.Double();
		Point start = points.next().mul(scale);
		path.moveTo(start.getX(), start.getY());

		while (points.hasNext()) {
			Point point = points.next().mul(scale);
			path.lineTo(point.getX(), point.getY());
		}

		// Draw unconvexity
		int unconvexity = CurveProperties.getUnconvexity(this);

		if (unconvexity != -1 && this instanceof TangentCurve) {
			Point p = getPoint(unconvexity).mul(scale);
			g.drawOval((int) (p.getX() - 5), (int) (p.getY() - 5), 10, 10);

			p = getPoint(unconvexity + 1).mul(scale);
			g.drawOval((int) (p.getX() - 4), (int) (p.getY() - 4), 8, 8);

			p = getPoint(unconvexity - 1).mul(scale);
			g.drawOval((int) (p.getX() - 4), (int) (p.getY() - 4), 8, 8);
		}

		Color tmp = g.getColor();
		g.setColor(Color.RED);
		
		final double curvatureFactor = .001;

		// Draw curvature
		for (int i = 1; i < size() - 1; i++) {
			Point a = getPoint(i - 1);
			Point b = getPoint(i);
			Point c = getPoint(i + 1);

			double curvature = curvatureFactor * getCurvature(a, b, c);

//			if (a.getX() == c.getX()) {
//				curvature *= -1;
//			}
			
			Point orthogonal = new Line(a, c).orthogonal().getDirection().normalize();
			Point lineEnd = b.add(orthogonal.mul(curvature)).mul(scale);
			b = b.mul(scale);

			g.drawLine((int) Math.round(b.getX()), (int) Math.round(b.getY()), (int) Math.round(lineEnd.getX()),
					(int) Math.round(lineEnd.getY()));
		}
		
		// Closed curve: Draw end point curvature
		Point a = getPoint(size() - 2);
		Point b = getPoint(0);
		Point c = getPoint(1);
		
		double curvature = curvatureFactor * getCurvature(a, b, c);
		
		Point orthogonal = new Line(a, c).orthogonal().getDirection().normalize();
		Point lineEnd = b.add(orthogonal.mul(curvature)).mul(scale);
		b = b.mul(scale);

		g.drawLine((int) Math.round(b.getX()), (int) Math.round(b.getY()), (int) Math.round(lineEnd.getX()),
				(int) Math.round(lineEnd.getY()));
		
		g.setColor(tmp);
		g.draw(path);
	}

	public double getCurvature(Point a, Point b, Point c) {
		double crossProductLength = c.getX() * (b.getY() - a.getY()) + b.getX() * a.getY() - c.getY()
				* (b.getX() - a.getX()) - b.getY() * a.getX();

		double distancesProduct = a.distanceTo(b) * b.distanceTo(c) * c.distanceTo(a);

		return crossProductLength / distancesProduct;
	}

	public abstract AbstractCurve clone();
}