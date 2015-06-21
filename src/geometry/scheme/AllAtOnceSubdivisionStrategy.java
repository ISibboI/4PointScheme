package geometry.scheme;

import geometry.Curve;
import geometry.PointSelector;

public class AllAtOnceSubdivisionStrategy implements SubdivisionStrategy {
	@Override
	public Curve subdivide(Curve currentCurve, PointSelector pointSelector, int step) {
		return currentCurve.subdivide(pointSelector, step);
	}
}