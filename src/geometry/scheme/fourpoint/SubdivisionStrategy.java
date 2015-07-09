package geometry.scheme.fourpoint;

import geometry.Curve;
import geometry.PointSelector;

public interface SubdivisionStrategy {

	FourPointCurve subdivide(FourPointCurve currentCurve, PointSelector pointSelector, int step);

}
