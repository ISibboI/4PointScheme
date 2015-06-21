package geometry.scheme;

import geometry.Curve;
import geometry.PointSelector;

public interface SubdivisionStrategy {

	Curve subdivide(Curve currentCurve, PointSelector pointSelector, int step);

}
