package geometry;

import java.awt.Graphics2D;

public interface Curve extends Cloneable, Iterable<Point> {
	int size();

	Curve clone();

	Point getPoint(int i);

	void setPoint(int i, Point point);
	
	void draw(Graphics2D g, double xScale, double yScale);

	void printProperties();
	
	Curve createSubcurve(int offset, int length);
}
