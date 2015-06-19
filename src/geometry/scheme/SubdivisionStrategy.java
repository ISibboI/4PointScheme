package geometry.scheme;

import geometry.Curve;

public interface SubdivisionStrategy {

	Curve subdivide(Curve currentCurve, PointSelector pointSelector, int step);

}
