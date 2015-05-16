package geometry.scheme;

import geometry.Line;
import geometry.Point;

public interface TangentChooser {
	Line chooseTangent(Point a, Point b, Point c);

	Line chooseFirstTangent(Point b, Point c);

	Line chooseLastTangent(Point a, Point b);
}
