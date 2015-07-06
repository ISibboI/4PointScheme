package geometry;


public interface PointSelector {
	Point getA(Curve curve);
	Point getB(Curve curve);
	Point getC(Curve curve);
	Point getD(Curve curve);
	
	void setIndex(int index);
	int getIndex();
}