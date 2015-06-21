package geometry;

import geometry.scheme.PointSelector;

import java.awt.Graphics2D;

public interface Curve extends Cloneable, Iterable<Point> {
	int size();

	Curve clone();

	Point getPoint(int i);

	void setPoint(int i, Point point);
	
	void draw(Graphics2D g, double xScale, double yScale);
	
	Curve subdivide(PointSelector pointSelector, int step);
	
	Curve subdivide(PointSelector pointSelector, int step, int index);

	void printProperties();
	
	Curve createSubcurve(int offset, int length);
}
