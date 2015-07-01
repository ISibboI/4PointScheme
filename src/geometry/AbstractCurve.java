package geometry;

import geometry.scheme.TangentCurve;

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
		Point start = points.next();
		path.moveTo(start.getX(), start.getY());

		for (Point point : this) {
			point = point.mul(scale);
			path.lineTo(point.getX(), point.getY());
		}

		g.draw(path);

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
	}

	public abstract AbstractCurve clone();
}