package geometry;

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
	}
	
	public abstract AbstractCurve clone();
}